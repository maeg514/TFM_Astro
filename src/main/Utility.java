package main;

import java.util.List;

public class Utility {

    /**
     * Converts Gregorian day to Julian day.
     *
     * @param year Gregorian year.
     * @param month Gregorian month.
     * @param day Gregorian day.
     * @param hour Gregorian hour.
     * @param minute Gregorian minute.
     * @param julian If true converts to Julian day.
     * @return Julian day.
     */
    public static double dateToJulianDay(int year, int month, int day, int hour, int minute, boolean julian) {
        if (month < 3) {
            year = year - 1;
            month = month + 12;
        }
        int a = year / 100;
        int b = 0;
        double c = (hour + minute / 60.0) / 24.0;
        if (!julian) b = 2 - a + a / 4;
        return (int) (365.25 * (year + 4716)) + (int) (30.6001 * (month + 1)) + day + b - 1524.5 + c;
    }

    public static double[] vectorObserver(double jd_ut, double ttMinusUT, double obsLon, double obsLat, double obsAlt) {
        double lst = Utility.localApparentSiderealTime(jd_ut, ttMinusUT, obsLon);
        double geocLat = (obsLat - .1925 * Math.sin(2 * obsLat) * main.Constants.DEG_TO_RAD);
        double geocR = 1.0 - Math.pow(Math.sin(obsLat), 2) / 298.257;
        double radiusAU = (geocR * Constants.EARTH_RADIUS + obsAlt * 0.001 / Constants.AU);
        double cosLat = Math.cos(geocLat);
        double[] correction = new double[]{
                radiusAU * cosLat * Math.cos(lst),
                radiusAU * cosLat * Math.sin(lst),
                radiusAU * Math.sin(geocLat)};
        return correction;
    }


    /**
     * Computes nutation in longitude and obliquity.
     *
     * @param jd Julian day in UT.
     * @return Nutation angles in radians.
     */
    public static double[] nutation(double jd, double ttMinusUT) {
        double t = (jd + ttMinusUT / Constants.SECONDS_PER_DAY - Constants.J2000) / main.Constants.JULIAN_DAYS_PER_CENTURY;

        // Mean elongation of Moon
        double D = (297.85036 + 445267.111480 * t - 0.0019142 * t * t + t *
                t * t / 189474.) * Constants.DEG_TO_RAD;
        // Mean anomaly of Sun (Earth)
        double M = (357.52772 + 35999.050340 * t - 0.0001603 * t * t - t *
                t * t / 300000.) * Constants.DEG_TO_RAD;
        // Mean anomaly of Moon
        double Mp = (134.96298 + 477198.867398 * t + 0.0086972 * t * t + t
                * t * t / 56250.) * Constants.DEG_TO_RAD;
        // Moon's argument of latitude
        double F = (93.27191 + 483202.017538 * t - 0.0036825 * t * t + t *
                t * t / 327270.) * Constants.DEG_TO_RAD;
        // Mean longitude of the ascending node of the Moon
        double OM = (125.04452 - 1934.136261 * t + 0.0020708 * t * t + t *
                t * t / 450000.) * Constants.DEG_TO_RAD;

        // Compute approximate nutation (see Meeus, page 133, terms up to 0.02"). Accuracy better than 0.08", 0.05" respect IAU1980 nutation
        double a2 = 2.0 * (F + OM - D), a3 = 2.0 * (F + OM);
        double nutLon = (-(17.1996 + 0.01742 * t) * Math.sin(OM) - (1.3187
                + 0.00016 * t) * Math.sin(a2) - (.2274 - 0.00002 * t) * Math.sin(a3)) +
                (0.2062 + 0.00002 * t) * Math.sin(2 * OM) + (0.1426 - 0.00034 *
                t) * Math.sin(M) + (0.0712 + 0.00001 * t) * Math.sin(Mp) +
                (-0.0517 + 0.00012 * t) * Math.sin(a2 + M) - (0.0386 - 0.00004
                * t) * Math.sin(2 * F + OM) - 0.0301 * Math.sin(2 * (F + OM) + Mp) +
                (0.0217 - 0.00005 * t) * Math.sin(a2 - M);
        double nutObl = ((9.2025 + .00089 * t) * Math.cos(OM) + (0.5736 -
                0.00031 * t) * Math.cos(a2) + (.0977 - 0.00005 * t) * Math.cos(a3)) +
                (-0.0895 + 0.00005 * t) * Math.cos(2 * OM) + (0.0054 - 0.00001
                * t) * Math.cos(M) - 0.00007 * Math.cos(Mp) +
                (0.0224 - 0.00006 * t) * Math.cos(a2 + M) + 0.0200 *
                Math.cos(2 * F + OM);

        return new double[]{nutLon * Constants.ARCSEC_TO_RAD, nutObl *
                Constants.ARCSEC_TO_RAD};
    }

