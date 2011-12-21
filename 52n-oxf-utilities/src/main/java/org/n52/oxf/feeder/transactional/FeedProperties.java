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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.sosMobile.SOSmobileRequestBuilder;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.valueDomains.time.TimePeriod;

import com.topografix.gpx.x1.x0.GpxDocument;
import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk;
import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Wpt;
import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk.Trkseg;
import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk.Trkseg.Trkpt;

/**
 * 
 * Class encapsulates all information (parameter values) for a feeding process, e.g.
 * time period of the dummy data to be created, and service information.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class FeedProperties {

    private static Logger LOGGER = LoggingHandler.getLogger(FeedProperties.class);

    private String serviceType = "SOS";

    protected TimePeriod timePeriod = (TimePeriod) TimeFactory.createTime("2009-01-02T12:00:00Z/2009-01-03T12:00:00Z");
    // private TimePeriod period = (TimePeriod)
    // TimeFactory.createTime("2008-11-01T10:00/2008-11-01T12:00");

    private List<Wpt> wayPoints;

    protected long sleepTime = 500;

    private double minDoubleValue = -0.0;

    private double maxDoubleValue = 40.0;

    private DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
    private DecimalFormat valueFormat = new DecimalFormat("#.000", symbols);

    private String serviceVersion = "1.0.0";

    private String observedPropertyName = "temperature"; // "windSpeed";

    private String observedPropertyURI = "urn:ogc:def:phenomenon:surfaceTemperature"; // "urn:x-ogc:def:property:OGC::WindSpeed";

    // private String[] observedProperties = {"urn:ogc:def:phenomenon:temperature",
    // "urn:ogc:def:phenomenon:pressure",
    // "urn:spacefleet:uss:enterprise:warpspeed"};

    protected String sensorID = "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-100";

    private String domainFeatureName = "Europe";// "Muenster";

    private String domainFeatureID = "eu-001";

    private String firstLastCoordMS = "7.581596374511719 51.987079081231315";
    @SuppressWarnings("unused")
    private String dFLMS = firstLastCoordMS + ", 7.610778808593750 51.9915189247043"
            + ", 7.623138427734375 51.9923645592681, 7.65987396240234 51.98750194243185 "
            + ", 7.663993835449219 51.9699498470919, 7.67120361328125 51.97143040982793 "
            + ", 7.672920227050781 51.9686807543837, 7.67292022705078 51.96296939243455 "
            + ", 7.663307189941406 51.9538720171228, 7.64682769775390 51.94583093952526 "
            + ", 7.642364501953125 51.9439262625933, 7.63687133789062 51.94096327104416 "
            + ", 7.627944946289062 51.9386350689522, 7.61489868164062 51.93651841674403, " + firstLastCoordMS;

    private String dFLEUstartend = "25.83984375 71.26694980922422";
    private String dFLEU = dFLEUstartend
            + ", 19.3359375 70.46032874320807, 12.3046875 68.29540429506305, 9.4921875 65.9034471904945"
            + ", 5.80078125 63.73585307238071, 1.23046875 62.21880354905434, -4.921875 59.92419185901303, -9.4921875 58.30025274211579"
            + ", -11.42578125 55.61807011936093, -13.18359375 52.203566364440995, -13.7109375 45.262514498373584"
            + ", -13.0078125 41.1654214723505, -10.72265625 36.51316734886143, -7.91015625 36.08817178048462"
            + ", -2.63671875 36.51316734886143, 1.93359375 38.18983996724313, 8.4375 38.74037299991194, 11.07421875 38.05155080658339"
            + ", 14.58984375 34.943586620477944, 20.91796875 34.79936932613168, 28.125 34.65489930099977, 32.6953125 34.65489930099977"
            + ", 35.68359375 36.37175953328168, 40.4296875 37.774187545982066, 44.47265625 37.91299989407177, 48.515625 39.558270399240385"
            + ", 49.39453125 44.13806655389196, 47.109375 49.88330863099783, 42.36328125 67.23125915059808, 39.0234375 69.62038929507884"
            + ", 34.1015625 71.37952989910096, 29.70703125 71.43557496264391, 27.0703125 71.37952989910096, 25.87726593017578 71.27043409783333"
            + ", " + dFLEUstartend;

    private String domainFeatureLocationLinearRing = dFLEU; // dFLMS;

    private String domainFeatureDescription = "Europe and stuff"; // "Area around the city of Muenster with the border along major roads around MS.";

    private String domainFeatureLocationSrsName = "4326";

    private String foiName = "SamplingPoint";

    private String foiID = "foiPoint";

    private String valueUnitOfMeasurement = "degree celsius";

    private String serviceUrlString = "http://localhost:8080/52nSOSv3.0-mobile/sos";

    private boolean mobileEnabled = true;

    private boolean positionFixed = false;

    private String positionName = "current sensor position";

    private String positionReferenceFrame = "urn:ogc:def:crs:EPSG:4326";

    private String uom = "degree";

    private boolean status = true;

    private boolean mobile = true;

    private ParameterContainer[] inputList;

    private ParameterContainer[] outputList;

    private URL serviceURL;

    private String timeString;

    protected int pointCount = 15; // 15000;

    protected String gpxFile = "D:\\aasee.gpx";

    // private String gpxFile = "D:\\Bikemap.net-M�nster - Essen - Dinslaken - Kleve.gpx";

    private boolean useTrackpoints = true; // if not, use waypoints

    private List<Trkpt> trackPoints;

    protected List<Boolean> activeList;

    protected ArrayList<Boolean> mobileList;

    /**
	 * 
	 */
    public FeedProperties() {
        try {
            this.serviceURL = new URL(serviceUrlString);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        this.inputList = new ParameterContainer[1];
        this.inputList[0] = new ParameterContainer();

        this.outputList = new ParameterContainer[1];
        this.outputList[0] = new ParameterContainer();

        try {
            this.inputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST_OBS_PROP_NAME,
                                           this.observedPropertyName);
            this.inputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_INPUT_LIST_OBS_PROP_DEFINITION,
                                           this.observedPropertyURI);

            this.outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_NAME,
                                            this.observedPropertyName);
            this.outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_QUANTITY_DEF,
                                            this.observedPropertyURI);
            this.outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_OFFERING_ID, "temp_eu");
            this.outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_OFFERING_NAME,
                                            "temperature on tour");
            this.outputList[0].addParameterShell(SOSmobileRequestBuilder.REGISTER_SENSOR_OUTPUT_LIST_UOM_CODE, "Cel");
        }
        catch (OXFException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * 
     */
    public void init() {
        readGpx();
        
        this.activeList = new ArrayList<Boolean>();
        this.mobileList = new ArrayList<Boolean>();
    }

    /**
     * 
     */
    private void readGpx() {
        // read points from gpx
        File gpxfile = new File(this.gpxFile);

        GpxDocument gpx = null;
        try {
            gpx = GpxDocument.Factory.parse(gpxfile);
            // System.out.println(XmlBeansHelper.formatStringRequest(gpx));

            boolean valid = gpx.validate();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("read gpx file, is valid=" + valid);
            }
        }
        catch (XmlException e) {
            e.printStackTrace();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Wpt[] waypointArray = gpx.getGpx().getWptArray();

        Trk[] trackArray = gpx.getGpx().getTrkArray();

        LOGGER.info("loaded " + trackArray.length + " tracks and " + waypointArray.length + " waypoints");

        Trk track = trackArray[0];
        Trkseg[] trackSegmentArray = track.getTrksegArray();
        Trkseg trackSegment = trackSegmentArray[0];

        Trkpt[] trackPointArray = trackSegment.getTrkptArray();
        LOGGER.info("loaded tracksegment with " + trackPointArray.length + " trackpoints");

        if ( !useTrackpoints) {
            this.wayPoints = Arrays.asList(waypointArray);
            if (this.wayPoints.size() > pointCount) {
                this.wayPoints = this.wayPoints.subList(0, pointCount);
            }
            if (this.wayPoints.size() < pointCount) {
                LOGGER.warn("given points from xml are less than point count... ");
            }
        }
        else {
            this.trackPoints = Arrays.asList(trackPointArray);
            if (this.trackPoints.size() > pointCount) {
                this.trackPoints = this.trackPoints.subList(0, pointCount);
            }
            if (this.trackPoints.size() < pointCount) {
                LOGGER.warn("given points from xml are less than point count... ");
            }
        }

        // TODO load domainFeature from bounds-Element of gpx-file

        gpx = null;
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        FeedProperties properties = new FeedProperties();
        properties.init();
        LOGGER.info("testing ... \n\n" + properties);
    }

    /**
     * @return the serviceType
     */
    synchronized public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *        the serviceType to set
     */
    synchronized public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the timePeriod
     */
    synchronized public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * @return the sleepTime
     */
    synchronized public long getSleepTime() {
        return sleepTime;
    }

    /**
     * @return the minDoubleValue
     */
    synchronized public double getMinDoubleValue() {
        return minDoubleValue;
    }

    /**
     * @return the maxDoubleValue
     */
    synchronized public double getMaxDoubleValue() {
        return maxDoubleValue;
    }

    /**
     * @return the serviceVersion
     */
    synchronized public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * @return the observedProperty
     */
    synchronized public String getObservedProperty() {
        return observedPropertyURI;
    }

    /**
     * @return the domainFeatureName
     */
    synchronized public String getDomainFeatureName() {
        return domainFeatureName;
    }

    /**
     * @return the domainFeatureID
     */
    synchronized public String getDomainFeatureID() {
        return domainFeatureID;
    }

    /**
     * @return the domainFeatureLocationLinearRing
     */
    synchronized public String getDomainFeatureLocationLinearRing() {
        return domainFeatureLocationLinearRing;
    }

    /**
     * @return the domainFeatureDescription
     */
    synchronized public String getDomainFeatureDescription() {
        return domainFeatureDescription;
    }

    /**
     * @return the domainFeatureLocationSrsName
     */
    synchronized public String getDomainFeatureLocationSrsName() {
        return domainFeatureLocationSrsName;
    }

    /**
     * @return the foiName
     */
    public String getFoiName() {
        return foiName;
    }

    /**
     * @return the foiID
     */
    synchronized public String getFoiID() {
        return foiID;
    }

    /**
     * @return the valueUnitOfMeasurement
     */
    synchronized public String getValueUnitOfMeasurement() {
        return valueUnitOfMeasurement;
    }

    /**
     * @return the mobileEnabled
     */
    synchronized public boolean isMobileEnabled() {
        return mobileEnabled;
    }

    /**
     * @return the serviceURL
     */
    synchronized public URL getServiceURL() {
        return serviceURL;
    }

    /**
     * @return the lon
     */
    synchronized public String getLon() {
        return this.trackPoints.get(0).getLon().toPlainString();
    }

    /**
     * @return the sensorID
     */
    synchronized public String getSensorID() {
        return sensorID;
    }

    /**
     * @return the lat
     */
    synchronized public String getLat() {
        return this.trackPoints.get(0).getLat().toPlainString();
    }

    /**
     * @return the positionFixed
     */
    synchronized public boolean isPositionFixed() {
        return positionFixed;
    }

    /**
     * @return the positionName
     */
    synchronized public String getPositionName() {
        return positionName;
    }

    /**
     * @return the uom
     */
    synchronized public String getUom() {
        return uom;
    }

    /**
     * @return the status
     */
    synchronized public boolean isStatus() {
        return status;
    }

    /**
     * @return the mobile
     */
    synchronized public boolean isMobile() {
        return mobile;
    }

    /**
     * @return the inputList
     */
    synchronized public ParameterContainer[] getInputList() {
        return inputList;
    }

    /**
     * @return the outputList
     */
    synchronized public ParameterContainer[] getOutputList() {
        return outputList;
    }

    /**
     * @param outputList
     *        the outputList to set
     */
    synchronized public void setOutputList(ParameterContainer[] outputList) {
        this.outputList = outputList;
    }

    /**
     * @return the valueFormat
     */
    synchronized public DecimalFormat getValueFormat() {
        return valueFormat;
    }

    /**
     * @return the positionReferenceFrame
     */
    synchronized public String getPositionReferenceFrame() {
        return positionReferenceFrame;
    }

    /**
     * @return the timeString
     */
    synchronized public String getTimeString() {
        return timeString;
    }

    /**
     * @return the trackPoints
     */
    public List<Trkpt> getTrackPoints() {
        return trackPoints;
    }

    /**
     * @param trackPoints
     *        the trackPoints to set
     */
    public void setTrackPoints(List<Trkpt> trackPoints) {
        this.trackPoints = trackPoints;
    }

    /**
     * 
     * @param time
     */
    public void setTimeString(String time) {
        this.timeString = time;
    }

    /**
     * 
     * @return
     */
    public List<Boolean> getActiveList() {
        return this.activeList;
    }

    /**
     * @return the mobileList
     */
    public ArrayList<Boolean> getMobileList() {
        return mobileList;
    }

}
