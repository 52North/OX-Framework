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
package org.n52.oxf.ui.swing.wms;

/**
 * A convenience class for storing information about layer status for
 * display
 * @author sroos
 *
 */
public class LayerStatus {

	private String layerName;
	private String layerStatus;
	
	public static String STATUS_NO_REQUEST = "No request currently active";
	public static String STATUS_REQUEST_INITIATED = "Request Initiated";
	public static String STATUS_NEW_LAYER_ADDED = "Adding layer initiated - querying service";
	public static String STATUS_OPERATION_RESULT_RECEIVED = "Service response received - intializing display";
	public static String STATUS_LAYER_VISUALIZATION_READY = "Visualization ready...";
	public static String STATUS_READY_TO_OVERLAY ="Done";
	
	public LayerStatus(String name, String status) {
		this.layerName = name;
		this.layerStatus = status;
	}

	public String getLayerName() {
		return layerName;
	}

	public String getLayerStatus() {
		return layerStatus;
	}
	
	public void setLayerStatus(String status) {
		this.layerStatus = status;
	}
	
}