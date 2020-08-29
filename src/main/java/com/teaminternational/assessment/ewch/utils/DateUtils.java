package com.teaminternational.assessment.ewch.utils;

import java.time.LocalDate;
import java.time.Period;

public final class DateUtils {

    public static int getYearsInstantDifferenceFromNow(LocalDate localDate) {
        LocalDate currentDate = LocalDate.now();
        Period yearsDifference = Period.between(localDate, currentDate);
        return yearsDifference.getYears();
    }

    public static boolean isAbleToWork(LocalDate localDate) {
        return getYearsInstantDifferenceFromNow(localDate) >= Constants.MINIMUM_AGE_TO_WORK;
    }
}
