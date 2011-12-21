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
 
 Created on: 22.07.2007
 *********************************************************************************/


package org.n52.oxf.feeder.transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * A feeder for a SensorObservationService on basis of the transactional profile.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class TransactionalFeeder {

    private static Logger LOGGER = LoggingHandler.getLogger(TransactionalFeeder.class);

    protected FeedProperties feedProperties;

    protected FeedMobile feed;

    /**
     * 
     * @param feedProperties2 
     * @param isMobile
     */
    public TransactionalFeeder(FeedProperties feedPropertiesP) {
        this.feedProperties = feedPropertiesP;
        this.feed = new FeedMobile(feedProperties);
    }

    /**
     * to be overwritten by subclasses!
     */
    public TransactionalFeeder() {
        //
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        TransactionalFeeder feeder = new TransactionalFeeder(new HamburgLissabonFeederProperties());
        feeder.register();
        feeder.feed();
    }

    /**
     * 
     * @param sensors
     * @throws OXFException
     */
    public void register() {
        if (feedProperties.isMobileEnabled()) {
            RegisterMobileSensor register = new RegisterMobileSensor(feedProperties);
            register.register();
        }
        else {
            LOGGER.error("no registerer for static sos implemented");
        }
    }

    /**
     * feed the db with artificial values within the given time period and at the given points
     * 
     * @param bb
     * @param timePeriod
     * @param points
     */
    public void feed() {
        if (feedProperties.isMobileEnabled()) {

            ExecutorService exec = Executors.newFixedThreadPool(10);
            exec.submit(feed);

            // start feeding
            feed.start();

        }
        else {
            LOGGER.error("no feeder for static sos implemented");
        }
    }
}
