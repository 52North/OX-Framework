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
package org.n52.oxf.ui.swing.ses;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;

public class SesInfoFrame {
	public static void showSesInfoFrame(OXFFeatureCollection collection) {
		List<OXFFeature> list = collection.toList();
		
		JFrame frame = new JFrame("Selected Features");
		String headers[] = { "FOI_ID", "X_Position", "Y_Position", "SamplingTime", "ObservedProperty", "VALUE" };
		TableModel model = new SesTableModel(list.size(), headers);
		JTable table = new JTable(model);
		JLabel renderer = ((JLabel)table.getDefaultRenderer(Object.class));
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		for(int i=0; i<list.size(); i++){
			OXFFeature feature = list.get(i);
			
			// get values
			String foiID = (String)feature.getAttribute(SesLayerAdder.FOI_ID);
			double xCoordinates = feature.getGeometry().getCoordinate().x;
			double yCoordinates = feature.getGeometry().getCoordinate().y;
			String samplingTime = (String)feature.getAttribute(SesLayerAdder.SAMPLING_TIME);
			String observedProperty = (String)feature.getAttribute(SesLayerAdder.OBSERVED_PROPERTY);
			String resultValue = (String)feature.getAttribute(SesLayerAdder.RESULT_VALUE);
			
			// set values
			model.setValueAt(foiID, i, 0);
			model.setValueAt(xCoordinates, i, 1);
			model.setValueAt(yCoordinates, i, 2);
			model.setValueAt(samplingTime, i, 3);
			model.setValueAt(observedProperty, i, 4);
			model.setValueAt(resultValue, i, 5);
		}
		
		// center frame
		int xCoord = (Toolkit.getDefaultToolkit().getScreenSize().width/2)-350;
		int yCoord = (Toolkit.getDefaultToolkit().getScreenSize().height/2);
		
		JScrollPane scrollPane = new JScrollPane(table);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.setSize(700, 100);
		frame.setLocation(xCoord, yCoord);
		frame.setVisible(true);
	}
}

class SesTableModel extends AbstractTableModel {

	private Hashtable lookup;

	private final int rows;

	private final int columns;

	private final String headers[];

	public SesTableModel(int rows, String columnHeaders[]) {
		if ((rows < 0) || (columnHeaders == null)) {
			throw new IllegalArgumentException(
					"Invalid row count/columnHeaders");
		}
		this.rows = rows;
		this.columns = columnHeaders.length;
		headers = columnHeaders;
		lookup = new Hashtable();
	}

	public int getColumnCount() {
		return columns;
	}

	public int getRowCount() {
		return rows;
	}

	public String getColumnName(int column) {
		return headers[column];
	}

	public Object getValueAt(int row, int column) {
		return lookup.get(new Point(row, column));
	}

	public void setValueAt(Object value, int row, int column) {
		if ((rows < 0) || (columns < 0)) {
			throw new IllegalArgumentException("Invalid row/column setting");
		}
		if ((row < rows) && (column < columns)) {
			lookup.put(new Point(row, column), value);
		}
	}
}
