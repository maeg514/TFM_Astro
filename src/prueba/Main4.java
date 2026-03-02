import prueba.*;


void main() {
    RungeKutta4 rungeKutta = new RungeKutta4();
    long currentTime = System.currentTimeMillis();

    double ttMinusUt = 69.185 / 86400.0;
    double jd = Constants.dateToJulianDay(2029, 4, 13, 21, 38, false); // UTC
    //double jd = Constants.dateToJulianDay(2024,1,1,12,0,false); // UTC
    double integrationEndTime = 1.0; // jd - 2451545.0; (TDB) // Diferencia entre TT
    double integrationEndTime2 = jd - 2451545.0 + ttMinusUt;
    double integrationStep = 0.1;
    System.out.println("Integration End Time should be: " + integrationEndTime2);
    rungeKutta.RK4(0, integrationEndTime2, integrationStep);
    long endTime = System.currentTimeMillis();
    double elapsed = (endTime - currentTime) * 0.001;
    System.out.println("Time: " + (float) elapsed);
}
//Terminar cambios tareas
//Printear con un poco más de sentido las cosas
//Implementar el JUnit para los test (No se si esto puede ir de la mano de lo que tienen Horizons)

/*
Se podría hacer una especie de menu que te deje ejecutar el runge kutta de forma bruta o con una configuración personalizada.
Dentro de los parámetros a elegir tenemos:
-Guardado
-Calidad de precisión
-Comparación con otros resultados
-Gráficos?

*/