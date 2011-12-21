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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.valueDomains.time.TimePeriod;

import com.topografix.gpx.x1.x0.GpxDocument.Gpx.Trk.Trkseg.Trkpt;

/**
 * 
 * Class holds all properties for a mobile sensor szenario of a bike tour from
 * Hamburg to Lissabon. Actual positions are used, measurement values are created randomly.
 * The "active" and "mobile" states of the sensor are changed randomly as well.
 * 
 * The positions are read from a gpx file (downloaded from bikemap.net).
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class HamburgLissabonFeederProperties extends FeedProperties {

    public HamburgLissabonFeederProperties() {
        super();

        gpxFile = "D:\\Bikemap.net-Hamburg-Lisabon.gpx";
        this.sleepTime = 8000;
        sensorID = "urn:ogc:object:feature:sensor:ifgi:ifgi-sensor-302";

        String monthDay = "02-15";
        this.timePeriod = (TimePeriod) TimeFactory.createTime("2009-" + monthDay + "T17:00:00+01:00/" + "2009-"
                + monthDay + "T18:00:00+01:00");

        this.pointCount = 16800;

        int numberOfWantedPoints = 600;

        init();

        int listSize = getTrackPoints().size();

        int stepSize = listSize / numberOfWantedPoints;

        List<Trkpt> tempPoints = new ArrayList<Trkpt>(numberOfWantedPoints);

        for (int i = 0; i < listSize; i = i + stepSize) {
            tempPoints.add(getTrackPoints().get(i));
        }

        setTrackPoints(tempPoints);
        this.pointCount = numberOfWantedPoints;

        ArrayList<Boolean> allTrue = new ArrayList<Boolean>();
        for (int i = 0; i < this.pointCount; i++) {
            allTrue.add(Boolean.TRUE);
        }
        this.activeList.addAll(allTrue);
        this.mobileList.addAll(allTrue);

        Random rand = new Random();
        int inactive = 0;
        int immobile = 0;
        for (int i = 0; i < this.activeList.size(); i++) {
            if (rand.nextDouble() < 0.3) {
                this.activeList.set(i, Boolean.FALSE);
                inactive++;
            }
            if (rand.nextDouble() < 0.05) {
                this.mobileList.set(i, Boolean.FALSE);
                immobile++;
            }
        }

        System.out.println("added random values... inactive=" + inactive + ", immobile=" + immobile);

        this.mobileList.set(0, Boolean.FALSE);
        this.mobileList.set(this.pointCount - 1, Boolean.FALSE);

    }

    public static void main(String[] args) {
        new HamburgLissabonFeederProperties();
    }

}
