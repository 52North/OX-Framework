/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.render.sos;

import java.awt.Image;
import java.util.Arrays;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.kml.x22.BalloonStyleType;
import net.opengis.kml.x22.DocumentType;
import net.opengis.kml.x22.KmlDocument;
import net.opengis.kml.x22.KmlType;
import net.opengis.kml.x22.PlacemarkType;
import net.opengis.kml.x22.PointType;
import net.opengis.kml.x22.StyleType;

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.feature.OXFAbstractFeatureType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFSamplingPointType;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

public class SimpleOM2KMLRenderer implements IFeatureDataRenderer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOM2KMLRenderer.class);

    public IVisualization renderLayer(OXFFeatureCollection observationCollection,
                                      ParameterContainer paramCon,
                                      int screenW,
                                      int screenH,
                                      IBoundingBox bbox,
                                      Set<OXFFeature> selectedFoiSet) {
        String[] observedProperties;
        // which observedProperty has been used?:
        ParameterShell observedPropertyPS = paramCon.getParameterShellWithServiceSidedName("observedProperty");
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
            observedProperties = observedPropertyPS.getSpecifiedTypedValueArray(String[].class);
        }
        else if (observedPropertyPS.hasSingleSpecifiedValue()) {
            observedProperties = new String[] {(String) observedPropertyPS.getSpecifiedValue()};
        }
        else {
            throw new IllegalArgumentException("no observedProperties found.");
        }

        // find tuples:
        ObservationSeriesCollection obsValues4FOI = new ObservationSeriesCollection(observationCollection,
                                                                                    selectedFoiSet,
                                                                                    observedProperties,
                                                                                    true);

        // ////// Let's produce KML:

        KmlDocument xb_kmlDocument = KmlDocument.Factory.newInstance();
        KmlType xb_kml = xb_kmlDocument.addNewKml();
        
        XmlCursor cursor = xb_kml.newCursor();
        
        DocumentType xb_document = DocumentType.Factory.newInstance();

        PlacemarkType[] placemarks = new PlacemarkType[selectedFoiSet.size()];
        StyleType[] styles = new StyleType[selectedFoiSet.size()];
        
        int i = 0;
        for (OXFFeature feature : selectedFoiSet) {
            String foiID = feature.getID();
            String styleID = "FOI_STYLE_" + foiID;
            String foiName = ((String[]) feature.getAttribute(OXFAbstractFeatureType.NAME))[0];
            Geometry foiGeom = (Geometry) feature.getAttribute(OXFSamplingPointType.POSITION);
            String[] foiCoordinates = new String[] {"" + foiGeom.getCoordinates()[0].x,
                                                    "" + foiGeom.getCoordinates()[0].y};

            // add placemark:
            PlacemarkType xb_placemark = PlacemarkType.Factory.newInstance();
            xb_placemark.setName(foiName);
            xb_placemark.setDescription("Feature Of Interest: '" + foiName + "' (id := '" + foiID + "')");
            xb_placemark.setStyleUrl("#" + styleID);

            PointType xb_Point = PointType.Factory.newInstance();
            xb_Point.setCoordinates(Arrays.asList(foiCoordinates));
            xb_placemark.setAbstractGeometryGroup(xb_Point);

            // add style:
            StyleType xb_style = StyleType.Factory.newInstance();
            xb_style.setId(styleID);
            BalloonStyleType xb_balloonStyle = xb_style.addNewBalloonStyle();

            String htmlText = "";
            htmlText += "<b><font color=\"#CC0000\" size=\"+3\">$[name]</font></b>";
            htmlText += "<br/><br/>";
            htmlText += "<font face=\"Arial\">$[description]</font>";
            htmlText += "<br/><br/>";
            htmlText += "Extra text!!";
            htmlText += "<br/><br/>";

            xb_balloonStyle.setText(htmlText);

            // add placemark & style to the arrays:
            placemarks[i] = xb_placemark;
            styles[i] = xb_style;
            
            i++;
        }

        xb_document.setAbstractFeatureGroupArray(placemarks);
        xb_document.setAbstractStyleSelectorGroupArray(styles);
        xb_kml.setAbstractFeatureGroup(xb_document);
        
        cursor.toChild(new QName("http://earth.google.com/kml/2.1", "Feature"));
        cursor.setName(new QName("http://earth.google.com/kml/2.1", "Document"));
        
        XmlCursor docCursor = xb_document.newCursor();
        for (int childIndex=0; childIndex<i; childIndex++) {
            docCursor.toChild(new QName("http://earth.google.com/kml/2.1", "Feature"), childIndex);
            docCursor.setName(new QName("http://earth.google.com/kml/2.1", "Placemark"));
        }
        
        return new TextVisualization(xb_kmlDocument.xmlText(XmlUtil.PRETTYPRINT));
    }

    public String getDescription() {
        return "This renderer produces a KML representation out of O&M.";
    }

    /**
     * @return the type of the service whose data can be rendered with this ServiceRenderer. In this case
     *         "OGC:SOS" will be returned.
     */
    public String getServiceType() {
        return "OGC:SOS";
    }

    /**
     * @return the versions of the services whose data can be rendered with this ServiceRenderer.
     */
    public String[] getSupportedVersions() {
        return new String[] {"0.0.0", "1.0.0"};
    }

    // ////////////////////////// new IVisualization class for textual representations: //////////////////////

    /**
     * 
     * @author <a href="mailto:broering@52north.org">Arne Broering</a>
     * 
     */
    class TextVisualization implements IVisualization {

        private String renderedText;
        
        public TextVisualization(String renderedText) {
            this.renderedText = renderedText;
        }
        
        public Image getLegend() {
           throw new UnsupportedOperationException("Not yet supported.");
        }

        /**
         * 
         */
        public String getRendering() {
            return renderedText;
        }

    }
}