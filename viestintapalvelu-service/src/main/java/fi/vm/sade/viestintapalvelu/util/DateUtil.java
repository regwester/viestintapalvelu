package fi.vm.sade.viestintapalvelu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {


    public static String palautusTimestampFi(String pvm, String aika) {
        Date timestamp = palautusTimestamp(pvm, aika);
        if(timestamp == null) {
            return null;
        }
        return new SimpleDateFormat("d.M.yyyy 'klo' H.mm").format(timestamp);
    }
    public static String palautusTimestampSv(String pvm, String aika) {
        Date timestamp = palautusTimestamp(pvm, aika);
        if(timestamp == null) {
            return null;
        }
        return new SimpleDateFormat("d.M.yyyy 'kl.' H.mm").format(timestamp);
    }
    public static String palautusTimestampEn(String pvm, String aika) {
        Date timestamp = palautusTimestamp(pvm, aika);
        if(timestamp == null) {
            return null;
        }
        return new SimpleDateFormat("d MMMM yyyy 'at' h a").format(timestamp);
    }

    private static Date palautusTimestamp(String pvm, String aika) {
        if(pvm == null || aika == null) {
            return null;
        }
        try {
            Date timestamp = new SimpleDateFormat("d.M.yyyy hh.mm").parse(pvm + " " + aika);
            return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
