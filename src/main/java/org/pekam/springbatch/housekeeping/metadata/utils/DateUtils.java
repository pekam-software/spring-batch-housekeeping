package org.pekam.springbatch.housekeeping.metadata.utils;

import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date addDays(Date date, int amount) {
        Assert.notNull(date, "Date must not be null.");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, amount);
        return c.getTime();
    }

}
