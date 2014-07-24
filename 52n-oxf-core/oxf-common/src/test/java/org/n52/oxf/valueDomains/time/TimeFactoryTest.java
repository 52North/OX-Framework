package org.n52.oxf.valueDomains.time;

import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;
import org.joda.time.DateTime;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.n52.oxf.ows.capabilities.ITime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Henning Bredel <h.bredel@52north.org>
 */
public class TimeFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeFactoryTest.class);

    @Test
    public void
            createTimeValuesBeforeAtAndAfterDaylightSavingTimeSwitch() {

        String beforeSwitch = "2007-10-28T02:55:00.000+02:00";
        String atSwitch = "2007-10-28T02:00:00.000+01:00";
        String afterSwitch = "2007-10-28T02:05:00.000+01:00";

        ITime oxfBeforeSwitch = TimeFactory.createTime(beforeSwitch);
        ITime oxfAtSwitch = TimeFactory.createTime(atSwitch);
        ITime oxfAfterSwitch = TimeFactory.createTime(afterSwitch);

//        LOGGER.debug("OX-F before: {}", oxfBeforeSwitch.toISO8601Format());
//        LOGGER.debug("OX-F at: {}", oxfAtSwitch.toISO8601Format());
//        LOGGER.debug("OX-F after {}:", oxfAfterSwitch.toISO8601Format());

        DateTime jodaBeforeSwitch = DateTime.parse(beforeSwitch);
        DateTime jodaAtSwitch = DateTime.parse(atSwitch);
        DateTime jodaAfterSwitch = DateTime.parse(afterSwitch);

        assertThat(jodaBeforeSwitch, is(DateTime.parse(oxfBeforeSwitch.toISO8601Format())));
        assertThat(jodaAtSwitch, is(DateTime.parse(oxfAtSwitch.toISO8601Format())));
        assertThat(jodaAfterSwitch, is(DateTime.parse(oxfAfterSwitch.toISO8601Format())));



    }
}
