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

package org.n52.oxf.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

class SyntaxHighlighter extends JFrame implements ActionListener{

  JTextPane pane;
  MutableAttributeSet tagAttributes, elementAttributes, 
    characterAttributes, cdataAttributes;
  Pattern partPattern, namePattern, attributePattern;
  Matcher partMatcher, nameMatcher, attributeMatcher;

  public static void main(String args[]) throws Exception {
    new SyntaxHighlighter();
  }

  SyntaxHighlighter() {
    super("SyntaxHighlighter");
    pane = new JTextPane();
    JScrollPane scrollPane = new JScrollPane(pane);
    getContentPane().add(scrollPane, BorderLayout.CENTER);
    initAttributeSets();
    initPatterns();
    initToolbar();
    
    setBounds(10,10,500,600);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
     Initialisiert AttributeSets f�r die unterschiedlichen
     syntaktischen Bestandteile der XML-Datei.
   */
  private void initAttributeSets() {
    tagAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(tagAttributes, Color.blue);

    cdataAttributes = new SimpleAttributeSet();
    StyleConstants.setFontFamily(cdataAttributes, "Courier");

    characterAttributes = new SimpleAttributeSet();
    StyleConstants.setBold(characterAttributes, true);

    elementAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(elementAttributes, new Color(153,0,0));
  }

  private void initPatterns() {

    // REX/Javascript 1.0 
    // Robert D. Cameron "REX: XML Shallow Parsing with Regular Expressions",
    // Technical Report TR 1998-17, School of Computing Science, Simon Fraser 
    // University, November, 1998.
    // Copyright (c) 1998, Robert D. Cameron. 
    // The following code may be freely used and distributed provided that
    // this copyright and citation notice remains intact and that modifications
    // or additions are clearly identified.
    
    // Angepa�t an java.util.regex.

    String TextSE = "[^<]+";
    String UntilHyphen = "[^-]*-";
    String Until2Hyphens = UntilHyphen + "([^-]" + UntilHyphen + ")*-";
    String CommentCE = Until2Hyphens + ">?";
    String UntilRSBs = "[^]]*]([^]]+])*]+";
    String CDATA_CE = UntilRSBs + "([^]>]" + UntilRSBs + ")*>";
    String S = "[ \\n\\t\\r]+";
    String NameStrt = "[A-Za-z_:]|[^\\x00-\\x7F]";
    String NameChar = "[A-Za-z0-9_:.-]|[^\\x00-\\x7F]";
    String Name = "(" + NameStrt + ")(" + NameChar + ")*";
    String QuoteSE = "\"[^\"]" + "*" + "\"" + "|'[^']*'";
    String DT_IdentSE = S + Name + "(" + S + "(" + Name + "|" + QuoteSE + "))*";
    String MarkupDeclCE = "([^]\"'><]+|" + QuoteSE + ")*>";
    String S1 = "[\\n\\r\\t ]";
    String UntilQMs = "[^?]*\\?+";
    String PI_Tail = "\\?>|" + S1 + UntilQMs + "([^>?]" + UntilQMs + ")*>";
    String DT_ItemSE = "<(!(--" + Until2Hyphens + ">|[^-]" + MarkupDeclCE + ")|\\?" + Name + "(" +
      PI_Tail + "))|%" + Name + ";|" + S;
    String DocTypeCE = DT_IdentSE + "(" + S + ")?(\\[(" + DT_ItemSE + ")*](" + S + ")?)?>?";
    String DeclCE = "--(" + CommentCE + ")?|\\[CDATA\\[(" + CDATA_CE + ")?|DOCTYPE(" + DocTypeCE +
      ")?";
    String PI_CE = Name + "(" + PI_Tail + ")?";
    String EndTagCE = Name + "(" + S + ")?>?";
    String AttValSE = "\"[^<\"]" + "*" + "\"" + "|'[^<']*'";
    String ElemTagCE = Name + "(" + S + Name + "(" + S + ")?=(" + S + ")?(" + AttValSE + "))*(" + S
      + ")?/?>?";
    String MarkupSPE = "<(!(" + DeclCE + ")?|\\?(" + PI_CE + ")?|/(" + EndTagCE + ")?|(" +
      ElemTagCE + ")?)";
    String XML_SPE = TextSE + "|" + MarkupSPE;
    
    partPattern = Pattern.compile(XML_SPE);
    namePattern = Pattern.compile(Name);
    attributePattern = Pattern.compile(AttValSE);
  }


  /**
     Packt einen Knopf in einen JToolBar, mit dem man eine Datei laden kann.
   */
  private void initToolbar() {
    JToolBar toolBar = new JToolBar();
    JButton highlightButton = new JButton("Datei laden...");
    highlightButton.addActionListener(this);
    toolBar.add(highlightButton);
    getContentPane().add(toolBar, BorderLayout.NORTH);
  }


  /**
     L�dt eine XML-Datei in die JTextPane und 
     nimmt das Syntax-Highlighting f�r alle Parts vor.
   */
  public void actionPerformed(ActionEvent e) {
    loadFile();
    String text = pane.getText();
    nameMatcher = namePattern.matcher(text);
    partMatcher = partPattern.matcher(text);
    attributeMatcher = attributePattern.matcher(text);

    while (partMatcher.find()) {
      formatierePart(partMatcher);
    }
  }

  /**
     �ffnet einen JFileChooser und l�dt die gegebenenfalls
     ausgew�hlte XML-Datei in die JTextPane.
   */

  private void loadFile() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("W�hle eine XML-Datei");

    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
    chooser.setMultiSelectionEnabled(false );
    chooser.setFileFilter(new XmlFilter());

    int buttonCode = chooser.showOpenDialog(this); 
    if(buttonCode == JFileChooser.APPROVE_OPTION) {
      try {
	BufferedReader br = new BufferedReader(new FileReader( chooser.getSelectedFile()));
	String line;
	StringBuffer sb=new StringBuffer((int)chooser.getSelectedFile().length());
	while((line = br.readLine()) != null)
	  sb.append(line+"\n");
	br.close();
	pane.setText(sb.toString());
      }
      catch(Exception e) {
	JOptionPane.showMessageDialog(this, e.getMessage());
      }
    }
  }

