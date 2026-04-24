package main;

import java.util.List;

public class Utility {

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

    public static void arrayPrinter(List<Body> bodiesToPrint) {
        for (Body bodyLoop : bodiesToPrint) {
            double[] p = bodyLoop.getPositionInitial();
            double[] v = bodyLoop.getVelocityInitial();
            System.out.println(bodyLoop.getName());
            System.out.println(p[0] + " " + p[1] + " " + p[2] + " " + v[0] + " " + v[1] + " " + v[2]);
        }
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


    public static double meanObliquity(double dt){
        double eps0 = 84381.448;
        double[] pol = {-468093., -155., 199925., -5138., -24967., -3905., 712., 2787., 579., 245.};
        double meanObliquity = 0;
        for (int i = 0; i < pol.length; i++) {
            meanObliquity += pol[i] * 0.01 * Math.pow(dt * 0.01, i + 1);
        }
        meanObliquity = (meanObliquity + eps0) * Constants.ARCSEC_TO_RAD;

        return meanObliquity;
    }


    /**
     * Computes nutation in longitude and obliquity
     *
     * @param jd Julian day in UT
     * @return Nutation angles in radians
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

    public static double[] vectorObserver(double jd_ut, double ttMinusUT, double obsLon, double obsLat, double obsAlt) {
        double lst = localApparentSiderealTime(jd_ut, ttMinusUT, obsLon);
        double geocLat = (obsLat - .1925 * Math.sin(2 * obsLat) * Constants.DEG_TO_RAD);
        double geocR = 1.0 - Math.pow(Math.sin(obsLat), 2) / 298.257;
        double radiusAU = (geocR * Constants.EARTH_RADIUS + obsAlt * 0.001 / Constants.UA);
        double cosLat = Math.cos(geocLat);
        double[] correction = new double[]{
                radiusAU * cosLat * Math.cos(lst),
                radiusAU * cosLat * Math.sin(lst),
                radiusAU * Math.sin(geocLat)};

        double dt = (jd_ut + ttMinusUT / Constants.SECONDS_PER_DAY - Constants.J2000) / main.Constants.JULIAN_DAYS_PER_CENTURY;

        return precessionLaskarToOrFromJ2000(correction, dt, true);
    }

    public static double localApparentSiderealTime(double jd_ut, double ttMinusUT, double obsLon) {
        // Obtain local apparent sidereal time
        double jd0 = Math.floor(jd_ut - 0.5) + 0.5; // previous midnight
        double t0 = (jd0 - Constants.J2000) / Constants.JULIAN_DAYS_PER_CENTURY; // centuries from previous midnight
        double secs = (jd_ut - jd0) * main.Constants.SECONDS_PER_DAY;
        double gmst = (((((-6.2e-6 * t0) + 9.3104e-2) * t0) + 8640184.812866) * t0) + 24110.54841;
        double msday = 1.0 + (((((-1.86e-5 * t0) + 0.186208) * t0) + 8640184.812866) / (main.Constants.SECONDS_PER_DAY *
                main.Constants.JULIAN_DAYS_PER_CENTURY));
        gmst = (gmst + msday * secs) * 15.0 * Constants.ARCSEC_TO_RAD;

        // IAU 1994 resolution C7 added two terms (dependent on the mean ascending node of the lunar orbit omega)
        // to the equation of equinoxes, taking effect since 1997-02-27
        double dt = (jd_ut + ttMinusUT / Constants.SECONDS_PER_DAY - Constants.J2000) / main.Constants.JULIAN_DAYS_PER_CENTURY;
        double omega = (125.04452 - 1934.136261 * dt + 0.0020708 * dt * dt + (dt * dt * dt) / 450000) * Constants.DEG_TO_RAD;

        double meanObliquity = Utility.meanObliquity(dt);

        double nutLon = nutation(jd_ut, ttMinusUT)[0];
        double last = (
                gmst + obsLon + nutLon * Math.cos(meanObliquity)
                        + 0.00264 * Math.sin(omega) * Constants.ARCSEC_TO_RAD
                        + 0.000063 * Math.sin(2 * omega) * Constants.ARCSEC_TO_RAD
        );

        last = last % (2 * Math.PI);
        return last;
    }

    public static double[] precessionLaskarToOrFromJ2000(double[] in, double t, boolean toJ2000) {
        /* Evaluation of Laskar precession angles */
        double[] pApol = new double[] { 0, -8.66e-10, -4.759e-8, 2.424e-7, 1.3095e-5, 1.7451e-4, -1.8055e-3, -0.235316, 0.07732, 111.1971, 50290.966 };
        double[] Wpol = new double[] { 6.6402e-16, -2.69151e-15, -1.547021e-12, 7.521313e-12, 6.3190131e-10, -3.48388152e-9, -1.813065896e-7,
                2.75036225e-8, 7.4394531426e-5, -0.042078604317, 3.052112654975 };
        double[] zpol = new double[] { 1.2147e-16, 7.3759e-17, -8.26287e-14, 2.503410e-13, 2.4650839e-11, -5.4000441e-11, 1.32115526e-9,
                -5.998737027e-7, -1.6242797091e-5, 0.002278495537, 0.0 };
        double pA = pApol[0], W = Wpol[0], z = zpol[0];
        for (int i = 1; i < pApol.length; i ++) {
            pA = pA * 0.1 * t + pApol[i];
            W = W * 0.1 * t + Wpol[i];
            z = z * 0.1 * t + zpol[i];
        }
        pA *= Constants.ARCSEC_TO_RAD * 0.1 * t;

        // Rotation angles to or from J2000, note input and output are equatorial
        double[] rotAngles = null;
        if (!toJ2000) rotAngles = new double[] {meanObliquity(0), W, z, -W - pA, -meanObliquity(t)};
        if (toJ2000) rotAngles = new double[] {meanObliquity(t), W + pA, -z, -W, -meanObliquity(0)};

        /* Implementation by elementary rotations using expansions. First rotate about the x axis from the initial equator to the ecliptic */
        double[] out = rotate(in, getRotX(rotAngles[0]));
        /* Rotate about z axis to the node */
        out = rotate(out, getRotZ(rotAngles[1]));
        /* Rotate about new x axis by the inclination of the moving ecliptic on the ecliptic for the initial time */
        out = rotate(out, getRotX(rotAngles[2]));
        /* Rotate about new z axis back from the node */
        out = rotate(out, getRotZ(rotAngles[3]));
        /* Rotate about x axis to final equator */
        return rotate(out, getRotX(rotAngles[4]));
    }

