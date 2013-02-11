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
