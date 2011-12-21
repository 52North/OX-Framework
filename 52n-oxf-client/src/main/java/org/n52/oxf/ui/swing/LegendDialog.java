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

package org.n52.oxf.ui.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.render.*;
import org.n52.oxf.ui.swing.tree.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LegendDialog extends JDialog implements TreeSelectionListener {

    private ContentTree tree;

    private JScrollPane scrollPane;
    private LegendPanel legendPanel;

    private Image currentLegend = null;

    private int WINDOW_WIDTH = 350;
    private int WINDOW_HEIGHT = 500;

    /**
     * 
     */
    public LegendDialog(JFrame owner, ContentTree contentTree) {
        super(owner);
        tree = contentTree;
        tree.addTreeSelectionListener(this);

        scrollPane = new JScrollPane();
        legendPanel = new LegendPanel(this);

        init(owner);
    }

    private void init(JFrame owner) {
        setTitle("Legend");
        setBounds(910, 100, WINDOW_WIDTH, WINDOW_HEIGHT);

        scrollPane.setViewportView(legendPanel);

        getContentPane().add(scrollPane);
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node != null) {
            if (node instanceof LayerNode) {
                LayerNode layerNode = (LayerNode) node;

                IContextLayer layer = layerNode.getLayer();

                IVisualization vis = layer.getLayerVisualization();

                if (vis != null) {

                    if (vis.getLegend() != null) {
                        currentLegend = vis.getLegend();
                    }
                    else {
                        currentLegend = null;
                    }

                    legendPanel.repaint();
                }
            }
        }
    }

    class LegendPanel extends JPanel implements Scrollable {

        private int maxUnitIncrement = 1;
        private LegendDialog owner;
        
        public LegendPanel(LegendDialog owner) {
            super();
            this.owner = owner;
        }
        
        public void paint(Graphics g) {
            super.paint(g);

            // draw white background:
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            if (currentLegend != null) {
                setSize(getPreferredSize());
                
                // draw image:
                g.drawImage(currentLegend, 0, 0, Color.WHITE, this);
            }
            else {
                g.setColor(Color.BLACK);
                g.drawString("No legend available.", 15, 30);
            }
        }

        public Dimension getPreferredSize() {
            if (currentLegend == null) {
                return new Dimension(owner.getWidth(), owner.getHeight());
            }
            else {
                return new Dimension(currentLegend.getWidth(this), currentLegend.getHeight(this));
            }
        }

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            // Get the current position.
            int currentPosition = 0;
            if (orientation == SwingConstants.HORIZONTAL) {
                currentPosition = visibleRect.x;
            }
            else {
                currentPosition = visibleRect.y;
            }

            // Return the number of pixels between currentPosition
            // and the nearest tick mark in the indicated direction.
            if (direction < 0) {
                int newPosition = currentPosition - (currentPosition / maxUnitIncrement)
                        * maxUnitIncrement;
                return (newPosition == 0) ? maxUnitIncrement : newPosition;
            }
            else {
                return ( (currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement
                        - currentPosition;
            }
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (orientation == SwingConstants.HORIZONTAL) {
                return visibleRect.width - maxUnitIncrement;
            }
            else {
                return visibleRect.height - maxUnitIncrement;
            }
        }

        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        public void setMaxUnitIncrement(int pixels) {
            maxUnitIncrement = pixels;
        }
    }
}