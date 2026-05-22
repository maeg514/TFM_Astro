package main;

import java.awt.*;

public class Constants {
    public static final double G = 2.9591220828411956E-04;

    /**
     * Astronomical Unit in km. As defined by JPL in DE440.
     */
    public static final double AU = 149597870.7;

    /**
     * Speed of light in m/s, exact as it is defined.
     */
    public static final double SPEED_OF_LIGHT = 299792458.0;

    /**
     * Speed of light in AU/day.
     */
    public static final double c = (SPEED_OF_LIGHT * 0.001 / AU) * 86400.0;// En UA/dia
    public static final double mu = G / Math.pow(c, 2);

    /**
     * Normalized mass for the Earth.
     */
    public static final double MASS_EARTH = 3.003489615465139E-6; //1/332946.048773;

    /**
     * Earth radius in AU.
     */
    public static final double EARTH_RADIUS = 6378.13659999999982 / AU;
    public static final double J2_DIMENSIONLESS = 0.00108262539;
    public static final double J2 = J2_DIMENSIONLESS * Math.pow(EARTH_RADIUS, 2) * G * MASS_EARTH;


    /**
     * Radians to degrees.
     */
    public static final double RAD_TO_DEG = 180.0 / Math.PI;

    /**
     * Degrees to radians.
     */
    public static final double DEG_TO_RAD = 1.0 / RAD_TO_DEG;

    /* Arcseconds to radians */
    public static final double ARCSEC_TO_RAD = (DEG_TO_RAD / 3600.0);


    /**
     * Two times Pi.
     */
    public static final double TWO_PI = 2.0 * Math.PI;

    /**
     * Pi divided by two.
     */
    public static final double PI_OVER_TWO = Math.PI / 2.0;

    /**
     * Julian century conversion constant = 100 * days per year.
     */
    public static final double JULIAN_DAYS_PER_CENTURY = 36525.0;

    /**
     * Seconds in one day.
     */
    public static final double SECONDS_PER_DAY = 86400.0;

    /**
     * Light time in days for 1 AU. DE405 definition.
     */
    public static final double LIGHT_TIME_DAYS_PER_AU = 0.00577551833109;

    /**
     * Our default epoch. The Julian Day which represents noon on 2000-01-01.
     */
    public static final double J2000 = 2451545.0;

    /**
     * Length of a sidereal day in days according to IERS Conventions.
     */
    public static final double SIDEREAL_DAY_LENGTH = 1.00273781191135448;

    /**
     * Position coefficients for the RungeKutta Order 4 Classic.
     */
    public static final double[][] RK4C_POS_COEFFICIENTS = new double[][]{{1 / 2.}, {0, 1 / 2.}, {0, 0, 1}};

    /**
     * Final coefficients or weights for the RungeKutta Order 4 Classic.
     */
    public static final double[] RK4C_RK_COEFFICIENTS = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};

    /**
     * Improved position coefficients for the RungeKutta Order 4.
     */
    public static final double[][] RK4I_POS_COEFFICIENTS = new double[][]{{1 / 3.}, {-1 / 3., 1}, {1, -1, 1}};

    /**
     * Final coefficients or weights for the RungeKutta Order 4 Improved.
     */
    public static final double[] RK4I_RK_COEFFICIENTS = new double[]{1 / 8., 3 / 8., 3 / 8., 1 / 8.};

    /**
     * Position coefficients for the RungeKutta Order 5.
     */
    public static final double[][] RK5_POS_COEFFICIENTS = {
            {1 / 6.0},
            {2 / 27.0, 4 / 27.0},
            {183 / 1372.0, -162 / 343.0, 1053 / 1372.0},
            {68 / 297.0, -4 / 11.0, 42 / 143.0, 1960 / 3861.0},
            {597 / 22528.0, 81 / 352.0, 63099 / 585728.0, 58653 / 366080.0, 4617 / 20480.0},
            {174197 / 959244.0, -30942 / 79937.0, 8152137 / 19744439.0, 666106 / 1039181.0, -29421 / 29068.0, 482048 / 414219.0},
            {587 / 8064.0, 0.0, 4440339 / 15491840.0, 24353 / 124800.0, 387 / 44800.0, 2152 / 5985.0, 7267 / 94080.0}};

    /**
     * Final coefficients or weights for the RungeKutta Order 5.
     */
    public static final double[] RK5_RK_COEFFICIENTS = new double[]{587 / 8064.0, 0, 4440339 / 15491840.0, 24353 / 124800.0, 387 / 44800.0, 2152 / 5985.0, 7267 / 94080.0};

    /**
     * Position coefficients for the Normand Prince method.
     */
    public static final double[][] NORMAND_PRINCE_POS_COEFFICIENTS = {
            {1 / 5.0},
            {3 / 40.0, 9 / 40.0},
            {44 / 45.0, -56 / 15.0, 32 / 9.0},
            {19372 / 6561.0, -25360 / 2187.0, 64448 / 6561.0, -212 / 729.0},
            {9017 / 3168.0, -355 / 33.0, 46732 / 5247.0, 49 / 176.0, -5103 / 18656.0}};

    /**
     * Final coefficients or weights for the Normand Prince method.
     */
    public static final double[] NORMAND_PRINCE_RK_COEFFICIENTS = new double[]{35 / 384.0, 0.0, 500 / 1113.0, 125 / 192.0, -2187 / 6784.0, 11 / 84.0};

    /**
     * Normalized mass for the Sun, with the Sun as the most massive object of the Solar System.
     */
    public static double MASS_SUN = 1;

    /**
     * Normalized mass for Mercury.
     */
    public static final double MASS_MERCURY = 1.660120825489089E-7; //1/6023657.944929;

    /**
     * Normalized mass for Venus.
     */
    public static final double MASS_VENUS = 2.447838287796944E-6; //1/408523.718656;

    /**
     * Normalized mass for the Moon.
     */
    public static final double MASS_MOON = 3.6943033501098785E-8; //1/27068702.952351;

    /**
     * Normalized mass for Mars.
     */
    public static final double MASS_MARS = 3.2271560829138995E-7; //1/3098703.546737;

    /**
     * Normalized mass for Jupiter.
     */
    public static final double MASS_JUPITER = 9.547919099414246E-4; //1/1047.348631;

    /**
     * Normalized mass for Saturn.
     */
    public static final double MASS_SATURN = 2.8588567002459455E-4; //1/3497.901801;

    /**
     * Normalized mass for Uranus.
     */
    public static final double MASS_URANUS = 4.3662496132221186E-5; //1/22902.950783;

    /**
     * Normalized mass for Neptune.
     */
    public static final double MASS_NEPTUNE = 5.151383772654574E-5; //1/19412.259776;

    /**
     * Normalized mass for Pluto.
     */
    public static final double MASS_PLUTO = 7.350478973158631E-9; //1/136045556.167380;

    /**
     * Normalized mass for Ceres.
     */
    public static final double MASS_CERES = 4.719142E-10;

    /**
     * Background color for the text area.
     */
    public static final Color colorBackground = Color.decode("#222222");

    /**
     * Color text for the text area.
     */
    public static final Color colorText = Color.decode("#d6753d");


    //Ascii jpl ephemerides

    //Hay una incertidumbre en la masa de los planetas, por eso hay una diferencia entre las masas en las distintas fuentes


}
