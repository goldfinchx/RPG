package ru.artorium.rpg.utils.time;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String getTimeToString(TimeUnit timeUnit, long timeValue) {
        StringBuilder stringBuilder = new StringBuilder();
        int[] nums = splitToComponentTimes(new BigDecimal(timeUnit.toSeconds(timeValue)));

        if (nums[0] != 0)
            stringBuilder.append(nums[0]);

        if (nums[1] != 0) {
            if (nums[0] != 0) {
                stringBuilder.append(":");

                if (nums[1] < 10)
                    stringBuilder.append(0);
            }

            stringBuilder.append(nums[1]);
        }

        return stringBuilder.toString();
    }

    private static int[] splitToComponentTimes(BigDecimal biggy) {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return new int[]{mins , secs};
    }

}
