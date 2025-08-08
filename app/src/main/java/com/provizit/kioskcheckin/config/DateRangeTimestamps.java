package com.provizit.kioskcheckin.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateRangeTimestamps {

    // Convert date + time to timestamp in seconds
    public static long convertToTimestamp(String dateStr, String timeStr) {
        try {
            String dateTimeStr = dateStr + " " + timeStr;
            SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Use UTC or change to your timezone
            Date date = sdf.parse(dateTimeStr);
            if (date != null) {
                return date.getTime() / 1000; // convert to seconds
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Generate [timestamp1, timestamp2, ...] format string
    public static String getTimestampArrayString(String fromDate, String fromTime, String toDate, String toTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Or use device timezone

        try {
            Date startDate = sdf.parse(fromDate);
            Date endDate = sdf.parse(toDate);

            if (startDate == null || endDate == null || startDate.after(endDate)) return "[]";

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            List<String> timestamps = new ArrayList<>();

            while (!calendar.getTime().after(endDate)) {
                String currentDate = sdf.format(calendar.getTime());
                long timestamp = convertToTimestamp(currentDate, fromTime);
                timestamps.add(String.valueOf(timestamp));
                calendar.add(Calendar.DATE, 1);
            }

            return "[" + String.join(",", timestamps) + "]";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "[]";
    }
}
