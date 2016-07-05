package fi.vm.sade.viestintapalvelu.util;

import org.junit.Assert;
import org.junit.Test;

import static fi.vm.sade.viestintapalvelu.util.DateUtil.*;

public class DateUtilTest {

    @Test
    public void testDateFormatsFi() {
        String pvm = "19.7.2016";
        String aika = "15.00";
        Assert.assertEquals("19.7.2016 klo 15.00", palautusTimestampFi(pvm,aika));
    }

    @Test
    public void testDateFormatsSv() {
        String pvm = "19.7.2016";
        String aika = "15.00";
        Assert.assertEquals("19.7.2016 kl. 15.00", palautusTimestampSv(pvm,aika));
    }

    @Test
    public void testDateFormatsEn() {
        String pvm = "19.7.2016";
        String aika = "15.00";
        Assert.assertEquals("19 July 2016 at 3 PM", palautusTimestampEn(pvm,aika));
    }
}
