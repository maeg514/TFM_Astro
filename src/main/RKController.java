package main;

import main.efficiencyOriented.RungeKutta;

import java.util.List;
import java.util.Scanner;

public class RKController {
    private final ObjectManagement objects;


    public RKController() {
        this.objects = new ObjectManagement();
        this.objects.addBodies();
    }

    public void run(String rkType) {

        Scanner teclado = new Scanner(System.in);

        long currentTime = System.currentTimeMillis();

        double ttMinusUt = 69.185 / 86400.0;
        double jd = Utility.dateToJulianDay(2029, 4, 13, 21, 38, false); // UTC
        //double jd = main.Constants.dateToJulianDay(2024,1,1,12,0,false); // UTC
        double integrationEndTime = 1.0; // jd - 2451545.0; (TDB) // Diferencia entre TT
        double integrationEndTime2 = jd - 2451545.0 + ttMinusUt;
        double integrationEndTime3 = 0.2;
        double integrationStep2 = 0.075;
        double integrationStep = 1.0;
        System.out.println("Integration End Time should be: " + integrationEndTime2);
        //rungeKutta.RK4(0, integrationEndTime2, integrationStep2);
        //rungeKutta.RK4_v2(0, integrationEndTime2, integrationStep2);

        //String rkType = "RK5";
        //String rkType = "RK4I";
        //String rkType = "RK4C";

        RungeKutta rungeKutta = selectRK(rkType);
        rungeKutta.RK(0, integrationEndTime2, integrationStep2);
        double[][] resultado = rungeKutta.getPos_vel_Initial();


        //System.out.println(Arrays.deepToString(resultado));

        objects.updateBodies(resultado);


        String approveChanges = "y";
        /*if (changesChecking()){
            System.out.println("¿Desea actualizar los valores de los cuerpos? (y/n)");
            approveChanges = teclado.next();
        }*/

        if (approveChanges.equals("y")) {
            Utility.arrayPrinter(objects.getBodies());
            objects.ra_dec();
            objects.vectorGeocentric();
            //changesChecking();
        }

        double lon = -(3 + 42 / 60.0);
        double lat = 40 + 26 / 60.0;
        double alt = 0;
        objects.ra_dec_Observer(jd, ttMinusUt * 86400, lon, lat, alt);


        long endTime = System.currentTimeMillis();
        double elapsed = (endTime - currentTime) * 0.001;
        System.out.println("Time: " + (float) elapsed);

    }

    private RungeKutta selectRK(String RKType) {
        switch (RKType) {
            case "RK5":
                return new RungeKutta(objects.getBodies(), Constants.RK5_POS_COEFFICIENTS, Constants.RK5_RK_COEFFICIENTS);
            case "RK4I":
                return new RungeKutta(objects.getBodies(), Constants.RK4I_POS_COEFFICIENTS, Constants.RK4I_RK_COEFFICIENTS);
            case "RK4C":
            default:
                return new RungeKutta(objects.getBodies(), Constants.RK4C_POS_COEFFICIENTS, Constants.RK4C_RK_COEFFICIENTS);
        }
    }



    private void saveResult() {

    }


    public List<Body> getBodies() {
        return objects.getBodies();
    }

    public ObjectManagement getObjects() {
        return objects;
    }
}
