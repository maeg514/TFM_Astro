package prueba;

public class MainPruebas {
    public static void main(String[] args) {
        RungeKutta4 rungeKutta = new RungeKutta4();
        long currentTime = System.currentTimeMillis();

        double ttMinusUt = 69.185 / 86400.0;
        double jd = Utility.dateToJulianDay(2029, 4, 13, 21, 38, false); // UTC
        //double jd = Constants.dateToJulianDay(2024,1,1,12,0,false); // UTC
        double integrationEndTime = 1.0; // jd - 2451545.0; (TDB) // Diferencia entre TT
        double integrationEndTime2 = jd - 2451545.0 + ttMinusUt;
        double integrationStep = 0.01;
        System.out.println("Integration End Time should be: " + integrationEndTime2);

        rungeKutta.RK4(0, integrationEndTime2, integrationStep);
        long endTime = System.currentTimeMillis();
        double elapsed = (endTime - currentTime) * 0.001;
        System.out.println("Time: " + (float) elapsed);

        double lon = -(3 + 42 / 60.0);
        double lat = 40 + 26 / 60.0;
        double alt = 0;
        rungeKutta.ra_dec_Observer(jd, ttMinusUt * 86400, lon, lat, alt);
    }
}
