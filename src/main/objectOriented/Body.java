package main.objectOriented;

import main.Constants;

import java.util.Arrays;

public class Body {
    private String name;
    private double mass;
    private double[] positionInitial;
    private double[] velocityInitial;
    private double[] acceleration;
    public double[] position;
    public double[] velocity;

    public Body(String name, double mass, double[] positionInitial, double[] velocityInitial) {
        this.name = name;
        this.mass = mass;
        this.positionInitial = positionInitial;
        this.velocityInitial = velocityInitial;
        this.acceleration = new double[3];
        this.position = new double[3];
        this.velocity = new double[3];
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double[] getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double[] acceleration) {
        this.acceleration = acceleration;
    }

    public double[] getPositionInitial() {
        return positionInitial;
    }

    public double[] getVelocityInitial() {
        return velocityInitial;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setVelocityInitial(double[] velocityInitial) {
        this.velocityInitial = velocityInitial;
    }

    public void setPositionInitial(double[] positionInitial) {
        this.positionInitial = positionInitial;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getPosition(int j) {
        return positionInitial[j] + position[j];
    }

    public double[] getPosition() {
        double[] pos = new double[3];
        for (int i = 0; i < 3; i++) {
            pos[i] = positionInitial[i] + position[i];
        }
        return pos;
    }

    public double getVelocity(int j) {
        return velocityInitial[j] + velocity[j];
    }

    public double[] getVelocity() {
        double[] vel = new double[3];
        for (int i = 0; i < 3; i++) {
            vel[i] = velocityInitial[i] + velocity[i];
        }
        return vel;
    }

    public double distance(Body body) {
        double r_2 = 0;
        for (int i = 0; i < position.length; i++) {
            r_2 += Math.pow(this.getPosition(i) - body.getPosition(i), 2);
        }
        return Math.sqrt(r_2);
    }
    public double distance(double[] bodyVector) {
        double r_2 = 0;
        for (int i = 0; i < position.length; i++) {
            r_2 += Math.pow(this.getPosition(i) - bodyVector[i], 2);
        }
        return Math.sqrt(r_2);
    }

    public double[] distanceVector(Body body) {
        double[] vectorDistance = new double[3];
        double[] vectorBody = body.getPositionInitial();
        for (int i = 0; i < position.length; i++) {
            vectorDistance[i] += vectorBody[i] - this.positionInitial[i];
        }
        return vectorDistance;
    }

    public double[] distanceVector(double[] skyPosition) {
        double[] vectorDistance = new double[3];
        for (int i = 0; i < position.length; i++) {
            vectorDistance[i] = skyPosition[i] - this.positionInitial[i];
        }
        return vectorDistance;
    }

    public double[] relativeVelocityVector(Body body) {
        double[] vectorVelocity = new double[3];
        double[] vectorBody = body.getVelocityInitial();
        for (int i = 0; i < position.length; i++) {
            vectorVelocity[i] += vectorBody[i] - this.velocityInitial[i];
        }
        return vectorVelocity;
    }

    public void ra_dec(Body body, boolean real, double[] obsPos) {//printear distancias tambien
        double ra, dec;
        double[] skyPosition = body.getPositionInitial().clone();
        skyPosition = distanceVector(skyPosition);
        double[] velocity = body.getVelocityInitial();
        if (obsPos != null) {
            for (int i = 0; i < skyPosition.length; i++) {
                skyPosition[i] -= obsPos[i];
            }
        }
        if (real) {
            double distance = Math.sqrt(Math.pow(skyPosition[0], 2) + Math.pow(skyPosition[1], 2) + Math.pow(skyPosition[2], 2));
            double time = distance / Constants.c;
            for (int j = 0; j < 3; j++) {
                skyPosition[j] = skyPosition[j] - velocity[j] * time;
            }
        }
        double h = Math.sqrt(Math.pow(skyPosition[0], 2) + Math.pow(skyPosition[1], 2)); //Math.hypot
        ra = Math.atan2(skyPosition[1], skyPosition[0]) * 180 / Math.PI;
        dec = Math.atan2(skyPosition[2], h) * 180 / Math.PI;
        System.out.println(body.getName() + " | " + ra + ": " + dec);
    }

    public double localApparentSiderealTime(double jd_ut, double ttMinusUT, double obsLon) {
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

        double eps0 = 84381.448;
        double[] pol = {-468093., -155., 199925., -5138., -24967., -3905., 712., 2787., 579., 245.};
        double meanObliquity = 0;
        for (int i = 0; i < pol.length; i++) {
            meanObliquity += pol[i] * 0.01 * Math.pow(dt * 0.01, i + 1);
        }
        meanObliquity = (meanObliquity + eps0) * Constants.ARCSEC_TO_RAD;

        double nutLon = nutation(jd_ut, ttMinusUT)[0];
        double last = (
                gmst + obsLon + nutLon * Math.cos(meanObliquity)
                        + 0.00264 * Math.sin(omega) * Constants.ARCSEC_TO_RAD
                        + 0.000063 * Math.sin(2 * omega) * Constants.ARCSEC_TO_RAD
        );

        return last;
    }

    public double[] vectorObserver(double jd_ut, double ttMinusUT, double obsLon, double obsLat, double obsAlt) {
        double lst = localApparentSiderealTime(jd_ut, ttMinusUT, obsLon);
        double geocLat = (obsLat - .1925 * Math.sin(2 * obsLat) * main.Constants.DEG_TO_RAD);
        double geocR = 1.0 - Math.pow(Math.sin(obsLat), 2) / 298.257;
        double radiusAU = (geocR * Constants.EARTH_RADIUS + obsAlt * 0.001 / Constants.UA);
        double cosLat = Math.cos(geocLat);
        double[] correction = new double[]{
                radiusAU * cosLat * Math.cos(lst),
                radiusAU * cosLat * Math.sin(lst),
                radiusAU * Math.sin(geocLat)};
        System.out.println(Arrays.toString(correction));
        return correction;
    }

    /**
     * Computes nutation in longitude and obliquity
     * @param jd Julian day in UT
     * @return Nutation angles in radians
     */
    public double[] nutation(double jd, double ttMinusUT) {
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
                * t) * Math.sin(2*F+OM) - 0.0301 * Math.sin(2*(F+OM)+Mp) +
                (0.0217 - 0.00005 * t) * Math.sin(a2 - M);
        double nutObl = ((9.2025 + .00089 * t) * Math.cos(OM) + (0.5736 -
                0.00031 * t) * Math.cos(a2) + (.0977 - 0.00005 * t) * Math.cos(a3)) +
                (-0.0895 + 0.00005 * t) * Math.cos(2 * OM) + (0.0054 - 0.00001
                * t) * Math.cos(M) - 0.00007 * Math.cos(Mp) +
                (0.0224 - 0.00006 * t) * Math.cos(a2 + M) + 0.0200 *
                Math.cos(2*F+OM);

        return new double[] {nutLon * Constants.ARCSEC_TO_RAD, nutObl *
                Constants.ARCSEC_TO_RAD};
    }
}
