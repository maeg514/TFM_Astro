package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ObjectManagement {
    private static final List<Body> bodies = new ArrayList<>();

    /**
     * Prints RA (Right Ascension) and Dec (Declination) for the complete list of bodies from the position of the body selected.
     * If the topocentric array is null, then the coordinates are calculated from the body center.
     *
     * @param bodyName Name for the body selected as the reference system.
     * @param topocentric Array containing the position of the observer.
     * @return String containing the Right Ascension and Declination for all bodies.
     */
    public String ra_dec(String bodyName, double[] topocentric) {
        Body bodyReference = getBody(bodyName);
        String coordinates;

        if (topocentric == null) {
            coordinates = prettyPrintRaDec(bodyReference);
        } else {
            coordinates = prettyPrintRaDecTop(bodyReference, topocentric);
        }

        return coordinates;
    }


    /**
     * Updates the position for the bodies in the list with the results obtained with the RungeKutta method.
     */
    public void updateBodies(double[][] results) {
        for (int i = 0; i < bodies.size(); i++) {
            Body updateBody = bodies.get(i);
            double[] pos = new double[3];
            double[] vel = new double[3];

            for (int j = 0; j < 3; j++) {
                pos[j] = results[i][j];
                vel[j] = results[i][j + 3];
            }
            updateBody.setPositionInitial(pos);
            updateBody.setVelocityInitial(vel);
        }
    }


    /**
     * Creates instances and adds the bodies needed for the correct characterization of the Solar System.
     */
    public void addBodies() {
        Body sun = new Body("Sun", Constants.MASS_SUN, new double[]{0, 0, 0}, new double[]{0, 0, 0});
        Body mercury = new Body("Mercury", Constants.MASS_MERCURY, new double[]{-0.1300936053754522, -0.40059372164232543, -0.20048930201672596}, new double[]{0.021366395668016163, -0.004926299692875428, -0.004847433077772866});
        Body venus = new Body("Venus", Constants.MASS_VENUS, new double[]{-0.718302296345389, -0.04627424670211335, 0.02464063845542861}, new double[]{7.981175157753219E-4, -0.018491837481062413, -0.008369735338020125});
        Body earth = new Body("Earth", Constants.MASS_EARTH, new double[]{-1.771350992727098E-01, 8.874285223255191E-01, 3.847428990882070E-01}, new double[]{-1.720762506872895E-02, -2.898167717572411E-03, -1.256395052182784E-03});
        Body moon = new Body("Moon", Constants.MASS_MOON, new double[]{-1.790843809223965E-01, 8.856456304126460E-01, 3.842341853815847E-01}, new double[]{-1.683595459141215E-02, -3.282865544741707E-03, -1.430425208901575E-03});
        Body mars = new Body("Mars", Constants.MASS_MARS, new double[]{1.390715921746287, 0.001401217626814569, -0.036960167196011424}, new double[]{6.71499521033585E-4, 0.013814037515614361, 0.006317900433310847});
        Body jupiter = new Body("Jupiter", Constants.MASS_JUPITER, new double[]{4.001177161126057, 2.7365787240216024, 1.0755122808242419}, new double[]{-0.004568313526752718, 0.005881462129979568, 0.0026323030159255195});
        Body saturn = new Body("Saturn", Constants.MASS_SATURN, new double[]{6.406408859532808, 6.174657792651239, 2.274770783705428}, new double[]{-0.00429235187384325, 0.0035283445659715523, 0.0016419315191857945});
        Body uranus = new Body("Uranus", Constants.MASS_URANUS, new double[]{14.431856614381783, -12.506266259007408, -5.681690059289029}, new double[]{0.00267810559142015, 0.002462004302669807, 0.0010404094481152937});
        Body neptune = new Body("Neptune", Constants.MASS_NEPTUNE, new double[]{16.812046968052883, -22.980100505749114, -9.824427653612803}, new double[]{0.002579274259047737, 0.001668425282316438, 6.188152032295604E-4});
        Body pluto = new Body("Pluto", Constants.MASS_PLUTO, new double[]{-9.87535222992358, -27.978868119163504, -5.753691421762491}, new double[]{0.0030287508460142658, -0.001127593278936313, -0.001265129364676525});
        Body ceres = new Body("Ceres", Constants.MASS_CERES, new double[]{-2.379327705912632E+00, 5.456711318714194E-01, 7.412254807098526E-01}, new double[]{-3.584228273217894E-03, -9.845217307737637E-03, -3.904543826022410E-03});
        Body apophis = new Body("Apophis", 0, new double[]{-1.037925696098939E+00, -1.268092611036419E-01, -7.404282940432429E-02}, new double[]{4.227374301759195E-03, -1.412107207094790E-02, -5.145341154842345E-03});
        Body A2024YR4 = new Body("2024 YR4", 0, new double[]{-2.594982968163765E-01, -2.604198475987155E+00, -1.161962651207284E+00}, new double[]{7.551958208069564E-03, 5.004810090096610E-03, 2.674350852357362E-03});
        Body C3IATLAS = new Body("3I/ATLAS", 0, new double[]{1.253897637473739E+02, -2.721809832353439E+02, -1.037600160708741E+02}, new double[]{-1.340809972204520E-02, 2.869866907957716E-02, 1.095113063192082E-02});

        bodies.clear();

        bodies.add(sun);
        bodies.add(mercury);
        bodies.add(venus);
        bodies.add(earth);
        bodies.add(mars);
        bodies.add(jupiter);
        bodies.add(saturn);
        bodies.add(uranus);
        bodies.add(neptune);
        bodies.add(pluto);
        bodies.add(moon);
        bodies.add(apophis);
        //bodies.add(ceres);
    }

    public List<Body> getBodies() {
        return bodies;
    }

    /**
     * Prints RA (Right Ascension) and Dec (Declination) for the complete list of bodies from the Geocentric point of view.
     *
     * @return String containing the Right Ascension and Declination for all bodies from Geocentric location.
     */
    private String prettyPrintRaDec(Body bodyReference) {
        StringBuilder sb = new StringBuilder();

        sb.append("||----------< Right Ascension and Declination from Earth's Center Position (Geocentric) >----------||\n");

        for (Body skyObject : bodies) {
            if (skyObject.getName().equals("Earth")) continue;

            sb.append("• ").append(skyObject.getName()).append(":\n");
            double[] ra_dec = bodyReference.ra_dec(skyObject, null);

            sb.append("   RA  (degrees)  --> ").append(String.format(Locale.US, "%10.10f", ra_dec[0])).append("\n");
            sb.append("   Dec (degrees)  --> ").append(String.format(Locale.US, "%10.10f", ra_dec[1])).append("\n");
            sb.append("   Distance (UA)  --> ").append(String.format(Locale.US, "%10.10f", ra_dec[2])).append("\n").append("\n");

        }

        sb.append("||-----------------------------------------------------------------------------------------------||");
        return sb.toString();
    }

    /**
     * Prints RA (Right Ascension) and Dec (Declination) for the complete list of bodies from the Geocentric point of view.
     *
     * @return String containing the Right Ascension and Declination for all bodies from Geocentric location.
     */
    private String prettyPrintRaDecTop(Body bodyReference, double[] topocentric) {
        StringBuilder sb = new StringBuilder();
        double jd_ut = topocentric[0];
        double ttMinusUT = topocentric[1];
        double obsLon = topocentric[2] * Constants.DEG_TO_RAD;
        double obsLat = topocentric[3] * Constants.DEG_TO_RAD;
        double obsAlt = topocentric[4];

        sb.append("||----------< Right Ascension and Declination from Earth's Center Position (Topocentric) >----------||\n");

        for (Body skyObject : bodies) {
            if (skyObject.getName().equals("Earth")) continue;

            sb.append("• ").append(skyObject.getName()).append(":\n");

            double[] ra_dec = bodyReference.ra_dec(skyObject, Utility.vectorObserver(jd_ut, ttMinusUT, obsLon, obsLat, obsAlt));

            sb.append("   RA  (degrees)  --> ").append(ra_dec[0]).append("\n");
            sb.append("   Dec (degrees)  --> ").append(ra_dec[1]).append("\n");
            sb.append("   Distance (UA)  --> ").append(ra_dec[2]).append("\n").append("\n");

        }

        sb.append("||-----------------------------------------------------------------------------------------------||");
        return sb.toString();
    }

    /**
     * Prints position and velocity for the complete list of bodies from the object's name point of view.
     * If the value name is null, it prints the heliocentric coordinate system.
     *
     * @param name Name for the coordinate system used.
     * @return String containing position and velocity for all bodies.
     */
    public String prettyPrintPosition(String name) {
        Body earth = getBody(name);

        StringBuilder sb = new StringBuilder();

        sb.append("                    |------------------------------------------------------------------------|------------------------------------------------------------------------|\n");
        sb.append("                    |                           POSITION (X, Y, Z)                           |                          VELOCITY (VX, VY, VZ)                         |\n");
        sb.append("                    |------------------------------------------------------------------------|------------------------------------------------------------------------|\n");
        for (Body bodyPrint : bodies) {
            double[] p;
            double[] v;

            if (name == null) {
                p = bodyPrint.getPositionInitial();
                v = bodyPrint.getVelocityInitial();
            } else {
                p = earth.distanceVector(bodyPrint);
                v = earth.relativeVelocityVector(bodyPrint);
            }

            sb.append(String.format("%-20s", bodyPrint.getName())).append(": ");
            sb.append(String.format("%-22s", p[0])).append(", ").append(String.format("%-22s", p[1])).append(", ").append(String.format("%-22s", p[2])).append(" | ");
            sb.append(String.format("%-22s", v[0])).append(", ").append(String.format("%-22s", v[1])).append(", ").append(String.format("%-22s", v[2])).append("\n");
        }

        return sb.toString();
    }

    /**
     * Founds body in the static list of bodies searching by name.
     * If the body is not found, it returns null.
     *
     * @param name Name of the body searched
     * @return First body with the name searched.
     */
    public Body getBody(String name) {
        Body body = null;

        for (Body b : bodies) {
            if (b.getName().equals(name)) {
                body = b;
                break;
            }
        }

        return body;
    }


    private boolean changesChecking() {
        boolean change = false;
        double[][] vectorChecking = {
                {-0.049242309887471504, 0.074132509477252, 0.0330492542781804, -6.638840273079683E-6, 1.1333410836986347E-5, 5.020739495743468E-6},
                {-0.1760739199818666, 0.32571526005188833, 0.18059095619818513, -0.03143059302208097, -0.010193391021771555, -0.0021899646768472115},
                {0.5307088992946106, 0.4815037437874529, 0.17966823928803466, -0.012152513410775403, 0.014445939303657404, 0.00726875747899652},
                {-0.9666535235112791, -0.29766690149825836, -0.12811318383017878, 0.006667698406407208, -0.014492822729155636, -0.006281995555942135},
                {-1.6366682834390605, -0.28430391930889515, -0.08855073053025654, 0.0037495327509574727, -0.011242415373455425, -0.005258152056345891},
                {-5.092976239731671, -1.868012145731055, -0.6766246251589241, 0.0027644573490153133, -0.006065485255554673, -0.0026671084712878933},
                {6.620173515580458, 5.99993594982585, 2.1933532843135004, -0.004135579566238353, 0.003682250293646825, 0.001699081484892807},
                {5.481440243214165, 17.010830890611494, 7.372570789687953, -0.003801828505975123, 8.534638617653026E-4, 4.2754997070165826E-4},
                {29.491198701523263, 4.311806999682784, 1.0320551034608143, -4.7644648041138786E-4, 0.0028954220548403336, 0.0011971054423927895},
                {-0.9641781702968775, -0.29675226035312885, -0.12746905047546234, 0.006437865411518236, -0.014017476469827213, -0.0060915920244318},
                {22.342215393477137, -24.52871026222943, -14.39150456821061, 0.0025554516150016306, 0.0014851518811703145, -3.071722236487479E-4},
                {-0.9668011993134284, -0.2974885677327149, -0.127997618542421, 0.010258531574114051, -0.013057629302864146, -0.004487815348114064},
        };

        double[][] differences = new double[bodies.size()][6];

        for (int i = 0; i < bodies.size(); i++) {
            Body bodyCheck = bodies.get(i);
            double[] posCheck = bodyCheck.getPositionInitial();
            double[] velCheck = bodyCheck.getVelocityInitial();
            for (int j = 0; j < 3; j++) {
                differences[i][j] = posCheck[j] - vectorChecking[i][j];
                differences[i][j + 3] = velCheck[j] - vectorChecking[i][j + 3];
                if (!change) {
                    if (differences[i][j] != 0 || differences[i][j + 3] != 0) change = true;
                }
            }
        }

        for (double[] fila : differences) {
            System.out.println(Arrays.toString(fila));
        }
        return change;
    }


    private double[][] bodiesIntoArray(List<Body> bodies) {
        double[][] initial_posVel = new double[bodies.size()][6];
        for (int i = 0; i < bodies.size(); i++) {
            double[] pos = bodies.get(i).getPositionInitial().clone();
            double[] vel = bodies.get(i).getVelocityInitial().clone();

            for (int j = 0; j < 3; j++) {
                initial_posVel[i][j] = pos[j];
                initial_posVel[i][j + 3] = vel[j];
            }
        }
        return initial_posVel;
    }
}