    private static double[][] getRotX(double angle) {
        return new double[][] {
                new double[] {1.0, 0.0, 0.0},
                new double[] {0.0, Math.cos(angle), Math.sin(angle)},
                new double[] {0.0, -Math.sin(angle), Math.cos(angle)}};
    }

    /**
     * Returns a 3x3 pure rotation matrix along axis Y.
     * @param angle The angle to rotate.
     * @return The matrix.
     */
    private static double[][] getRotY(double angle) {
        return new double[][] {
                new double[] {Math.cos(angle), 0.0, -Math.sin(angle)},
                new double[] {0.0, 1.0, 0.0},
                new double[] {Math.sin(angle), 0.0, Math.cos(angle)}};
    }

    /**
     * Returns a 3x3 pure rotation matrix along axis Z.
     * @param angle The angle to rotate.
     * @return The matrix.
     */
    private static double[][] getRotZ(double angle) {
        return new double[][] {
                new double[] {Math.cos(angle), Math.sin(angle), 0.0},
                new double[] {-Math.sin(angle), Math.cos(angle), 0.0},
                new double[] {0.0, 0.0, 1.0}};
    }

    /**
     * Multiplication of a vector with a matrix
     * @param p The vector, with 3 (position) or 6 (position and velocity) components
     * @param m The 3x3 rotation matrix
     * @return The result of the rotation
     */
    private static double[] rotate(double[] p, double[][] m) {
        double[] out = new double[p.length];
        for (int i=0; i<3; i++) {
            out[i] = 0;
            for (int j=0; j<3; j++) {
                out[i] += m[i][j] * p[j];
            }
            if (p.length == 3) continue;
            out[i + 3] = 0;
            for (int j=0; j<3; j++) {
                out[i + 3] += m[i][j] * p[j + 3];
            }
        }
        return out;
    }
}
