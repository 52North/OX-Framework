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


package org.n52.oxf.wms.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Contact;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.DatasetParameter;
import org.n52.oxf.ows.capabilities.GetRequestMethod;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.OnlineResource;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ows.capabilities.ServiceContact;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;
import org.n52.oxf.valueDomains.IntegerRangeValueDomain;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.n52.oxf.valueDomains.spatial.BoundingBox2D;
import org.n52.oxf.wms.capabilities.Style;
import org.n52.oxf.wms.capabilities.WMSLayer;
import org.n52.wmsclientcore.capabilities.model.ContactInformation;
import org.n52.wmsclientcore.capabilities.model.ContactPersonPrimary;
import org.n52.wmsclientcore.capabilities.model.HTTPItem;
import org.n52.wmsclientcore.capabilities.model.Layer;
import org.n52.wmsclientcore.capabilities.model.Request;
import org.n52.wmsclientcore.capabilities.model.WMS_Capabilities;
import org.n52.wmsclientcore.capabilities.model.types.DCPType;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class WMSCapabilitiesMapper {

    public static final String LAYERS = "LAYERS";
    public static final String STYLES = "STYLES";
    public static final String SRS = "SRS";
    public static final String BBOX = "BBOX";
    public static final String TRANSPARENT = "TRANSPARENT";
    public static final String WIDTH = "WIDTH";
    public static final String HEIGHT = "HEIGHT";
    public static final String TIME = "TIME";
    public static final String ELEVATION = "ELEVATION";
    public static final String BGCOLOR = "BGCOLOR";
    public static final String EXCEPTIONS = "EXCEPTIONS";
    public static final String FORMAT = "FORMAT";
    public static final String VERSION = "VERSION";

    /**
     * 
     */
    public ServiceDescriptor mapCapabilities(WMS_Capabilities capabilities) throws ExceptionReport, OXFException {
        Contents contents = initContents(capabilities);
        OperationsMetadata operationMetadata = initOperationsMetadata(capabilities, contents);
        ServiceIdentification serviceIdentification = initServiceIdentification(capabilities);

        ServiceProvider provider = initServiceProvider(capabilities);
        
        ServiceDescriptor descriptor = new ServiceDescriptor(capabilities.getVersion(),
                                                             serviceIdentification,
                                                             provider,
                                                             operationMetadata,
                                                             contents);

        return descriptor;
    }

    /**
     * returns <code>null</code>, if the WMS does not contain any layers. The content only contains named
     * Layers!
     * 
     * If the layerTitle is empty or <code>null</code>, then the Name will be set as the Dataset
     * title. This has to be done because the title in WMS Capabilities is not mandatory.
     * 
     * For the outputFormat of the content, the outputFormat of the getMap request will be used.
     * 
     * @param caps
     *        the capabilties of the WMS
     * @return a metadata element describing the dataLayers of the service.
     */
    private Contents initContents(WMS_Capabilities caps) throws OXFException {
        Layer layers = caps.getCapability().getLayer();
        if (layers == null) {
            return null;
        }
        Contents contents = new Contents();
        ArrayList layerList = new ArrayList();
        layers.fillLayerList(layerList);
        
        String[] outputFormats = caps.getCapability().getRequest().getGetMap().getFormat();
        
        for (Iterator iter = layerList.iterator(); iter.hasNext();) {
            Layer layer = (Layer) iter.next();

            String identifier = layer.getName();

            String title = layer.getTitle();

            Style[] styles = new Style[layer.getStyle().length];
            for (int n = 0; n < styles.length; n++) {
                styles[n] = new Style(layer.getStyle()[n].getName(), layer.getStyle()[n].getTitle());
            }

            Vector allCRS = layer.getAllCRS();
            int srsCounter = 0;
            String[] availableCRSs = new String[allCRS.size()];
            for (int i = 0; i < allCRS.size(); i++) {
                availableCRSs[i] = (String) allCRS.get(i);
            }

            BoundingBox[] bboxes = produceBoundingBoxes(layer);;
            if (identifier != null) {
                WMSLayer dataIdent = new WMSLayer(title,
                                                  identifier,
                                                  bboxes,
                                                  outputFormats,
                                                  availableCRSs,
                                                  styles,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null);
                contents.addDataIdentication(dataIdent);
            }
        }
        return contents;
    }

    private ServiceProvider initServiceProvider(WMS_Capabilities caps) {
        ContactInformation ci = caps.getService().getContactInformation();

        ServiceContact sc = null;
        String providerName = null;
        Contact contact = null; // contact will stay null! parsing has to be implemented!
        if (ci != null) {
            String contactPosition = ci.getContactPosition();

            ContactPersonPrimary cpp = ci.getContactPersonPrimary();
            if (cpp != null) {
                String contactPerson = cpp.getContactPerson();
                String contactOrg = cpp.getContactOrganization();
                sc = new ServiceContact(contactPerson, contactPosition, contactOrg, contact);

                providerName = cpp.getContactOrganization();
            }

        }

        if (providerName == null || providerName.equals("")) {
            providerName = "Unnamed provider";
        }
        ServiceProvider sp = new ServiceProvider(providerName, sc);
        return sp;
    }

    /**
     * 
     * @param caps
     * @return
     */
    private ServiceIdentification initServiceIdentification(WMS_Capabilities caps) {
        ServiceIdentification identification = new ServiceIdentification(caps.getService().getTitle(),
                                                                         WMSAdapter.SERVICE_TYPE,
                                                                         new String[] {caps.getVersion()});
        return identification;
    }

    /**
     * 
     * @param caps
     * @param contents
     * @return
     * @throws OXFException
     */
    private OperationsMetadata initOperationsMetadata(WMS_Capabilities caps, Contents contents) throws OXFException {
        // SETTING UP THE FEATUREINFOOPERATION
        Request operations = caps.getCapability().getRequest();
        if (operations.getGetFeatureInfo() != null) {
            /*
             * NOT YET IMPLEMENTED!
             */

            // Parameter parameter = new Parameter();
            // parameter.setName()
            // Operation getFeatureOperation = new Operation();
            // getFeatureOperation.setName(OPERATION_GETFEATUREINFO);
            // operations.getGetFeatureInfo().
        }
        
        // SETTING UP THE GETMAPOPERATION

        // SETTING THE RESTRICTION FOR THE AVAILABLE PARAMETER VALUES
        ArrayList<Layer> layerList = new ArrayList<Layer>();
        caps.getCapability().getLayer().fillLayerList(layerList);

        List<Parameter> paramList = new ArrayList<Parameter>();
        ArrayList<String> possibleLayerNames = new ArrayList<String>();
        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            possibleLayerNames.add(contents.getDataIdentification(i).getIdentifier());
        }
        StringValueDomain layerDomain = new StringValueDomain(possibleLayerNames);
        Parameter layersParameter = new Parameter(LAYERS,
                                                  true,
                                                  layerDomain,
                                                  Parameter.COMMON_NAME_RESOURCE_ID);
        paramList.add(layersParameter);
        // Initializing and setting the parameters;
        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            ArrayList<String> styleDomainNames = new ArrayList<String>();
            for (Layer layer : layerList) {
                if (layer.getName() != null
                        && layer.getName().equals(contents.getDataIdentification(i).getIdentifier())) {
                    /**
                     * SETTING THE PARAMETER STYLE
                     */
                    if (layer.getStyle() != null) {
                        for (int styleInt = 0; styleInt < layer.getStyleCount(); styleInt++) {
                            styleDomainNames.add(layer.getStyle(styleInt).getName());
                        }
                    }
                    if ( !styleDomainNames.isEmpty()) {
                        DatasetParameter styleParameter = new DatasetParameter(STYLES,
                                                                               true,
                                                                               new StringValueDomain(styleDomainNames),
                                                                               contents.getDataIdentification(i),
                                                                               Parameter.COMMON_NAME_STYLE);
                        paramList.add(styleParameter);
                    }
                    /*
                     * SETTING THE BBOX PARAMETER
                     */
                    IBoundingBox[] bboxes = produceBoundingBoxes(layer);
                    for (IBoundingBox bbox : bboxes) {
                        DatasetParameter bboxParameter = new DatasetParameter(BBOX,
                                                                              true,
                                                                              bbox,
                                                                              contents.getDataIdentification(i),
                                                                              Parameter.COMMON_NAME_BBOX);
                        paramList.add(bboxParameter);
                    }

                    /**
                     * SRS, transparent PARAMETER
                     */
                    ArrayList<String> srsCodes = new ArrayList<String>();
                    for (Iterator srsIter = layer.getAllCRS().iterator(); srsIter.hasNext();) {
                        srsCodes.add((String) srsIter.next());
                    }
                    DatasetParameter srsParameter = new DatasetParameter(SRS,
                                                                         true,
                                                                         new StringValueDomain(srsCodes),
                                                                         contents.getDataIdentification(i),
                                                                         Parameter.COMMON_NAME_SRS);
                    paramList.add(srsParameter);
                    DatasetParameter transparentParameter = new DatasetParameter(TRANSPARENT,
                                                                                 false,
                                                                                 new StringValueDomain(Boolean.toString(layer.getOpaque())),
                                                                                 contents.getDataIdentification(i),
                                                                                 TRANSPARENT);
                    paramList.add(transparentParameter);

                }
            }

            // Parameter timeParameter = new Parameter("TIME", false, null, Parameter.COMMON_NAME_TIME);
            // Parameter exceptionsParameter = new Parameter("EXCEPTIONS", false, null, null);
            // Parameter elevationParameter = new Parameter("ELEVATIONS", false, null, null);
            // Parameter backgroundColorParameter = new Parameter("BGCOLOR", false, null, null);
        }
        Parameter widthParameter = new Parameter(WIDTH,
                                                 true,
                                                 new IntegerRangeValueDomain(0, Integer.MAX_VALUE),
                                                 Parameter.COMMON_NAME_WIDTH);
        Parameter heightParameter = new Parameter(HEIGHT,
                                                  true,
                                                  new IntegerRangeValueDomain(0, Integer.MAX_VALUE),
                                                  Parameter.COMMON_NAME_HEIGHT);
        paramList.add(widthParameter);
        paramList.add(heightParameter);
        StringValueDomain formatDomain = new StringValueDomain(caps.getCapability().getRequest().getGetMap().getFormat());
        Parameter formatParameter = new Parameter(FORMAT,
                                                  true,
                                                  formatDomain,
                                                  Parameter.COMMON_NAME_FORMAT);
        paramList.add(formatParameter);

        StringValueDomain versionDomain = new StringValueDomain(caps.getVersion());
        Parameter versionParameter = new Parameter(VERSION,
                                                   true,
                                                   versionDomain,
                                                   Parameter.COMMON_NAME_VERSION);
        paramList.add(versionParameter);

        Parameter[] parameters = new Parameter[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            parameters[i] = paramList.get(i);
        }
        DCPType[] mapDCPs = caps.getCapability().getRequest().getGetMap().getDCPType();
        String getMapURL = null;
        for (DCPType type : mapDCPs) {
            if (type != null && type.getHTTP() != null) {
                for (HTTPItem item : type.getHTTP().getHTTPItem()) {
                    if (item != null && item.getGet() != null) {
                        getMapURL = item.getGet().getOnlineResource().getHref();
                        break;
                    }
                }
            }
        }
        OnlineResource or = new OnlineResource(getMapURL);
        GetRequestMethod method = new GetRequestMethod(or);
        DCP dcp = new DCP(method, null);
        Operation getMapOperation = new Operation(WMSAdapter.OPERATION_GETMAP,
                                                  parameters,
                                                  null,
                                                  new DCP[] {dcp});
        OperationsMetadata operationMetadata = new OperationsMetadata(new Operation[] {getMapOperation});
        return operationMetadata;
    }

    /**
     * 
     * @param layer
     * @return all BoundingBoxes of the Layer.
     * @throws OXFException
     */
    private BoundingBox[] produceBoundingBoxes(Layer layer) throws OXFException {
        Vector<BoundingBox> bboxes = new Vector<BoundingBox>();

        if (layer.getBoundingBox().length != 0) {
            for (int boxInt = 0; boxInt < layer.getBoundingBox().length; boxInt++) {
                org.n52.wmsclientcore.capabilities.model.BoundingBox tempBox = layer.getBoundingBox()[boxInt];
                bboxes.add(new BoundingBox2D(tempBox.getCRS(),
                                             tempBox.getMinx(),
                                             tempBox.getMiny(),
                                             tempBox.getMaxx(),
                                             tempBox.getMaxy()));
            }
        }
        if (layer.getEX_GeographicBoundingBox() != null) {
            bboxes.add(new BoundingBox2D("EPSG:4326",
                                         layer.getEX_GeographicBoundingBox().getWestBoundLongitude(),
                                         layer.getEX_GeographicBoundingBox().getSouthBoundLatitude(),
                                         layer.getEX_GeographicBoundingBox().getEastBoundLongitude(),
                                         layer.getEX_GeographicBoundingBox().getNorthBoundLatitude()));
        }

        BoundingBox[] bboxArray = new BoundingBox[bboxes.size()];
        return bboxes.toArray(bboxArray);
    }

}