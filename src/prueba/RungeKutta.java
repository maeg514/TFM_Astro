package prueba;

import java.util.ArrayList;
import java.util.List;

public class RungeKutta {
    List<Body> bodies = new ArrayList<>();

    public RungeKutta() {

        Body sun = new Body("Sun", Constants.MASS_SUN, new double[]{0, 0, 0}, new double[]{0, 0, 0});
        Body mercury = new Body("Mercury", Constants.MASS_MERCURY, new double[]{-1.300936053934398 * Math.pow(10, -1), -4.005937216381782 * Math.pow(10, -1), -2.004893020126451 * Math.pow(10, -1)}, new double[]{2.136639566769687 * Math.pow(10, -2), -4.926299693858606 * Math.pow(10, -3), -4.847433078264927 * Math.pow(10, -3)});
        Body venus = new Body("Venus", Constants.MASS_VENUS, new double[]{-7.183022963460609 * Math.pow(10, -1), -4.627424668654580 * Math.pow(10, -2), 2.464063846247476 * Math.pow(10, -2)}, new double[]{7.981175152963302 * Math.pow(10, -4), -1.849183748109327 * Math.pow(10, -2), -8.369735338003694 * Math.pow(10, -3)});
        Body earth = new Body("Earth", Constants.MASS_EARTH, new double[]{-1.771350992582233E-01, 8.874285223279590E-01, 3.847428990892646E-01}, new double[]{-1.720762506877444E-02, -2.898167717339057E-03, -1.256395052081739E-03});
        Body moon = new Body("Moon", Constants.MASS_MOON, new double[]{-1.790843809082230E-01, 8.856456304154098E-01, 3.842341853827889E-01}, new double[]{-1.683595459153420E-02, -3.282865544577147E-03, -1.430425208820006E-03});
        Body mars = new Body("Mars", Constants.MASS_MARS, new double[]{1.390715921745722, 1.401217615185074 * Math.pow(10, -3), -3.696016720133020 * Math.pow(10, -2)}, new double[]{6.714995211622484 * Math.pow(10, -4), 1.381403751561449 * Math.pow(10, -2), 6.317900433307425 * Math.pow(10, -3)});
        Body jupiter = new Body("Jupiter", Constants.MASS_JUPITER, new double[]{4.001177161129903, 2.736578724016651, 1.075512280822026}, new double[]{-4.568313526744571 * Math.pow(10, -3), 5.881462129985139 * Math.pow(10, -3), 2.632303015927710 * Math.pow(10, -3)});
        Body saturn = new Body("Saturn", Constants.MASS_SATURN, new double[]{6.406408859536422, 6.174657792648269, 2.274770783704045}, new double[]{-4.292351873841176 * Math.pow(10, -3), 3.528344565973554 * Math.pow(10, -3), 1.641931519186532 * Math.pow(10, -3)});
        Body uranus = new Body("Uranus", Constants.MASS_URANUS, new double[]{1.443185661437953 * Math.pow(10, 1), -1.250626625900948 * Math.pow(10, 1), -5.681690059289905}, new double[]{2.678105591420612 * Math.pow(10, -3), 2.462004302669418 * Math.pow(10, -3), 1.040409448115117 * Math.pow(10, -3)});
        Body neptune = new Body("Neptune", Constants.MASS_NEPTUNE, new double[]{1.681204696805071 * Math.pow(10, 1), -2.298010050575052 * Math.pow(10, 1), -9.824427653613323}, new double[]{2.579274259047898 * Math.pow(10, -3), 1.668425282316235 * Math.pow(10, -3), 6.188152032294732 * Math.pow(10, -4)});
        Body pluto = new Body("Pluto", Constants.MASS_PLUTO, new double[]{-9.875352229926131E+00, -2.797886811916256E+01, -5.753691421761426E+00}, new double[]{3.028750846014184E-03, -1.127593278936559E-03, -1.265129364676574E-03});
        Body ceres = new Body("Ceres", Constants.MASS_CERES, new double[]{-2.379327705912632E+00, 5.456711318714194E-01, 7.412254807098526E-01}, new double[]{-3.584228273217894E-03, -9.845217307737637E-03, -3.904543826022410E-03});
        Body apophis = new Body("Apophis", 0, new double[]{-1.037925696098939E+00, -1.268092611036419E-01, -7.404282940432429E-02}, new double[]{4.227374301759195E-03, -1.412107207094790E-02, -5.145341154842345E-03});
        Body A2024YR4 = new Body("2024 YR4", 0, new double[]{-2.594982968163765E-01, -2.604198475987155E+00, -1.161962651207284E+00}, new double[]{7.551958208069564E-03, 5.004810090096610E-03, 2.674350852357362E-03});
        Body C3IATLAS = new Body("3I/ATLAS", 0, new double[]{1.253897637473739E+02, -2.721809832353439E+02, -1.037600160708741E+02}, new double[]{-1.340809972204520E-02, 2.869866907957716E-02, 1.095113063192082E-02});


        bodies.add(sun);
        bodies.add(mercury);
        bodies.add(venus);
        bodies.add(earth);
        bodies.add(mars);
        bodies.add(jupiter);
        bodies.add(saturn);
        bodies.add(uranus);
        bodies.add(neptune);
    }

