package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ObjectManagement {
    private static final HashMap<String, Body> bodies = new HashMap<>();

    /**
     * Prints RA (Right Ascension) and Dec (Declination) for the complete list of bodies from the position of the body selected.
     * If the topocentric array is null, then the coordinates are calculated from the body center.
     *
     * @param bodyName    Name for the body selected as the reference system.
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
    public void updateBodies(List<Body> bodiesUpdate) {
        for (Body updateBody : bodiesUpdate) {
            bodies.put(updateBody.getName(), updateBody);
        }
    }


    /**
     * Creates instances and adds the bodies needed for the correct characterization of the Solar System and adds them to the hashmap with the name of the body as the Key and the Body as the Value.
     * (TDB 2000-01-01 12:00) Sun Body center 500@10
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
        Body pluto = new Body("Pluto", Constants.MASS_PLUTO, new double[]{-9.87535222992358, -27.978868119163504, -5.753691421762491}, new double[]{0.0030287508460142658, -0.001127593278936313, -0.001265129364676525}); //no coincide con horizons

        // 16 cuerpos menores que tiene en cuenta JPL
        Body ceres = new Body("Ceres", Constants.MASS_CERES, new double[]{-2.379327705912632E+00, 5.456711318714194E-01, 7.412254807098526E-01}, new double[]{-3.584228273217894E-03, -9.845217307737637E-03, -3.904543826022410E-03});
        Body vesta = new Body("Vesta", Constants.MASS_VESTA, new double[]{1.243605461680222E+00, 2.133870405923554E+00, 6.874866847781977E-01}, new double[]{-8.779290950353064E-03, 4.462599423343603E-03, 2.927653651039283E-03}); //Se utilizó el SB de JPL
        Body pallas = new Body("Pallas", Constants.MASS_PALLAS, new double[]{-8.411384433388419E-01, 1.944444968595056E+00, -3.274545822177069E-01}, new double[]{-1.173753499559210E-02, -5.300336219764020E-03, 1.862824062653985E-03}); //SB
        Body hygiea = new Body("Hygiea", Constants.MASS_HYGIEA, new double[]{-2.374062486038638E+00, -1.271928530960060E+00, -7.456415084531227E-01}, new double[]{6.109725584714315E-03, -8.312981774250642E-03, -3.324251433567044E-03}); //SB
        Body davida = new Body("Davida", Constants.MASS_DAVIDA, new double[]{-2.559593623470846E-01, -3.570257891952850E+00, -1.118571415085192E+00}, new double[]{7.727143597701131E-03, 8.384749989470708E-05, -2.179735688557923E-03}); //SB
        Body interamnia = new Body("Interamnia", Constants.MASS_INTERAMNIA, new double[]{-1.356753642919263E+00, -2.490233973485644E+00, -1.719578360179235E+00}, new double[]{8.211143716569797E-03, -3.677817626488679E-03, 9.607221729043243E-04}); //SB
        Body europa = new Body("Europa", Constants.MASS_EUROPA, new double[]{-2.742739560307649E+00, 7.159048917754582E-01, 5.363944361812114E-01}, new double[]{-3.797092088551376E-03, -9.359087295313760E-03, -2.764542342261691E-03}); //SB
        Body sylvia = new Body("Sylvia", Constants.MASS_SYLVIA, new double[]{-1.243450709480745E-02, 3.182851885015189E+00, 1.595471309821621E+00}, new double[]{-8.847643547576252E-03, -1.331158216094903E-04, 1.748886022145435E-03}); //SB
        Body eunomia = new Body("Eunomia", Constants.MASS_EUNOMIA, new double[]{-2.587398928455714E+00, 1.226024025883543E+00, 9.909957454443644E-02}, new double[]{-5.175498480382019E-03, -6.744880465907278E-03, -4.729689263274217E-03}); //SB
        Body juno = new Body("Juno", Constants.MASS_JUNO, new double[]{3.841773243423457E-01, -3.039797417312752E+00, -5.869332904676425E-01}, new double[]{8.411460625023161E-03, 2.828740126962025E-03, 2.041835262248759E-04}); //SB
        Body psyche = new Body("Psyche", Constants.MASS_PSYCHE, new double[]{2.502682689905626E+00, -4.963200139481901E-01, -2.592260824536308E-01}, new double[]{1.730892123600570E-03, 1.053220086828449E-02, 3.940457550580482E-03}); //SB
        Body camilla = new Body("Camilla", Constants.MASS_CAMILLA, new double[]{-1.198690949093367E-01, 3.137868182744258E+00, 7.535477772494622E-01}, new double[]{-9.875171428454163E-03, -7.590603708244406E-04, 2.883016100027749E-05}); //SB
        Body thisbe = new Body("Thisbe", Constants.MASS_THISBE, new double[]{6.008946089881430E-01, -2.103402661523676E+00, -8.797571084555780E-01}, new double[]{1.133620032706874E-02, 3.062874876590536E-03, 2.494564228242002E-03}); //SB
        Body iris = new Body("Iris", Constants.MASS_IRIS, new double[]{-1.170852109858923E+00, 1.713075339113774E+00, 5.877481809134372E-01}, new double[]{-1.140411295399638E-02, -3.627708315228408E-03, -2.674818418976173E-03}); //SB
        Body euphrosyne = new Body("Euphrosyne", Constants.MASS_EUPHROSYNE, new double[]{2.991570928199286E+00, 6.427953077511142E-02, -9.596726903023471E-01}, new double[]{-2.013552676903032E-04, 6.662965887950446E-03, 7.060117159991139E-03}); //SB
        Body cybele = new Body("Cybele", Constants.MASS_CYBELE, new double[]{-2.595759435191407E+00, 2.344640792620458E+00, 9.328164217953492E-01}, new double[]{-5.543337668789665E-03, -6.452077986352413E-03, -2.224482257965874E-03}); //SB

        // Cuerpos de estudio
        Body apophis = new Body("Apophis", 0, new double[]{-1.037925696098939E+00, -1.268092611036419E-01, -7.404282940432429E-02}, new double[]{4.227374301759195E-03, -1.412107207094790E-02, -5.145341154842345E-03});
        Body A2024YR4 = new Body("2024 YR4", 0, new double[]{-2.596307168653424E-01, -2.604287454649033E+00, -1.162009799132625E+00}, new double[]{7.551896469920276E-03, 5.004225858848752E-03, 2.674091869770479E-03});
        Body C3IATLAS = new Body("3I/ATLAS", 0, new double[]{1.253897637473739E+02, -2.721809832353439E+02, -1.037600160708741E+02}, new double[]{-1.340809972204520E-02, 2.869866907957716E-02, 1.095113063192082E-02});

        // Imágenes
        Body kibeshigemaro = new Body("Kibeshigemaro", 0, new double[]{7.954842509693949E-02, 3.293918052167155E+00, 8.152252650302890E-01}, new double[]{-8.695163676880786E-03, -9.872469761374068E-04, 2.038448785180896E-03});


        bodies.clear();


        bodies.put(sun.getName(), sun);
        bodies.put(mercury.getName(), mercury);
        bodies.put(venus.getName(), venus);
        bodies.put(earth.getName(), earth);
        bodies.put(mars.getName(), mars);
        bodies.put(jupiter.getName(), jupiter);
        bodies.put(saturn.getName(), saturn);
        bodies.put(uranus.getName(), uranus);
        bodies.put(neptune.getName(), neptune);
        bodies.put(pluto.getName(), pluto);
        bodies.put(moon.getName(), moon);

        //bodies.put(apophis.getName(), apophis);
        //bodies.put(A2024YR4.getName(), A2024YR4);
        //bodies.put(kibeshigemaro.getName(), kibeshigemaro);

/*
        bodies.put(ceres.getName(), ceres);
        bodies.put(vesta.getName(), vesta);
        bodies.put(pallas.getName(), pallas);
        bodies.put(hygiea.getName(), hygiea);
        bodies.put(davida.getName(), davida);
        bodies.put(interamnia.getName(), interamnia);
        bodies.put(europa.getName(), europa);
        bodies.put(sylvia.getName(), sylvia);
        bodies.put(eunomia.getName(), eunomia);
        bodies.put(juno.getName(), juno);
        bodies.put(psyche.getName(), psyche);
        bodies.put(camilla.getName(), camilla);
        bodies.put(thisbe.getName(), thisbe);
        bodies.put(iris.getName(), iris);
        bodies.put(euphrosyne.getName(), euphrosyne);
        bodies.put(cybele.getName(),cybele);*/
    }

    public HashMap<String, Body> getBodies() {
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

        for (Body skyObject : bodies.values()) {
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

        for (Body skyObject : bodies.values()) {
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
        Body earth = bodies.get(name);

        StringBuilder sb = new StringBuilder();

        sb.append("                    |------------------------------------------------------------------------|------------------------------------------------------------------------|\n");
        sb.append("                    |                           POSITION (X, Y, Z)                           |                          VELOCITY (VX, VY, VZ)                         |\n");
        sb.append("                    |------------------------------------------------------------------------|------------------------------------------------------------------------|\n");
        for (Body bodyPrint : bodies.values()) {
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

        for (Body b : bodies.values()) {
            if (b.getName().equals(name)) {
                body = b;
                break;
            }
        }

        return body;
    }


    /**
     * Saves RA and Dec for the bodies in the list in a file,
     *
     * @param filename Name of the file.
     * @param objectListName List of the bodies' name chosen to bew saved.
     */
    public void saveInFile(String filename, List<String> objectListName, boolean vector, boolean append){
        File directory = new File("src/main/java/resources");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File archivo = new File(directory, filename);

        try {
            FileWriter writer = new FileWriter(archivo, append);

            Body bodyReference = bodies.get("Sun");

            if (vector){
                for (String objectWrite : objectListName) {
                    StringBuilder sb = new StringBuilder();
                    Body bodyPrint = bodies.get(objectWrite);
                    double[] p = bodyReference.distanceVector(bodyPrint);
                    sb.append("1, ").append(bodyPrint.getName()).append(", ").append(String.format("%-22s", p[0])).append(", ").append(String.format("%-22s", p[1])).append(", ").append(String.format("%-22s", p[2]));
                    writer.write(sb.toString());
                    writer.write(System.lineSeparator());
                }

            }else {
                for (String objectWrite : objectListName) {
                    Body bodyWrite = bodies.get(objectWrite);
                    double[] ra_dec = bodyReference.ra_dec(bodyWrite, null);
                    StringBuilder sb = new StringBuilder(bodyWrite.getName());
                    sb.append(",").append(String.format(Locale.US, "%10.10f", ra_dec[0])).append(",").append(String.format(Locale.US, "%10.10f", ra_dec[1])).append("\n");
                    writer.write(sb.toString());
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
