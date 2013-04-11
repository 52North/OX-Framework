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
package org.n52.oxf.request;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.request.MultiValue;


public class MultiValueTest {
    
    private MultiValue multiValue;

    @Before public void
    setUp()
    {
        multiValue = new MultiValue();
    }
    
    @Test public void 
    shouldBeCreatable() 
    {
        assertNotNull(multiValue);
    }
    
    @Test public void 
    shouldAllowNullsAsInput() 
    {
        assertThat(multiValue.addValue(null), is(true));
    }
    
    @Test public void 
    shouldHoldEmptyStringInsteadOfNulls() 
    {
        multiValue.addValue(null);
        assertThat(multiValue.contains(""), is(true));
    }
    
    @Test() public void 
    shouldAllowEmptyStringValues() 
    {
        assertThat(multiValue.addValue(""), is(true));
    }
    
    @Test public void 
    shouldIndicateThatValuesArePresentAfterValuesWhereAdded()
    {
        multiValue.addValue("1.0.0");
        assertThat(multiValue.hasValues(), is(true));
    }
    
    @Test public void 
    shouldTellTheRightAmountOfAddedValues() 
    {
        multiValue.addValue("1.0.0");
        multiValue.addValue("2.0.0");
        assertThat("Size does not match.", multiValue.size(), is(2));
    }
    
    @Test public void 
    shouldReturnFalseWhenAddingDoesNotChangeInstance() 
    {
        assertThat("No change indicated!", multiValue.addValue("1.0.0"), is(true));
        assertThat("Change indicated!", multiValue.addValue("1.0.0"), is(false));
    }

    @Test public void 
    shouldReturnEmptyCollectionWhenNoValuesWereAdded()
    {
        assertThat(multiValue.getValues(), isA(Collection.class));
    }
    
    @Test public void
    shouldReturnTrueIfAddedValuesAreContained()
    {
        multiValue.addValue("1.0.0");
        assertThat(multiValue.contains("1.0.0"), is(true));
    }
    
    @Test public void
    shouldReturnFalseIfAskedForNullValue()
    {
        multiValue.addValue("1.0.0");
        assertThat(multiValue.contains(null), is(false));
    }
    
    @Test public void 
    shouldReturnFalseIfRemovingNotAddedElement()
    {
        multiValue.addValue("1.0.0");
        assertThat(multiValue.removeValue("2.0.0"), is(false));
    }
    
    @Test public void 
    shouldReturnTrueIfRemovingAddedElement()
    {
        multiValue.addValue("1.0.0");
        assertThat(multiValue.removeValue("1.0.0"), is(true));
    }
    
    @Test public void 
    shouldBeEmptyAfterRemovingTheLastElement()
    {
        multiValue.addValue("1.0.0");
        multiValue.addValue("2.0.0");
        multiValue.removeValue("1.0.0");
        multiValue.removeValue("2.0.0");
        assertThat(multiValue.size(), is(0));
        assertThat(multiValue.hasValues(), is(false));
    }
    
    @Test public void 
    shouldBeEmptyAfterRemovingAllAddedElements()
    {
        multiValue.addValue("1.0.0");
        multiValue.addValue("2.0.0");
        multiValue.removeAll();
        assertThat(multiValue.hasValues(), is(false));
    }
    

}
