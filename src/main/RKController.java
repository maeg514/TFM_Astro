package main;

import main.efficiencyOriented.RungeKutta;

import java.time.LocalDateTime;
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


        List<int[]> fechas = new ArrayList<>();

        anadirFechas(fechas, "2000", true);

        for (int[] f : fechas) {
            objects.addBodies();

            //Apophis
            /*double ttMinusUt = 69.185 / Constants.SECONDS_PER_DAY;
            double jd = Utility.dateToJulianDay(2029, 4, f[0], f[1], f[2], false); // UTC*/

            //2024 YR4
            /*double ttMinusUt = 69.183627 / Constants.SECONDS_PER_DAY;
            double jd = Utility.dateToJulianDay(2032, 12, f[0], f[1], f[2], false);*/

            //Prueba atras
            //double ttMinusUt = 18.621954 / Constants.SECONDS_PER_DAY; //1800
            //double ttMinusUt = 110.459908 / Constants.SECONDS_PER_DAY; //1600
            //double ttMinusUt = 485.544920 / Constants.SECONDS_PER_DAY; //1400 2232408
            //double ttMinusUt = 920.638852 / Constants.SECONDS_PER_DAY; //1200 2159358
            //double ttMinusUt = 1660.030446 / Constants.SECONDS_PER_DAY; //1000 2086308
            //double ttMinusUt = 2957.130704 / Constants.SECONDS_PER_DAY; //800 2013258
            //double ttMinusUt = 4671.051761 / Constants.SECONDS_PER_DAY; //600 1940208
            //double ttMinusUt = 6560.663171 / Constants.SECONDS_PER_DAY; //400 1867158
            //double ttMinusUt = 8457.241354 / Constants.SECONDS_PER_DAY; //200 1794108
            double ttMinusUt = 10481.694274 / Constants.SECONDS_PER_DAY; //0 1721424



            double jd = Utility.dateToJulianDay(1, 1, 1, 12, 0, false);
            double integrationEndTime2 = 1721424 - 2451545.0 + ttMinusUt;
            double integrationStep2 = -0.1;

            //double integrationEndTime = 1.0; // jd - 2451545.0; (TDB) // Diferencia entre TT

            System.out.println("Integration End Time should be: " + integrationEndTime2);


            RungeKutta rungeKutta = selectRK(rkType);
            rungeKutta.RK(0, integrationEndTime2, integrationStep2);
            rungeKutta.arrayIntoBodies();

            objects.updateBodies(rungeKutta.getBodies());

            System.out.println(objects.prettyPrintPosition(null));

            System.out.println(objects.ra_dec("Earth", null));

            System.out.println(objects.prettyPrintPosition("Sun"));

            double[] topocentric = new double[]{
                    jd,
                    ttMinusUt * Constants.SECONDS_PER_DAY,
                    -(3 + 42 / 60.0), // lon
                    40 + 26 / 60.0, //lat
                    0 //alt
            };

            System.out.println(objects.ra_dec("Earth", topocentric));

            List<String> bodyList = List.of("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto", "Moon");

            //List<String> bodyList = List.of("Apophis");

            //List<String> bodyList = List.of("2024 YR4");

            objects.saveInFile("GraficoErrores2000", bodyList, true, true);

            long endTime = System.currentTimeMillis();
            double elapsed = (endTime - currentTime) * 0.001;
            System.out.println("Time: " + (float) elapsed);
        }

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

    public void anadirFechas(List<int[]> fechas, String type, boolean unico) {
        LocalDateTime fechaCentral;
        switch (type){
            case "Apophis":
                fechaCentral = LocalDateTime.of(2029, 4, 13, 21, 38);
                if (unico) {
                    fechas.add(new int[]{fechaCentral.getDayOfMonth(), fechaCentral.getHour(), fechaCentral.getMinute()});
                } else {
                    LocalDateTime inicio = fechaCentral.minusHours(12);
                    LocalDateTime fin = fechaCentral.plusHours(12);

                    for (LocalDateTime fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusMinutes(30)) {
                        fechas.add(new int[]{fecha.getDayOfMonth(), fecha.getHour(), fecha.getMinute()});
                    }
                }
                break;
            case "2024YR4":
                fechaCentral = LocalDateTime.of(2032, 12, 22, 8, 24);

                if (unico) {
                    fechas.add(new int[]{fechaCentral.getDayOfMonth(), fechaCentral.getHour(), fechaCentral.getMinute()});
                } else {
                    LocalDateTime inicio = fechaCentral.minusHours(12);
                    LocalDateTime fin = fechaCentral.plusHours(12);

                    for (LocalDateTime fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusMinutes(30)) {
                        fechas.add(new int[]{fecha.getDayOfMonth(), fecha.getHour(), fecha.getMinute()});
                    }
                }
                break;
            case "2000":
            default:
                fechaCentral = LocalDateTime.of(2000, 1, 1, 12, 0);

                if (unico) {
                    fechas.add(new int[]{fechaCentral.getYear()});
                } else {
                    LocalDateTime inicio = fechaCentral.minusYears(200);
                    for (LocalDateTime fecha = fechaCentral; !fecha.isBefore(inicio); fecha = fecha.minusYears(100)) {
                        fechas.add(new int[]{fecha.getYear()});
                    }
                }

        }

    }

}