  private void formatierePart(Matcher partMatcher) {
    String part = partMatcher.group();
    if (!part.startsWith("<")) {
      applyAttributeSet(partMatcher, characterAttributes);
    }
    else {
      applyAttributeSet(partMatcher, tagAttributes);
      if (!part.startsWith("<?") && !part.startsWith("<!")) {
	formatiereElement(partMatcher);
      }
      if (part.startsWith("<![CDATA[") && part.endsWith("]]>")) {
	applyAttributeSet(partMatcher, cdataAttributes, 9, 12);
      }
    }
  }
    
  /** 
      Formatiert Namen und Attribute in einem XML-Element.
   */

  private void formatiereElement(Matcher partMatcher) {
    // Alle Namen innerhalb des Elements werden gefunden und formatiert
    nameMatcher.find(partMatcher.start());
    do {
      applyAttributeSet(nameMatcher, elementAttributes);
      if (! nameMatcher.find()) break;
    }
    while(nameMatcher.end() < partMatcher.end());
    
    // Alle Attribute innerhalb des Elements werden gefunden und formatiert
    if (!attributeMatcher.find(partMatcher.start())) return;
    do {
      applyAttributeSet(attributeMatcher, characterAttributes, 1, 1);
      if (! attributeMatcher.find()) break;
    }
    while(attributeMatcher.end() < partMatcher.end());
  }

  /** 
      Formatiert den Text der aktuellen Group
      des Matchers mit dem AttributeSet.
  */
  private void applyAttributeSet(Matcher m, AttributeSet attributeSet) {
    applyAttributeSet(m, attributeSet, 0, 0);
  }

  /** 
      Formatiert den Text der aktuellen Group (abz�glich bestimmter R�nder links und rechts)
      des Matchers mit dem AttributeSet.
  */
  private void applyAttributeSet(Matcher m, AttributeSet attributeSet, int additionalOffset, int lengthReduction) {
    int offset = m.start();
    int length = m.group().length();
    pane.getStyledDocument().
      setCharacterAttributes(offset+additionalOffset, 
			     length-lengthReduction, attributeSet, true);
  }
}


class XmlFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
      return f.getName().endsWith(".xml");
    }
    public String getDescription() {
        return "XML-Dateien";
    }
}
