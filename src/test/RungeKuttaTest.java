package test;

import main.Constants;
import main.ObjectManagement;
import main.Utility;
import main.efficiencyOriented.RungeKutta;
import main.Body;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RungeKuttaTest {

    private HashMap<String, Body> bodiesCalculatedValues;
    private HashMap<String, Body> bodiesErrorValues;

    @Test
    void testPositions() {


        double umbral = 0.01;

        relativeError();

        List<Executable> comprobaciones = new ArrayList<>();
        for (Body bodyError : bodiesErrorValues.values()) {
            double[] posError = bodyError.getPositionInitial();
            double[] velError = bodyError.getVelocityInitial();
            for (int i = 0; i < 3; i++) {
                int finalI = i;
                comprobaciones.add(() ->
                                assertTrue(posError[finalI] > umbral,
                        "La componente " + finalI + " del vector posición supera el umbral de error.")
                );
            }
            for (int i = 0; i < 3; i++) {
                int finalI = i;
                comprobaciones.add(() ->
                        assertTrue(velError[finalI] > umbral,
                                "La componente " + finalI + " del vector velocidad supera el umbral de error.")
                );
            }
        }

        assertAll(comprobaciones);
    }


    public void relativeError() {
        for (Body bodyError : bodiesErrorValues.values()) {
            Body bodyCalculated = bodiesCalculatedValues.get(bodyError.getName());
            double[] posCalculated = bodyCalculated.getPositionInitial();
            double[] velCalculated = bodyCalculated.getVelocityInitial();
            double[] posError = bodyError.getPositionInitial();
            double[] velError = bodyError.getVelocityInitial();
            for (int i = 0; i < 3; i++) {
                posError[i] = Math.abs(posError[i]-posCalculated[i]);
                velError[i] = Math.abs(velError[i]-velCalculated[i]);
            }
        }
    }

    public void addExpectedValues() {
        Body sun = new Body("Sun", new double[]{9.174111984309344E-01, 3.717994494509857E-01, 1.611624558094557E-01}, new double[]{-6.674331661898967E-03, 1.450415814310701E-02, 6.287017796545985E-03});
        Body mercury = new Body("Mercury", new double[]{7.905799019985850E-01, 6.233823070691173E-01, 3.087041816801627E-01}, new double[]{-3.809829738480040E-02, 4.299457792695273E-03, 4.092046377615045E-03});
        Body venus = new Body("Venus", new double[]{1.497362410800706E+00, 7.791706801017169E-01, 3.077814388634021E-01}, new double[]{-1.882020611604074E-02, 2.893876412037385E-02, 1.355075455472744E-02});
        Body earth = new Body("Earth", new double[]{-1.771350992727098E-01, 8.874285223255191E-01, 3.847428990882070E-01}, new double[]{-1.720762506872895E-02, -2.898167717572411E-03, -1.256395052182784E-03});
        Body moon = new Body("Moon", new double[]{2.476527044724693E-03, 9.122146298017155E-04, 6.431561577655856E-04}, new double[]{-2.292731123378179E-04, 4.755534035351698E-04, 1.905473870102167E-04});
        Body mars = new Body("Mars", new double[]{-6.700147827813567E-01, 1.336303799447882E-02, 3.956247730319566E-02}, new double[]{-2.918160337783553E-03, 3.250409290748070E-03, 1.023845022807565E-03});
        Body jupiter = new Body("Jupiter", new double[]{-4.126322715086816E+00, -1.570345238363382E+00, -5.485114379144114E-01}, new double[]{-3.903235412264955E-03, 8.427339488302387E-03, 3.614888592483175E-03});
        Body saturn = new Body("Saturn", new double[]{7.586827050620712E+00, 6.297602867329725E+00, 2.321466475679894E+00}, new double[]{-1.046952127599172E-02, 1.534628864354745E-02, 6.709547048244603E-03});
        Body uranus = new Body("Uranus", new double[]{6.448093972465624E+00, 1.730849794392529E+01, 7.500684038681587E+00}, new double[]{0.00267810559142015, 0.002462004302669807, 0.0010404094481152937});
        Body neptune = new Body("Neptune", new double[]{3.045785259733286E+01, 4.609473811440890E+00, 1.160168256335489E+00}, new double[]{-7.144139204074170E-03, 1.738824677271062E-02, 7.479102494882281E-03});
        Body pluto = new Body("Pluto", new double[]{2.330886892958736E+01, -2.423104362545851E+01, -1.426339163987423E+01}, new double[]{-4.112241172639988E-03, 1.597797653129441E-02, 5.974824795108667E-03});
        Body apophis = new Body("Apophis", new double[]{-1.455656900545057E-04, 1.751927427817715E-04, 1.141817805459706E-04}, new double[]{3.621977758203940E-03, 1.426507322271576E-03, 1.790082662150367E-03});

        bodiesErrorValues.put(sun.getName(), sun);
        bodiesErrorValues.put(mercury.getName(), mercury);
        bodiesErrorValues.put(venus.getName(), venus);
        bodiesErrorValues.put(mars.getName(), mars);
        bodiesErrorValues.put(jupiter.getName(), jupiter);
        bodiesErrorValues.put(saturn.getName(), saturn);
        bodiesErrorValues.put(uranus.getName(), uranus);
        bodiesErrorValues.put(neptune.getName(), neptune);
        bodiesErrorValues.put(pluto.getName(), pluto);
        bodiesErrorValues.put(moon.getName(), moon);
        bodiesErrorValues.put(apophis.getName(), apophis);
       /*double[][] expectedValues = new double[][]{
                    {9.174111984309344E-01, 3.717994494509857E-01, 1.611624558094557E-01, -6.674331661898967E-03, 1.450415814310701E-02, 6.287017796545985E-03},
                    {7.905799019985850E-01, 6.233823070691173E-01, 3.087041816801627E-01, -3.809829738480040E-02, 4.299457792695273E-03, 4.092046377615045E-03},
                    {1.497362410800706E+00, 7.791706801017169E-01, 3.077814388634021E-01, -1.882020611604074E-02, 2.893876412037385E-02, 1.355075455472744E-02},
                    {-6.700147827813567E-01, 1.336303799447882E-02, 3.956247730319566E-02, -2.918160337783553E-03, 3.250409290748070E-03, 1.023845022807565E-03},
                    {-4.126322715086816E+00, -1.570345238363382E+00, -5.485114379144114E-01, -3.903235412264955E-03, 8.427339488302387E-03, 3.614888592483175E-03},
                    {7.586827050620712E+00, 6.297602867329725E+00, 2.321466475679894E+00, -1.080327236559625E-02, 1.817507504624789E-02, 7.981078548674150E-03},
                    {6.448093972465624E+00, 1.730849794392529E+01, 7.500684038681587E+00, -1.046952127599172E-02, 1.534628864354745E-02, 6.709547048244603E-03},
                    {3.045785259733286E+01, 4.609473811440890E+00, 1.160168256335489E+00, -7.144139204074170E-03, 1.738824677271062E-02, 7.479102494882281E-03},
                    {2.330886892958736E+01, -2.423104362545851E+01, -1.426339163987423E+01, -4.112241172639988E-03, 1.597797653129441E-02, 5.974824795108667E-03},
                    {2.476527044724693E-03, 9.122146298017155E-04, 6.431561577655856E-04, -2.292731123378179E-04, 4.755534035351698E-04, 1.905473870102167E-04},
                    {-1.455656900545057E-04, 1.751927427817715E-04, 1.141817805459706E-04, 3.621977758203940E-03, 1.426507322271576E-03, 1.790082662150367E-03}};*/
    }

    public double[][] bodiesIntoArray(List<Body> bodies) {
        double[][] initial_posVel = new double[bodies.size() - 1][6];
        Body earth = bodies.get(3);
        int i = 0;
        for (Body skyObject : bodies) {
            if (skyObject.equals(earth)) {
                continue;
            }
            double[] pos = earth.distanceVector(skyObject);
            double[] vel = earth.relativeVelocityVector(skyObject);

            for (int j = 0; j < 3; j++) {
                initial_posVel[i][j] = pos[j];
                initial_posVel[i][j + 3] = vel[j];
            }
            i++;
        }
        return initial_posVel;
    }

    @BeforeEach
    public void getValues() {
        ObjectManagement managementTest = new ObjectManagement();
        managementTest.addBodies();
        RungeKutta rkTest = new RungeKutta(new ArrayList<>(managementTest.getBodies().values()), Constants.RK5_POS_COEFFICIENTS, Constants.RK5_RK_COEFFICIENTS);
        double ttMinusUt = 69.185 / 86400.0;
        double jd = Utility.dateToJulianDay(2029, 4, 13, 21, 38, false); // UTC
        double integrationEndTime2 = jd - 2451545.0 + ttMinusUt;
        double integrationStep2 = 0.075;
        rkTest.RK(0, integrationEndTime2, integrationStep2);
        rkTest.arrayIntoBodies();
        managementTest.updateBodies(rkTest.getBodies());
        bodiesCalculatedValues = managementTest.getBodies();
        addExpectedValues();
    }




    /* "Sun" = 10
    X = 9.174111984309344E-01 Y = 3.717994494509857E-01 Z = 1.611624558094557E-01
    VX=-6.674331661898967E-03 VY= 1.450415814310701E-02 VZ= 6.287017796545985E-03
     */

    /* "Moon" = 301
    X = 2.476527044724693E-03 Y = 9.122146298017155E-04 Z = 6.431561577655856E-04
    VX=-2.292731123378179E-04 VY= 4.755534035351698E-04 VZ= 1.905473870102167E-04
     */

    /* "Mercury" = 1
    X = 7.905799019985850E-01 Y = 6.233823070691173E-01 Z = 3.087041816801627E-01
    VX=-3.809829738480040E-02 VY= 4.299457792695273E-03 VZ= 4.092046377615045E-03
     */

    /* "Venus" = 2
    X = 1.497362410800706E+00 Y = 7.791706801017169E-01 Z = 3.077814388634021E-01
    VX=-1.882020611604074E-02 VY= 2.893876412037385E-02 VZ= 1.355075455472744E-02
     */

    /* "Mars" = 4
    X =-6.700147827813567E-01 Y = 1.336303799447882E-02 Z = 3.956247730319566E-02
    VX=-2.918160337783553E-03 VY= 3.250409290748070E-03 VZ= 1.023845022807565E-03
     */

    /* "Jupiter" = 5
    X =-4.126322715086816E+00 Y =-1.570345238363382E+00 Z =-5.485114379144114E-01
    VX=-3.903235412264955E-03 VY= 8.427339488302387E-03 VZ= 3.614888592483175E-03
     */

    /* "Saturn" = 6
    X = 7.586827050620712E+00 Y = 6.297602867329725E+00 Z = 2.321466475679894E+00
    VX=-1.080327236559625E-02 VY= 1.817507504624789E-02 VZ= 7.981078548674150E-03
     */

    /* "Uranus" = 7
    X = 6.448093972465624E+00 Y = 1.730849794392529E+01 Z = 7.500684038681587E+00
    VX=-1.046952127599172E-02 VY= 1.534628864354745E-02 VZ= 6.709547048244603E-03
     */

    /* "Neptune" = 8
    X = 3.045785259733286E+01 Y = 4.609473811440890E+00 Z = 1.160168256335489E+00
    VX=-7.144139204074170E-03 VY= 1.738824677271062E-02 VZ= 7.479102494882281E-03
     */

    /* "Pluto" = 9
    X = 2.330886892958736E+01 Y =-2.423104362545851E+01 Z =-1.426339163987423E+01
    VX=-4.112241172639988E-03 VY= 1.597797653129441E-02 VZ= 5.974824795108667E-03
     */

    /* "Apophis"
    X =-1.455656900545057E-04 Y = 1.751927427817715E-04 Z = 1.141817805459706E-04
    VX= 3.621977758203940E-03 VY= 1.426507322271576E-03 VZ= 1.790082662150367E-03
     */

}
