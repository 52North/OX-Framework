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
 
 Created on: 17.10.2006
 *********************************************************************************/

package org.n52.oxf.render.sos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Set;

import org.n52.oxf.OXFException;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.valueDomains.time.ITimePosition;

import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ProportionalCircleMapRenderer implements IFeatureDataRenderer {

    private static final int NUMBER_OF_CLASSES = 5;
    private static final int MIN_DOT_SIZE = 2;
    private static final int MAX_DOT_SIZE = 30;

    private static final Color POINT_INNER_COLOR = Color.BLUE;

    private static final int LEGEND_WIDTH = 400;
    private static final int LEGEND_HEIGHT = 400;
    
    protected static final int X_SHIFT = -10;
    protected static final int Y_SHIFT = -10;

    private ObservationSeriesCollection obsValues4FOI = null;

    private Set<OXFFeature> selectedFeaturesCache = null;
    
    /**
     * 
     */
    public ProportionalCircleMapRenderer() {
        super();
    }

    public AnimatedVisualization renderLayer(OXFFeatureCollection observationCollection,
                                             ParameterContainer paramCon,
                                             int screenW,
                                             int screenH,
                                             IBoundingBox bbox,
                                             Set<OXFFeature> selectedFeatures) throws OXFException {
        if (selectedFeaturesCache == null) {
            selectedFeaturesCache = selectedFeatures;
        }
        
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
        if (obsValues4FOI == null) {
            obsValues4FOI = new ObservationSeriesCollection(observationCollection,
                                                        selectedFeaturesCache,
                                                        observedProperties,
                                                        true);
        }

        // render Legend:
        Image legend = renderLegend(obsValues4FOI, observedProperties[0]);

        //
        // render Images for each time stamp (frame) and add them to the resultVis:
        //
        AnimatedVisualization resultVis = new AnimatedVisualization(legend);

        ITimePosition[] sortedArray = obsValues4FOI.getSortedTimeArray();
        for (int i = 0; i < sortedArray.length; i++) {
            resultVis.addFrame(renderFrame(sortedArray,
                                           i,
                                           screenW,
                                           screenH,
                                           bbox,
                                           selectedFeaturesCache,
                                           obsValues4FOI));
        }

        return resultVis;
    }

    private Image renderLegend(ObservationSeriesCollection obsValues, String observedProperty) {

        double lowestValue = (Double) obsValues.getMinimum(0);
        double highestValue = (Double) obsValues.getMaximum(0);

        double classDistance = (highestValue - lowestValue) / (NUMBER_OF_CLASSES - 2);
        int dotSizeDistance = (MAX_DOT_SIZE - MIN_DOT_SIZE) / (NUMBER_OF_CLASSES - 2);

        BufferedImage image = new BufferedImage(LEGEND_WIDTH,
                                                LEGEND_HEIGHT,
                                                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, LEGEND_WIDTH, LEGEND_HEIGHT);

        // draw information:
        observedProperty = observedProperty.split(":")[observedProperty.split(":").length - 1];
        g.setColor(Color.BLACK);
        g.drawString("Observed Property:   '" + observedProperty + "'", 25, 25);

        for (int i = 1; i <= NUMBER_OF_CLASSES; i++) {
            // draw text:
            int x_stringLocation = 100;
            int y_location = i * 60;
            g.setColor(Color.BLACK);

            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.GERMAN);
            df.applyPattern("#,###,##0.00");

            double lowerBorder = lowestValue + classDistance * (i - 1);
            double upperBorder = lowestValue + classDistance * i;

            g.drawString(i + ". class: " + df.format(lowerBorder) + " - " + df.format(upperBorder),
                         x_stringLocation,
                         y_location + 10);

            // draw symbol:
            int x_symbolLocation = 30;
            g.setColor(POINT_INNER_COLOR);
            g.fillOval(x_symbolLocation, y_location, i * dotSizeDistance, i * dotSizeDistance);
        }

        return image;
    }

    /**
     * renders one frame of the animation.
     */
    private Image renderFrame(ITimePosition[] sortedTimeArray,
                              int currentTimeIndex,
                              int screenW,
                              int screenH,
                              IBoundingBox bbox,
                              Set<OXFFeature> selectedFeatures,
                              ObservationSeriesCollection obsValues) {
        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);

        BufferedImage image = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        ITimePosition currentTimePos = sortedTimeArray[currentTimeIndex];
        
        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenW, screenH);

        // draw time-string into map:
        g.setColor(Color.BLACK);
        g.drawString(currentTimePos.toString(), 20, 20);

        for (OXFFeature dotFeature : selectedFeatures) {

            //
            // draw the points into the image at the georeferenced position of the corresponding feature:
            //
            Point pRealWorld = (Point) dotFeature.getGeometry();

            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                         screenW,
                                                                         screenH,
                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                            pRealWorld.getCoordinate().y));
            
            ObservedValueTuple tuple = obsValues.getTuple(dotFeature, currentTimePos);

            // if there wasn't a tuple for the current time position go backwards through the sortedTimeArray and take the most recent one:
            int j = currentTimeIndex - 1;
            while (tuple == null && j>=0) {
                tuple = obsValues.getTuple(dotFeature, sortedTimeArray[j]);
                j--;
            }
            
            // if a tuple was found -> draw the dot:
            if (tuple != null) {
                int dotSize = computeDotSize((Double) tuple.getValue(0),
                                             (Double) obsValues.getMinimum(0),
                                             (Double) obsValues.getMaximum(0));
                g.setColor(POINT_INNER_COLOR);
                g.fillOval(pScreen.x - (dotSize / 2), pScreen.y - (dotSize / 2), dotSize, dotSize);
            }
            // otherwise draw "no data available"
            else {
                g.setColor(Color.BLACK);
                
                // draw point of feature:
                g.fillOval(pScreen.x - (FeatureGeometryRenderer.DOT_SIZE_POINT / 2),
                           pScreen.y - (FeatureGeometryRenderer.DOT_SIZE_POINT / 2),
                           FeatureGeometryRenderer.DOT_SIZE_POINT,
                           FeatureGeometryRenderer.DOT_SIZE_POINT);
                
                g.drawString("No data available", pScreen.x + X_SHIFT, pScreen.y + Y_SHIFT);
            }
        }

        return image;
    }

    private static int computeDotSize(double value, double lowestValue, double highestValue) {

        //
        // classify:
        //
        int grade = 0;

        double classDistance = (highestValue - lowestValue) / (NUMBER_OF_CLASSES - 2);

        double valueStep = lowestValue;
        while ( (valueStep <= highestValue) && (value >= valueStep)) {
            grade++;
            valueStep = valueStep + classDistance;
        }

        //
        // compute dot size:
        //
        int dotSizeDistance = (MAX_DOT_SIZE - MIN_DOT_SIZE) / (NUMBER_OF_CLASSES - 2);
        int dotSize = MIN_DOT_SIZE + (grade * dotSizeDistance);

        return dotSize;
    }

    public String getDescription() {
        return "ProportionalCircleMapRenderer - visualizes a temporal animation of proportional circles into a map";
    }
    
    public String toString() {
        return getDescription();
    }

    public String getServiceType() {
        return null;
    }

    public String[] getSupportedVersions() {
        return null;
    }

}