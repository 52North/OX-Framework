/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.request;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


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
