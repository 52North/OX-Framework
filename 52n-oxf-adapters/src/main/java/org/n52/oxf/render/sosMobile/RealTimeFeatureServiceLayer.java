/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 12.03.2006
 *********************************************************************************/

package org.n52.oxf.render.sosMobile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.media.jai.PropertyChangeEmitter;

import org.apache.log4j.Logger;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.util.OXFEventSupport;

/**
 * 
 * A service layer that uses an inner thread to fire refresh event in a defined 
 * intervall to create a pseudo-real-time service update by letting the be updated.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class RealTimeFeatureServiceLayer extends FeatureServiceLayer {

    private static Logger LOGGER = LoggingHandler.getLogger(RealTimeFeatureServiceLayer.class);

    private RealTimeRefresher refresher;
    private ExecutorService executor;

    protected String layerID;

    /**
     * @param adapter
     * @param featureRenderer
     * @param featureStore
     * @param featurePicker
     * @param descriptor
     * @param parameterContainer
     * @param layerID
     * @param layerTitle
     * @param resourceOperationName
     * @param caching
     * @param intervall
     * @param controller
     */
    public RealTimeFeatureServiceLayer(IServiceAdapter adapter,
                                       IFeatureDataRenderer featureRenderer,
                                       IFeatureStore featureStore,
                                       IFeaturePicker featurePicker,
                                       ServiceDescriptor descriptor,
                                       ParameterContainer parameterContainer,
                                       String layerID,
                                       String layerTitle,
                                       String resourceOperationName,
                                       long intervall) {
        super(adapter,
              featureRenderer,
              featureStore,
              featurePicker,
              descriptor,
              parameterContainer,
              layerID,
              layerTitle,
              resourceOperationName,
              false);
        this.refresher = new RealTimeRefresher(intervall, this);
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.submit(this.refresher);
    }

    /**
     * 
     */
    public void endRefreshing() {
        this.refresher.interrupt();
        this.executor.shutdownNow();
        this.refresher = null;
        this.executor = null;

        LOGGER.info("Refreshing ended and objects nulled.");
    }

    /**
     * @return the refresher
     */
    public RealTimeRefresher getRefresher() {
        return refresher;
    }
    
    /**
     * @return the event support from the abstract class
     */
    protected OXFEventSupport getEventSupport() {
        return eventSupport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RealTimeFeatureServiceLayer [layerID=");
        sb.append(this.layerID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This thread is given the {@link IContextLayer} and an intervall within which the layer is to be
     * refreshed. To do that, a LAYER_REAL_TIME_REFRESH event is fired.
     * 
     * @author d_nues01
     * 
     */
    public class RealTimeRefresher extends Thread implements IEventEmitter, PropertyChangeEmitter {

        private static final long WHILE_TRUE_THREAD_SLEEP_TIME = 50;
        public static final String PROPERTY_EVENT_NEXT_UPDATE_IN_MILLIS = "penuim";
        private static final long PROPERTY_CHANGE_EVENT_SLEEP_MILLIS = 100;
        private Logger LOG = LoggingHandler.getLogger(RealTimeRefresher.class);
        private boolean refreshing = false;
        private long intervall;
        private IContextLayer layer;
        private ArrayList<PropertyChangeListener> propertyChangeListeners;
        private long nextUpdate;

        /**
         * 
         * @param intervall
         * @param realTimeFeatureServiceLayer
         * @param layerID
         */
        public RealTimeRefresher(long intervall, IContextLayer realTimeFeatureServiceLayer) {
            this.intervall = intervall;
            this.layer = realTimeFeatureServiceLayer;
            this.propertyChangeListeners = new ArrayList<PropertyChangeListener>();
        }

        /**
         * 
         */
        @Override
        public void run() {
            try {
                while (true && !isInterrupted()) {
                    if (this.refreshing) {
                        innerRun();
                    }
                    else {
                        Thread.sleep(WHILE_TRUE_THREAD_SLEEP_TIME);
                    }
                }
            }
            catch (InterruptedException e) {
                LOG.warn("InterruptedException, message=" + e.getMessage() + "... hope this was on purpose!");
            }
        }

        /**
         * 
         * @throws InterruptedException
         */
        private void innerRun() throws InterruptedException {
            LOG.info("starting refresh for layer " + this.layer);

            long updateTime = System.currentTimeMillis();
            try {
                getEventSupport().fireEvent(EventName.LAYER_REAL_TIME_REFRESH, new Long(updateTime));
            }
            catch (OXFEventException e) {
                LOG.error("could not fire LAYER_REAL_TIME_REFRESH", e);
            }
            this.nextUpdate = updateTime + this.intervall;

            startPropertyEventsAndSleep();
        }

        /**
         * 
         * @throws InterruptedException
         */
        private void startPropertyEventsAndSleep() throws InterruptedException {
            while (System.currentTimeMillis() < this.nextUpdate + PROPERTY_CHANGE_EVENT_SLEEP_MILLIS) {
                long now = System.currentTimeMillis();
                long timeTo = this.nextUpdate - now;
                fireNextUpdateInEvent(timeTo);
                Thread.sleep(PROPERTY_CHANGE_EVENT_SLEEP_MILLIS);
            }
        }

        /**
         * 
         * @param timeTo
         */
        private void fireNextUpdateInEvent(long timeTo) {
            Long l = Long.valueOf(Math.max(timeTo, 0));
            PropertyChangeEvent evt = new PropertyChangeEvent(this, PROPERTY_EVENT_NEXT_UPDATE_IN_MILLIS, null, l);
            for (PropertyChangeListener listener : this.propertyChangeListeners) {
                listener.propertyChange(evt);
            }
        }

        /**
         * 
         */
        public void startRefreshing() {
            this.refreshing = true;
            LOG.info("started " + this);
        }

        /**
         * 
         */
        public void stopRefreshing() {
            this.refreshing = false;
            resetNextUpdate();
            LOG.info("stopped " + this);
        }

        /**
         * @return the intervall
         */
        public long getIntervall() {
            return intervall;
        }

        /**
         * @param intervall
         *        the intervall to set
         */
        public void setIntervall(long intervall) {
            this.intervall = intervall;
            resetNextUpdate();
        }

        /**
         * 
         */
        private void resetNextUpdate() {
            this.nextUpdate = System.currentTimeMillis();
        }

        /**
         * @return the running
         */
        public boolean isRefreshing() {
            return refreshing;
        }

        public void addPropertyChangeListener(PropertyChangeListener arg0) {
            this.propertyChangeListeners.add(arg0);
        }

        public void addPropertyChangeListener(String arg0, PropertyChangeListener arg1) {
            addPropertyChangeListener(arg1);
        }

        public void removePropertyChangeListener(PropertyChangeListener arg0) {
            this.propertyChangeListeners.remove(arg0);
        }

        public void removePropertyChangeListener(String arg0, PropertyChangeListener arg1) {
            removePropertyChangeListener(arg1);
        }

        public void addEventListener(IEventListener listener) {
            getEventSupport().addOXFEventListener(listener);
        }

        public void removeEventListener(IEventListener listener) {
            getEventSupport().removeOXFEventListener(listener);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("RealTimeRefreshTask [intervall=");
            sb.append(intervall);
            sb.append("; running=");
            sb.append(this.refreshing);
            sb.append("; next update=");
            sb.append(this.nextUpdate);
            sb.append("]");
            return sb.toString();
        }
    }

}