    public double[] funcionYPrima(Body body, double h) {
        double[] ac = new double[3];
        for (int i = 0; i < bodies.size(); i++) {
            Body bodyLoop = bodies.get(i);
            if (bodyLoop.equals(body)) continue;
            double GMr3 = -Constants.G * body.getMass() / Math.pow(body.distance(bodyLoop), 3);
            for (int j = 0; j < 3; j++) {
                ac[j] = ac[j] + GMr3 * (bodyLoop.getPosition()[j] - body.getPosition()[j]);
            }
        }
        body.setAceleration(ac);
        return new double[]{ac[0] * h, ac[1] * h, ac[2] * h};
    }

    public void RK4(double a, double b, double N) {
        double h = (b - a) / N;
        double[] coefPosicion = new double[]{0, 0.5, 0.5, 1};
        double[] coefRunge = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};

        System.out.println("el valor de h es: " + h);
        for (int i = 0; i < N; i++) {
            double time = a + i * h;

            double[][][] k = new double[bodies.size()][coefRunge.length][3];
            double[][] dvel = new double[bodies.size()][3];
            double[][] dpos = new double[bodies.size()][3];
            for (int j = 0; j < coefPosicion.length; j++) {
                for (int l = 0; l < bodies.size(); l++) {
                    if (j > 0) {
                        for (int m = 0; m < 3; m++) {
                            dvel[l][m] = coefPosicion[j] * k[l][j - 1][m];
                            dpos[l][m] = dvel[l][m] * h;
                        }
                    }


                    Body bodyLoop = bodies.get(l);
                    bodyLoop.position[0] = bodyLoop.getPositionInitial()[0] + dpos[l][0];
                    bodyLoop.position[1] = bodyLoop.getPositionInitial()[1] + dpos[l][1];
                    bodyLoop.position[2] = bodyLoop.getPositionInitial()[2] + dpos[l][2];

                    k[l][j] = funcionYPrima(bodyLoop, h);
                    //System.out.println("  " + j + " " + k[j]);
                }
            }

            for (int l = 0; l < bodies.size(); l++) {
                Body bodyLoop = bodies.get(l);

                double[] dvelR = new double[3];
                for (int j = 0; j < coefRunge.length; j++) {
                    for (int t = 0; t < 3; t++) {
                        dvelR[t] += coefRunge[j] * k[l][j][t];
                    }

                }
                for (int t = 0; t < 3; t++) {
                    dvelR[t] += bodyLoop.getVelocitiesInitial()[t];
                }
                bodyLoop.setVelocitiesInitial(dvelR);

                double[] dposR = new double[3];
                double[] posF = new double[3];

                for (int j = 0; j < 3; j++) {
                    dposR[j] = dvelR[j] * h;
                    posF[j] = dposR[j] + bodyLoop.getPositionInitial()[j];
                }
                bodyLoop.setPositionInitial(posF);

            }

        }

        for (int l = 0; l < bodies.size(); l++) {
            Body bodyLoop = bodies.get(l);
            double[] p = bodyLoop.getPositionInitial();
            double[] v = bodyLoop.getVelocitiesInitial();
            System.out.println(bodyLoop.getName());
            System.out.println(p[0] + " " + p[1] + " " + p[2] + " " + v[0] + " " + v[1] + " " + v[2]);
        }
    }
}
