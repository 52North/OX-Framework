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

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.OWSException;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * Class encapsulates all information necessary for registering a sensor with a SOS. It builds the parameter
 * container and has the register() method to do the registration.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class RegisterSensor {

    private static Logger LOGGER = LoggingHandler.getLogger(RegisterSensor.class);

    protected SOSAdapter sosAdapter;

    protected String observedProperty;

    protected String sensorID;

    protected String uom;

    protected URL serviceURL;

    protected String serviceVersion;

    protected String serviceType;

    /**
     * 
     * @param feedProperties
     */
    public RegisterSensor(FeedProperties feedProperties) {
        this(feedProperties.getServiceType(),
             feedProperties.getServiceVersion(),
             feedProperties.getServiceURL(),
             feedProperties.getSensorID(),
             feedProperties.getObservedProperty(),
             feedProperties.getUom());
    }

    /**
     * 
     * @param serviceURL
     * @param sensorID
     * @param lat
     * @param lon
     * @param observedProperty
     * @param positionFixed
     * @param positionName
     * @param uom
     * @param mobile
     * @param status
     */
    public RegisterSensor(String serviceType,
                          String serviceVersion,
                          URL serviceURL,
                          String sensorID,
                          String observedProperty,
                          String uom) {
        this.serviceType = serviceType;
        this.serviceVersion = serviceVersion;
        this.sensorID = sensorID;
        this.observedProperty = observedProperty;
        this.uom = uom;
        this.serviceURL = serviceURL;

        this.sosAdapter = new SOSAdapter(this.serviceVersion);
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        FeedProperties properties = new HamburgLissabonFeederProperties();

        RegisterSensor registerM = new RegisterSensor(properties);

        registerM.register();
    }

    public void register() {

        ParameterContainer paramCon = null;
        try {
            paramCon = buildParameterContainer();
        }
        catch (OXFException e1) {
            LOGGER.error("creating parameter container", e1);
        }

        Operation op = new Operation(SOSAdapter.REGISTER_SENSOR,
                                     this.serviceURL.toString() + "?",
                                     this.serviceURL.toString());

        OperationResult opResult = null;
        try {
            opResult = this.sosAdapter.doOperation(op, paramCon);
        }
        catch (OXFException e) {
            LOGGER.error("error in run()", e);
        }
        catch (ExceptionReport e) {
            LOGGER.error("ows error in run()");
            Iterator<OWSException> iter = e.getExceptionsIterator();

            while (iter.hasNext()) {
                OWSException ex = iter.next();
                System.out.println(ex.getExceptionCode() + " --- " + ex.getMessage() + "\n"
                        + Arrays.toString(ex.getExceptionTexts()));
                System.out.println("\n" + ex.getSendedRequest());
            }

            e.printStackTrace();
        }

        if (LOGGER.isDebugEnabled()) {
            System.out.println(opResult);
        }
    }

    /**
     * 
     * @return
     * @throws OXFException
     */
    public ParameterContainer buildParameterContainer() throws OXFException {
        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER, this.serviceType);
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER, this.serviceVersion);
        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER, this.sensorID);

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
                                   this.observedProperty);

        paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER, this.uom);

        return paramCon;
    }

}