    /**
     * Calculates the Local Apparent Sidereal Time (LAST) for a terrestrial observer.
     *
     * @param jd_ut Julian in Universal Time (UT).
     * @param ttMinusUT difference between Terrestrial Time and Universal Time.
     * @param obsLon geographical longitude.
     * @return LAST in radians and normalized.
     */
    public static double localApparentSiderealTime(double jd_ut, double ttMinusUT, double obsLon) {
        // Obtain local apparent sidereal time
        double jd0 = Math.floor(jd_ut - 0.5) + 0.5; // previous midnight
        double t0 = (jd0 - Constants.J2000) / Constants.JULIAN_DAYS_PER_CENTURY; // centuries from previous midnight
        double secs = (jd_ut - jd0) * main.Constants.SECONDS_PER_DAY;
        double gmst = (((((-6.2e-6 * t0) + 9.3104e-2) * t0) + 8640184.812866) * t0) + 24110.54841;
        double msday = 1.0 + (((((-1.86e-5 * t0) + 0.186208) * t0) + 8640184.812866) / (Constants.SECONDS_PER_DAY *
                Constants.JULIAN_DAYS_PER_CENTURY));
        gmst = (gmst + msday * secs) * 15.0 * Constants.ARCSEC_TO_RAD;

        // IAU 1994 resolution C7 added two terms (dependent on the mean ascending node of the lunar orbit omega)
        // to the equation of equinoxes, taking effect since 1997-02-27
        double dt = (jd_ut + ttMinusUT / Constants.SECONDS_PER_DAY - Constants.J2000) / main.Constants.JULIAN_DAYS_PER_CENTURY;
        double omega = (125.04452 - 1934.136261 * dt + 0.0020708 * dt * dt + (dt * dt * dt) / 450000) * Constants.DEG_TO_RAD;

        double eps0 = 84381.448;
        double[] pol = {-468093., -155., 199925., -5138., -24967., -3905., 712., 2787., 579., 245.};
        double meanObliquity = 0;
        for (int i = 0; i < pol.length; i++) {
            meanObliquity += pol[i] * 0.01 * Math.pow(dt * 0.01, i + 1);
        }
        meanObliquity = (meanObliquity + eps0) * Constants.ARCSEC_TO_RAD;

        double nutLon = Utility.nutation(jd_ut, ttMinusUT)[0];
        double last = (
                gmst + obsLon + nutLon * Math.cos(meanObliquity)
                        + 0.00264 * Math.sin(omega) * Constants.ARCSEC_TO_RAD
                        + 0.000063 * Math.sin(2 * omega) * Constants.ARCSEC_TO_RAD
        );

        last = last % (2 * Math.PI);
        return last;
    }

    /**
     * Calculates the norm of the position vector, it is used to calculate the distance between two bodies.
     *
     * @param bodyVector Vector whose length it's calculated.
     * @return Magnitude of the vector.
     */
    public static double vectorLength(double[] bodyVector) {
        double r_2 = 0;
        for (int i = 0; i < bodyVector.length; i++) {
            r_2 += bodyVector[i] * bodyVector[i];
        }
        return Math.sqrt(r_2);
    }


    public static String matrixToString(double[][] matriz) {
        StringBuilder sb = new StringBuilder();

        for (double[] fila : matriz) {
            for (double valor : fila) {
                sb.append(String.format("%10.10f", valor)).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


}
