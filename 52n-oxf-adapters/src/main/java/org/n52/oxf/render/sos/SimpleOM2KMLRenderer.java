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

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.feature.OXFAbstractFeatureType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFSamplingPointType;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.XmlBeansHelper;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 */
public class SimpleOM2KMLRenderer implements IFeatureDataRenderer {

    private static Logger LOGGER = LoggingHandler.getLogger(SimpleOM2KMLRenderer.class);

    /**
     * 
     */
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
            observedProperties = (String[]) observedPropertyPS.getSpecifiedValueArray();
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
        
        String rendererdText = XmlBeansHelper.formatStringRequest(xb_kmlDocument);
        rendererdText = rendererdText.replace("ns:", "");
        
        return new TextVisualization(rendererdText);
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