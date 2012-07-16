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

package org.n52.oxf.ui.swing.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.n52.oxf.context.ContextHelper;
import org.n52.oxf.ui.swing.AnimatedMapCanvas;
import org.n52.oxf.ui.swing.BoundingBoxPanel;
import org.n52.oxf.ui.swing.LegendDialog;
import org.n52.oxf.ui.swing.MyGridBagLayout;
import org.n52.oxf.ui.swing.animation.AnimationPanel;
import org.n52.oxf.ui.swing.menu.ConnectServiceMenu;
import org.n52.oxf.ui.swing.menu.Menu;
import org.n52.oxf.ui.swing.menu.OptionsMenu;
import org.n52.oxf.ui.swing.menu.ProjectMenu;
import org.n52.oxf.ui.swing.menu.SelectionMenu;
import org.n52.oxf.ui.swing.tool.FeatureToolBar;
import org.n52.oxf.ui.swing.tool.LayerToolBar;
import org.n52.oxf.ui.swing.tool.ZoomPanToolBar;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OXClient extends JFrame {

    private static final long serialVersionUID = -7535286141922675210L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OXClient.class);

    protected ContextHelper oxf;

    protected JSplitPane splitPane;

    protected JPanel rightPanel;
    protected AnimatedMapCanvas map;

    protected BoundingBoxPanel bBoxPanel;

    protected JPanel topPanel;
    protected ZoomPanToolBar zpToolBar;
    protected LayerToolBar layerToolBar;
    protected FeatureToolBar featureToolBar;

    protected JPanel leftPanel;
    protected JScrollPane treeView;
    protected ContentTree tree;

    protected JMenuBar menuBar;
    protected ProjectMenu projectMenu;
    protected ConnectServiceMenu connectMenu;
    protected SelectionMenu selectionMenu;
    protected OptionsMenu optionsMenu;

    public OXClient() {
        super("52North");

        oxf = new ContextHelper();

        rightPanel = new JPanel();
        map = new AnimatedMapCanvas(oxf.getImageBuilder());

        bBoxPanel = new BoundingBoxPanel(map);

        leftPanel = new JPanel();
        tree = new ContentTree(map);
        treeView = new JScrollPane(tree);

        topPanel = new JPanel();
        zpToolBar = new ZoomPanToolBar(this, SwingConstants.HORIZONTAL, map, tree);
        layerToolBar = new LayerToolBar(this, SwingConstants.HORIZONTAL, map, tree);
        featureToolBar = new FeatureToolBar(this, SwingConstants.HORIZONTAL, map, tree);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);

        menuBar = new JMenuBar();
        projectMenu = new ProjectMenu(this, map, tree);
        connectMenu = new ConnectServiceMenu(this, map, tree);
        selectionMenu = new SelectionMenu(this, map, tree);

        LegendDialog legendDialog = new LegendDialog(this, tree);
        legendDialog.setVisible(false);

        optionsMenu = new OptionsMenu(this, map, tree, legendDialog);

        initGUI();
    }

    protected void initGUI() {
        setSize(1000, 580);
        setLocation(100, 100);
        setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(layerToolBar);
        topPanel.add(zpToolBar);
        topPanel.add(featureToolBar);

        // treeView.setPreferredSize(new Dimension(200,200));

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(treeView);
        leftPanel.add(bBoxPanel);

        MyGridBagLayout rightPanelLayout = new MyGridBagLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.addComponent(map,
                                      0,
                                      0,
                                      1,
                                      1,
                                      100,
                                      100,
                                      GridBagConstraints.CENTER,
                                      GridBagConstraints.BOTH,
                                      new Insets(0, 0, 0, 0));
        rightPanelLayout.addComponent(new AnimationPanel(map),
                                      0,
                                      1,
                                      1,
                                      1,
                                      0,
                                      0,
                                      GridBagConstraints.SOUTH,
                                      GridBagConstraints.HORIZONTAL,
                                      new Insets(0, 0, 0, 0));

        getContentPane().add(topPanel, BorderLayout.PAGE_START);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        Dimension minimumSize = new Dimension(380, 100);
        leftPanel.setMinimumSize(minimumSize);
        rightPanel.setMinimumSize(minimumSize);

        // menu adden:
        addMenu(projectMenu);
        addMenu(connectMenu);
        addMenu(selectionMenu);
        addMenu(optionsMenu);
        setJMenuBar(menuBar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GUI initialized");
        }
    }

    protected void addMenu(Menu oxfMenu) {
        menuBar.add(oxfMenu);
    }

    public static void main(String[] args) {

        new OXClient().setVisible(true);

    }
}