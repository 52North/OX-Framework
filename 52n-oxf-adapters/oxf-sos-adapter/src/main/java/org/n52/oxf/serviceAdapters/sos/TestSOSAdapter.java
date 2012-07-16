/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package org.n52.oxf.serviceAdapters.sos;

import java.util.Iterator;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.SOSFoiStore;
import org.n52.oxf.feature.sos.SOSObservationStore;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sos.caps.ObservationOffering;
import org.n52.oxf.util.SosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates how to use the SOSAdapter. You might use it as an example for your own code.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TestSOSAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSOSAdapter.class);

	// private final String url = "http://localhost:8080/52nSOSv2_WeatherSA_artifical/sos";

	// private final String url = "http://140.114.79.14:8080/52nSOSv2/sos";

	// private final String url = "http://v-swsl.uni-muenster.de:8080/52nSOSv3/sos";
	
	// private final String url = "http://mars.uni-muenster.de:8080/OWS5SOS/sos";

	// private final String url = "http://v-swe.uni-muenster.de:8080/HWS-SOS/sos";
	
	// private final String url = "http://v-swsl.uni-muenster.de:8080/52nSOS_AirBase_LinkedData/sos";
	
	// private final String url = "http://sensorweb.demo.52north.org/PegelOnlineSOSv2/sos";
	
	// private final String url = "http://fluggs.wupperverband.de/sos/sos";
	
	private final String url = "http://v-swe.uni-muenster.de:8080/WeatherSOS/sos";
	
	private final String serviceVersion = "1.0.0";

	public static void main(String[] args) throws OXFException, ExceptionReport {
		new TestSOSAdapter().testGetObservation();
//		    	new TestSOSAdapter().testGetCapabilities();
//		    	new TestSOSAdapter().testGetFeatureOfInterest();
//		    	new TestSOSAdapter().testRegisterSensor();
		// new TestSOSAdapter().testInsertCategoryObservation();
	}

	public void testGetCapabilities() throws ExceptionReport, OXFException {

		SOSAdapter adapter = new SOSAdapter(serviceVersion);

		ParameterContainer paramCon = new ParameterContainer();
		paramCon.addParameterShell(ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER, serviceVersion);
		paramCon.addParameterShell(ISOSRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER, SosUtil.SERVICE_TYPE);

		OperationResult opResult = adapter.doOperation(new Operation("GetCapabilities", url + "?", url), paramCon);

		ServiceDescriptor desc = adapter.initService(opResult);

		LOGGER.info(desc.getServiceIdentification().getTitle());

		ObservationOffering obsOff = (ObservationOffering) desc.getContents().getDataIdentification(0);

		LOGGER.info("Offering: " + obsOff.getIdentifier());
	}

	/**
	 * This method shows you how you may use the SOSAdapter in your application. It shows how to connect to
	 * and access the SOS and to unmarshall the returned O&M data into OXFFeature objects:
	 * 
	 * <pre>
	 * SOSAdapter sosAdapter = new SOSAdapter();
	 * 
	 * Operation op = new Operation(&quot;GetObservation&quot;, &quot;get_not_used&quot;, url);
	 * 
	 * // put all parameters you want to use into a ParameterContainer:
	 * ParameterContainer paramCon = new ParameterContainer();
	 * 
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_SERVICE_PARAMETER, SOSAdapter.SERVICE_TYPE);
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_VERSION_PARAMETER, SOSAdapter.SUPPORTED_VERSIONS[0]);
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_OFFERING_PARAMETER, &quot;WeatherSA&quot;);
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER,
	 *                            &quot;text/xml;subtype=\&quot;om/0.0.0\&quot;&quot;);
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
	 *                            &quot;urn:ogc:def:phenomenon:OGC:1.0.30:dryBulbTemp&quot;);
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_RESULT_MODEL_PARAMETER, &quot;Observation&quot;); // &quot;Measurement&quot;);
	 * 
	 * ITime eventTime = TimeFactory.createTime(&quot;2007-04-10T01:45:00+02/2007-05-10T17:45:00+02&quot;);
	 * paramCon.addParameterShell(new ParameterShell(new Parameter(SOSRequestBuilder_000.GET_OBSERVATION_EVENT_TIME_PARAMETER,
	 *                                                             false,
	 *                                                             new TemporalValueDomain(eventTime),
	 *                                                             Parameter.COMMON_NAME_TIME), eventTime));
	 * 
	 * paramCon.addParameterShell(SOSRequestBuilder_000.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, &quot;inline&quot;);
	 * 
	 * LOGGER.info(sosAdapter.getSOSRequestBuilder().buildGetObservationRequest(paramCon));
	 * 
	 * // now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
	 * // you receive is an OperationResult.
	 * OperationResult opResult = sosAdapter.doOperation(op, paramCon);
	 * 
	 * IFeatureStore featureStore = new SOSObservationStore();
	 * 
	 * // The OperationResult can be used as an input for the 'unmarshalFeatures' operation of your
	 * // SOSObservationStore to parse the returned O&amp;M document and to build up OXFFeature objects.
	 * OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);
	 * 
	 * LOGGER.info(&quot;featureCollection.size:&quot; + featureCollection.size());
	 * 
	 * // and now do something with these feature objects ...
	 * 
	 * LOGGER.info(&quot;geom of '0'.foi: &quot;
	 *         + ((OXFFeature) featureCollection.toList().get(0).getAttribute(&quot;featureOfInterest&quot;)).getGeometry());
	 * 
	 * </pre>
	 * 
	 */
	@SuppressWarnings("unused")
	public void testGetObservation() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("GetObservation", "http://GET_URL_not_used", url);

		// put all parameters you want to use into a ParameterContainer:
		ParameterContainer paramCon = new ParameterContainer();

		//////// definition of request parameters:
		String omFormat = "text/xml;subtype=\"om/1.0.0\"";
		String responseMode = "inline";
		String resultModel = "Measurement";
		
		/*
		// example parameters for 'http://fluggs.wupperverband.de/sos/sos'
		String offering = "Luft";
		String observedProperty = "Lufttemperatur";
		String eventTime = "2010-01-01T00:00:00.000+0100/2010-01-15T09:54:19+0100";
		String procedure = null;
		String featureOfInterest = null;
		*/
		
		// example parameters for 'http://v-swe.uni-muenster.de:8080/WeatherSOS/sos'
		String offering = "HUMIDITY";
		String observedProperty = "urn:ogc:def:property:OGC::RelativeHumidity";
		String eventTime = "2011-01-01T00:00:00.000+0200/2011-01-06T14:00:00.000+0200";
		String procedure = null;
		String featureOfInterest = null;
		
		paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SosUtil.SERVICE_TYPE);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, SosUtil.SUPPORTED_VERSIONS[1]);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, omFormat);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, responseMode);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, resultModel);
        
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offering);
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperty);
        if (eventTime != null)
        	paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, eventTime);
        if (procedure != null)
        	paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, procedure);
        if (featureOfInterest != null)
        	paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, featureOfInterest);

		LOGGER.info("Sent request: " + SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildGetObservationRequest(paramCon));

        // now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
        // you receive is an OperationResult.
        OperationResult opResult = sosAdapter.doOperation(op, paramCon);

        LOGGER.info("Received response: " + new String(opResult.getIncomingResult()));

        IFeatureStore featureStore = new SOSObservationStore();

        OXFFeatureCollection oxfFeatureColl = featureStore.unmarshalFeatures(opResult);
        
        LOGGER.info("Count of received observations: " + oxfFeatureColl.size());
        
    }

	public OXFFeatureCollection testGetFeatureOfInterest() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("GetFeatureOfInterest", "http://GET_URL_not_used", url);

		ParameterContainer paramCon = new ParameterContainer();

		paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER, SosUtil.SERVICE_TYPE);
		paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER, SosUtil.SUPPORTED_VERSIONS[1]);
		paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_ID_PARAMETER, new String[] 
        {//"foi_1001",
		 "foi_sampSur_1001",
//		 "foi_2001",
//		 "4_1272645745097",
//		 "4_1272645763895",
//		 "4_1272645777473",
//		 "4_1272645788598",
//		 "8_1272898241728",
//		 "8_1272898306881",
//		 "8_1273154975701",
//		 "8_1273157811373",
//		 "8_1273158433373",
//		 "8_1273159194873",
//		 "8_1273159235982",
//		 "8_1273159406373",
//		 "8_1273159448216",
//		 "8_1273159679341",
//		 "8_1273159761107",
//		 "8_1273159794263",
//		 "8_1273159870654",
//		 "foi_10001",
//		 "foi_20001",
//		 "foi_30001",
//		 "foi_40001",
//		 "foi_50001",
//		 "bruehlstrasse"
		 });

		OperationResult opResult = sosAdapter.doOperation(op, paramCon);


        // now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
        // you receive is an OperationResult.
        // OperationResult opResult = sosAdapter.doOperation(op, paramCon);

		SOSFoiStore featureStore = new SOSFoiStore();
		OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);

		LOGGER.info("featureCollection.size:" + featureCollection.size());
		for (Iterator iterator = featureCollection.iterator(); iterator.hasNext();) {
			OXFFeature feature = (OXFFeature) iterator.next();
			LOGGER.info("feature ID: " + feature.toString());
			LOGGER.info("feature geometry: " + feature.getGeometry());
		}
		// and now do something with those features ...

		return featureCollection;
	}

	public void testInsertObservation() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("InsertObservation", "http://GET_URL_not_used", url);

		// put all parameters you want to use into a ParameterContainer:
		ParameterContainer paramCon = new ParameterContainer();

		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER, "SOS");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER, serviceVersion);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
				"urn:ogc:def:phenomenon:OGC:1.0.30:waterspeed");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER, "id_2001");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER,
		"urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-2");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME, "2008-02-15T15:13:13Z");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER, "23");

		//Test with new FOI
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME, "Airport Berlin");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION, "40.85 -74.0608");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS, "4326");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC, "The Airport in Berlin Germany");

		LOGGER.info(SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildInsertObservation(paramCon));

		// now use this ParameterContainer as an input for the 'doOperation' method of your SOSAdapter. What
		// you receive is an OperationResult.
		//        OperationResult opResult = sosAdapter.doOperation(op, paramCon);

	}
	public void testInsertCategoryObservation() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("InsertObservation", "http://GET_URL_not_used", url);

        String sosURL = url;
        // get the values from InsertObservation
        String eventTime = "2009-02-26T23:44:15+00:00";
        String foiParameter = "STATIONID";
        String value = "ALL_OKAY";
        // this method is only used to insert invalid data: each incoming message is stored
        String procedure = "urn:ogc:object:feature:Sensor:human-sensor-web:reporter:" + 
        	"0815_23-42_005" + "_invaliddata";
		
		// put all parameters you want to use into a ParameterContainer
		ParameterContainer paramCon = new ParameterContainer();
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE,ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER,"1.0.0");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER,"SOS");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,eventTime);

		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER,foiParameter);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME,foiParameter);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC, "waterpoint for humans (@ID@)".replaceAll("@ID@", foiParameter));
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION, 7.0d + " " + 52.0d);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS, "4326");
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,"urn:ogc:def:phenomenon:OGC:0.1:WaterPointState");

		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER,procedure);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER,value);
		paramCon.addParameterShell(ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, "http://example.com/myCodespace");

		LOGGER.info(SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildInsertObservation(paramCon));
	}
	
	public void testRegisterSensor() throws OXFException, ExceptionReport {
		SOSAdapter sosAdapter = new SOSAdapter(serviceVersion);

		Operation op = new Operation("RegisterSensor", "http://GET_URL_not_used", url);

		// put all parameters you want to use into a ParameterContainer:
		ParameterContainer paramCon = new ParameterContainer();

		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER, "SOS");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER, serviceVersion);
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER, "ifgi-sensor-5");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER, "52.00");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER, "7.97");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
		"urn:ogc:def:phenomenon:OGC:1.0.30:waterlevel");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_FIXED_PARAMETER, "true");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_NAME_PARAMETER, "TestArea_5200_0797");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER, "mm");
		paramCon.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE, ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);

		LOGGER.info(SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildRegisterSensor(paramCon));

		OperationResult opResult = sosAdapter.doOperation(new Operation(SOSAdapter.REGISTER_SENSOR, url + "?", url), paramCon);



	}
}