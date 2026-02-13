package prueba;

import java.util.ArrayList;
import java.util.List;

public class RungeKutta4 {
    List<Body> bodies = new ArrayList<>();

    public RungeKutta4() {
        addBodies();
    }

    public void funcionYPrima(Body body, double h) {
        double[] ac = new double[3];
        for (int i = 0; i < bodies.size(); i++) {
            Body bodyLoop = bodies.get(i);
            if (bodyLoop.equals(body)) continue;


            if (bodyLoop.getName().equals("Sun")) {
                double pv = 0, v2 = 0;
                for (int j = 0; j < 3; j++) {
                    double dp = body.getPosition(j) - bodyLoop.getPosition(j);
                    double dv = body.getVelocity(j) - bodyLoop.getVelocity(j);
                    v2 += dv * dv;
                    pv += dp * dv;
                }
                for (int j = 0; j < 3; j++) {
                    double a = (4 * Constants.G * (body.getPosition(j) - bodyLoop.getPosition(j)) / Math.pow(body.distance(bodyLoop), 4));
                    double b = (v2 * (body.getPosition(j) - bodyLoop.getPosition(j)) / Math.pow(body.distance(bodyLoop), 3));
                    double c = (4 * pv / Math.pow(body.distance(bodyLoop), 3)) * (body.getVelocity(j) - bodyLoop.getVelocity(j));
                    ac[j] += Constants.mu * (a - b + c);
                }
                //Metodo diferente
            }

            double GMr3 = -Constants.G * bodyLoop.getMass() / Math.pow(body.distance(bodyLoop), 3);
            for (int j = 0; j < 3; j++) {
                ac[j] = ac[j] + GMr3 * (body.getPosition(j) - bodyLoop.getPosition(j));
            }
        }
        body.setAceleration(ac);
    }

    public void RK4(double a, double b, double h) {
        double[] coefPosicion = new double[]{0, 0.5, 0.5, 1};
        double[] coefRunge = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};

        int N = (int) ((b - a) / h);
        System.out.println("el valor de h es: " + h);
        double time = 0;
        for (int i = 0; i < N + 1; i++) {
            time = a + i * h;
            if (i == N) {
                double lastH = b - time;
                if (lastH == 0) break;
                h = lastH;
            }

            double[][][] k = new double[bodies.size()][coefRunge.length][6];
            //double[][] dvel = new double[bodies.size()][3];
            //double[][] dpos = new double[bodies.size()][3];
            for (int j = 0; j < coefPosicion.length; j++) {
                if (j > 0) {
                    for (int l = 0; l < bodies.size(); l++) {
                        Body bodyLoop = bodies.get(l);
                        for (int m = 0; m < 3; m++) {
                            bodyLoop.position[m] = coefPosicion[j] * k[l][j - 1][m];
                            bodyLoop.velocities[m] = coefPosicion[j] * k[l][j - 1][m + 3];
                        }
                    }
                }

                for (int l = 0; l < bodies.size(); l++) {
                    Body bodyLoop = bodies.get(l);
                    funcionYPrima(bodyLoop, h);
                }

                for (int l = 0; l < bodies.size(); l++) {
                    Body bodyLoop = bodies.get(l);
                    for (int m = 0; m < 3; m++) {
                        k[l][j][m + 3] = bodyLoop.getAceleration()[m] * h;
                        k[l][j][m] = bodyLoop.getVelocity(m) * h;
                    }
                }
            }

            for (int l = 0; l < bodies.size(); l++) {
                Body bodyLoop = bodies.get(l);
                double[] p = bodyLoop.getPositionInitial();
                double[] v = bodyLoop.getVelocitiesInitial();
                for (int m = 0; m < 3; m++) {
                    double positionIncrement = 0;
                    double velIncrement = 0;
                    for (int j = 0; j < coefRunge.length; j++) {
                        positionIncrement += coefRunge[j] * k[l][j][m];
                        velIncrement += coefRunge[j] * k[l][j][m + 3];
                    }
                    p[m] += positionIncrement;
                    v[m] += velIncrement;

                    //bodyLoop.velocities[m] = bodyLoop.position[m] = 0; // Es mas rapido ?
                }
                bodyLoop.setPositionInitial(p);//por clarificar
                bodyLoop.setVelocitiesInitial(v);
                bodyLoop.position = new double[3];
                bodyLoop.velocities = new double[3];
            }
            time += h;
        }

        System.out.println("Time (integration end): " + time);

        for (int l = 0; l < bodies.size(); l++) {
            Body bodyLoop = bodies.get(l);
            double[] p = bodyLoop.getPositionInitial();
            double[] v = bodyLoop.getVelocitiesInitial();
            System.out.println(bodyLoop.getName());
            System.out.println(p[0] + " " + p[1] + " " + p[2] + " " + v[0] + " " + v[1] + " " + v[2]);
        }

        ra_dec();

    }

    private void addBodies() {
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


        bodies.add(sun);
        bodies.add(mercury);
        bodies.add(venus);
        bodies.add(earth);
        bodies.add(mars);
        bodies.add(jupiter);
        bodies.add(saturn);
        bodies.add(uranus);
        bodies.add(neptune);
        bodies.add(moon);
        bodies.add(pluto);
        bodies.add(apophis);
    }

    public void ra_dec() {
        //arctan(y/x)
        //arctan(z/h)
        double[][] dist_times = new double[bodies.size()-1][2];
        double[][] ra_dec = new double[bodies.size()-1][2];
        Body earth = bodies.get(3);
        for (int i = 0; i < bodies.size()-1; i++) {
            Body bodyLoop = bodies.get(i);
            if (!bodyLoop.equals(earth)) {
                dist_times[i][0] = earth.distance(bodyLoop);
                dist_times[i][1] = dist_times[i][0] / Constants.c;
                double[] pos = bodyLoop.getPositionInitial();
                double h = Math.sqrt(Math.pow(pos[0], 2)+Math.pow(pos[1], 2));
                ra_dec[i][0] = Math.atan(pos[0]/pos[1]);
                ra_dec[i][1] = Math.atan(pos[2]/h);
            }
            System.out.println(dist_times[i][1]);
        }
    }
}
