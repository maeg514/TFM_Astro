package main;

import main.efficiencyOriented.RungeKutta;

import java.util.ArrayList;
import java.util.List;

public class RKController {
    private final ObjectManagement objects;


    public RKController() {
        this.objects = new ObjectManagement();
        this.objects.addBodies();
    }

    public void run(String rkType) {

        long currentTime = System.currentTimeMillis();


        //Apophis
        /*double ttMinusUt = 69.185 / Constants.SECONDS_PER_DAY;
        double jd = Utility.dateToJulianDay(2029, 4, 13, 21, 38, false); // UTC*/

        //2024 YR4
        double ttMinusUt = 69.183627 / Constants.SECONDS_PER_DAY;
        double jd = Utility.dateToJulianDay(2032, 12, 22, 8, 24, false);


        double integrationEndTime2 = jd - 2451545.0 + ttMinusUt;
        double integrationStep2 = 0.1;

        //double integrationEndTime = 1.0; // jd - 2451545.0; (TDB) // Diferencia entre TT
        //double jd = main.Constants.dateToJulianDay(2024,1,1,12,0,false); // UTC
        //double jd = Utility.dateToJulianDay(2025,6,26,2,45,false); // UTC
        //double integrationEndTime2 = jd - 2451545.0;
        //double integrationEndTime3 = 0.2;
        //double integrationStep = 1.0;

        System.out.println("Integration End Time should be: " + integrationEndTime2);


        RungeKutta rungeKutta = selectRK(rkType);
        rungeKutta.RK(0, integrationEndTime2, integrationStep2);
        rungeKutta.arrayIntoBodies();

        objects.updateBodies(rungeKutta.getBodies());

        System.out.println(objects.prettyPrintPosition(null));

        System.out.println(objects.ra_dec("Earth", null));

        System.out.println(objects.prettyPrintPosition("Earth"));

        double[] topocentric = new double[]{
                jd,
                ttMinusUt * Constants.SECONDS_PER_DAY,
                -(3 + 42 / 60.0), // lon
                40 + 26 / 60.0, //lat
                0 //alt
        };

        System.out.println(objects.ra_dec("Earth", topocentric));

        List<String> bodyList = List.of("Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto", "Sun", "2024 YR4", "Moon");

        objects.saveInFile("2024YR4_RK5_025_v2", bodyList);

        long endTime = System.currentTimeMillis();
        double elapsed = (endTime - currentTime) * 0.001;
        System.out.println("Time: " + (float) elapsed);

    }

    /**
     * Creates an instance of the Runge Kutta (RK) class with the corresponding coefficients.
     *
     * @param RKType String indicating the RK method chosen between the RK order 4 Classic ("RK4C"),
     *               RK order 4 with Improved coefficients ("RK4I"), RK order 5 ("RK5")
     *               and RK order 5 with Dormand-Prince coefficients ("D-P").
     * @return A {@code RungeKutta} with the correct coefficients for the selected type.
     */
    private RungeKutta selectRK(String RKType) {
        List<Body> bodiesRK = new ArrayList<>(objects.getBodies().values());
        switch (RKType) {
            case "RK5":
                return new RungeKutta(bodiesRK, Constants.RK5_POS_COEFFICIENTS, Constants.RK5_RK_COEFFICIENTS);
            case "D-P":
                return new RungeKutta(bodiesRK, Constants.DORMAND_PRINCE_POS_COEFFICIENTS, Constants.DORMAND_PRINCE_RK_COEFFICIENTS);
            case "RK4I":
                return new RungeKutta(bodiesRK, Constants.RK4I_POS_COEFFICIENTS, Constants.RK4I_RK_COEFFICIENTS);
            case "RK4C":
            default:
                return new RungeKutta(bodiesRK, Constants.RK4C_POS_COEFFICIENTS, Constants.RK4C_RK_COEFFICIENTS);
        }
    }

    /**
     * Returns a list contaning the bodies of the ObjectManagement map of bodies.
     *
     * @return ArrayList of bodies contained in ObjectManagement.
     */
    public List<Body> getBodies() {
        return new ArrayList<>(objects.getBodies().values());
    }

    /**
     * Gets the object management class that.
     *
     * @return A {@code ObjectManagement} used for the management of the bodies.
     */
    public ObjectManagement getObjects() {
        return objects;
    }
}
