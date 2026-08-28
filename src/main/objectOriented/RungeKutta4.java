package main.objectOriented;

import main.Body;
import main.Constants;
import main.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RungeKutta4 {
    List<Body> bodies = new ArrayList<>();

    public RungeKutta4() {
        addBodies();
    }

    public void funcionYPrima(Body body, double h) {
        double[] ac = new double[3];
        double[] posBody = body.getPosition();
        for (Body bodyLoop : bodies) {
            double distance = body.distance(bodyLoop);
            double[] posBodyLoop = bodyLoop.getPosition();

            if (bodyLoop.equals(body)) continue;


            if (bodyLoop.getName().equals("Sun")) {
                relativisticAcceleration(body, bodyLoop, ac);
                if (body.getName().equals("Apophis")){
                    apophisNGF(body,bodyLoop,ac);
                }
            }
            if (bodyLoop.getName().equals("Earth") && (body.getName().equals("Moon") || body.getName().equals("Apophis"))) {
                earthsFlatteningFactor(body, bodyLoop, ac);
            }

            double GMr3 = -main.Constants.G * bodyLoop.getMass() / Math.pow(distance, 3);
            for (int j = 0; j < 3; j++) {
                ac[j] += GMr3 * (posBody[j] - posBodyLoop[j]);
            }
        }
        body.setAcceleration(ac);
    }

    public void RK4(double a, double b, double h) {
        double[] coefPosicion = new double[]{0, 0.5, 0.5, 1};
        double[] coefRunge = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};

        int N = (int) ((b - a) / h);
        System.out.println("el valor de h es: " + h);//Lanzar excepcion
        double time = 0;
        for (int i = 0; i < N + 1; i++) {
            time = a + i * h;
            if (i == N) {
                double lastH = b - time;
                if (lastH == 0) break;
                h = lastH;
            }

            double[][][] k = new double[bodies.size()][coefRunge.length][6];

            for (int j = 0; j < coefPosicion.length; j++) {
                if (j > 0) {
                    for (int l = 0; l < bodies.size(); l++) {
                        Body bodyLoop = bodies.get(l);
                        double[] posBody = bodyLoop.getPosition();
                        double[] velBody = bodyLoop.getVelocity();
                        for (int m = 0; m < 3; m++) {
                            posBody[m] = coefPosicion[j] * k[l][j - 1][m];
                            velBody[m] = coefPosicion[j] * k[l][j - 1][m + 3];
                        }
                        bodyLoop.setPositionDelta(posBody);
                        bodyLoop.setVelocity(velBody);
                    }
                }

                for (int l = 0; l < bodies.size(); l++) {
                    Body bodyLoop = bodies.get(l);
                    funcionYPrima(bodyLoop, h);
                }

                for (int l = 0; l < bodies.size(); l++) {
                    Body bodyLoop = bodies.get(l);
                    for (int m = 0; m < 3; m++) {
                        k[l][j][m + 3] = bodyLoop.getAcceleration()[m] * h;
                        k[l][j][m] = bodyLoop.getVelocity(m) * h;
                    }
                }
            }

            for (int l = 0; l < bodies.size(); l++) {
                Body bodyLoop = bodies.get(l);
                double[] p = bodyLoop.getPositionInitial();
                double[] v = bodyLoop.getVelocityInitial();
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
                bodyLoop.setVelocityInitial(v);
                bodyLoop.positionDelta = new double[3];
                bodyLoop.velocityDelta = new double[3];
            }
            time += h;
        }

        System.out.println("Time (integration end): " + time);

        String approveChanges = "y";
        /*if (changesChecking()){
            System.out.println("¿Desea actualizar los valores de los cuerpos? (y/n)");
            approveChanges = teclado.next();
        }*/

        if (approveChanges.equals("y")){
            for (Body bodyLoop : bodies) {
                double[] p = bodyLoop.getPositionInitial();
                double[] v = bodyLoop.getVelocityInitial();
                System.out.println(bodyLoop.getName());
                System.out.println(p[0] + " " + p[1] + " " + p[2] + " " + v[0] + " " + v[1] + " " + v[2]);
            }
            ra_dec();
            vectorGeocentric();
            //changesChecking();
        }
    }

    private void relativisticAcceleration(Body body, Body bodyLoop, double[] ac) {
        double pv = 0, v2 = 0;
        double[] posBody = body.getPosition();
        double[] velBody = body.getVelocity();
        double[] posBodyLoop = bodyLoop.getPosition();
        double[] velBodyLoop = bodyLoop.getVelocity();
        double d = body.distance(bodyLoop);
        for (int j = 0; j < 3; j++) {
            double dp = posBody[j] - posBodyLoop[j];
            double dv = velBody[j] - velBodyLoop[j];
            v2 += dv * dv;
            pv += dp * dv;
        }
        for (int j = 0; j < 3; j++) {
            double a = (4 * main.Constants.G * (posBody[j] - posBodyLoop[j]) / Math.pow(d, 4));
            double b = (v2 * (posBody[j] - posBodyLoop[j]) / Math.pow(d, 3));
            double c = (4 * pv / Math.pow(d, 3)) * (velBody[j] - velBodyLoop[j]);
            ac[j] += Constants.mu * (a - b + c);
        }
    }

    private void earthsFlatteningFactor(Body body, Body bodyLoop, double[] ac) {
        double[] vectorAux = new double[3];
        double[] posBody = body.getPosition();
        double[] posBodyLoop = bodyLoop.getPosition();
        for (int i = 0; i < 3; i++) {
            vectorAux[i] = posBodyLoop[i] - posBody[i];
        }
        double factor = -main.Constants.J2 / Math.pow(body.distance(bodyLoop), 7);
        ac[0] += factor * vectorAux[0] * (6 * Math.pow(vectorAux[2], 2) - 1.5 * (Math.pow(vectorAux[0], 2) + Math.pow(vectorAux[1], 2)));
        ac[1] += factor * vectorAux[1] * (6 * Math.pow(vectorAux[2], 2) - 1.5 * (Math.pow(vectorAux[0], 2) + Math.pow(vectorAux[1], 2)));
        ac[2] += factor * vectorAux[2] * (3 * Math.pow(vectorAux[2], 2) - 4.5 * (Math.pow(vectorAux[0], 2) + Math.pow(vectorAux[1], 2)));
    }

    // Calculates accelerations on Apophis due to non-gravitational forces, see Marsden et al. (1973), Astron. J. 78, 211-225.
    private void apophisNGF(Body body, Body bodyLoop, double[] ac) {
        double[] posAux = new double[3];
        double[] velAux = new double[3];
        double[] posBody = body.getPosition();
        double[] posBodyLoop = bodyLoop.getPosition();
        double[] velBody = body.getVelocity();
        double[] velBodyLoop = bodyLoop.getVelocity();
        double pv = 0;
        for (int i = 0; i < 3; i++) {
            posAux[i] = posBodyLoop[i] - posBody[i];
            velAux[i] = velBodyLoop[i] - velBody[i];
            pv += posAux[i] * velAux[i];
        }

        // Within-orbital-plane transverse vector components
        double r = body.distance(bodyLoop);
        double r2 = r * r;
        double tx = r2 * velAux[0] - pv * posAux[0];
        double ty = r2 * velAux[1] - pv * posAux[1];
        double tz = r2 * velAux[2] - pv * posAux[2];

        // Multiplication factors. NGF (A) values are read in AU/s^2. a3 = 0 for Apophis
        double a1 = 4.999999873689E-13 / r;
        double a2 = -2.901766720242E-14 / Math.sqrt(tx * tx + ty * ty + tz * tz);

        // X, Y and Z components of non-gravitational acceleration
        ac[0] -= (a1 * posAux[0] + a2 * tx) / r2;//Corregido esto, estaba mal el signo
        ac[1] -= (a1 * posAux[1] + a2 * ty) / r2;
        ac[2] -= (a1 * posAux[2] + a2 * tz) / r2;
    }

    public void ra_dec() {//escoger body, astrometric
        //arctan(y/x)
        //arctan(z/h)
        Body earth = bodies.get(3);
        for (Body skyObject : bodies) {
            if (skyObject.equals(earth)) continue;
            earth.ra_dec(skyObject, null);
        }
    }

    public void ra_dec_Observer(double jd_ut, double ttMinusUT, double obsLon, double obsLat, double obsAlt){
        obsLon *= main.Constants.DEG_TO_RAD;
        obsLat *= main.Constants.DEG_TO_RAD;

        Body earth = bodies.get(3);
        for (Body skyObject : bodies) {
            if (skyObject.equals(earth)) continue;
            double[] vectorObserver = Utility.vectorObserver(jd_ut, ttMinusUT, obsLon, obsLat, obsAlt);
            earth.ra_dec(skyObject, vectorObserver);
        }
    }

    public void vectorGeocentric() {//geometric
        Body earth = bodies.get(3);
        for (Body skyObject : bodies) {
            if (skyObject.equals(earth)) continue;
            double[] p = earth.distanceVector(skyObject);
            double[] v = earth.relativeVelocityVector(skyObject);
            System.out.println(skyObject.getName());
            System.out.println(p[0] + " " + p[1] + " " + p[2] + " " + v[0] + " " + v[1] + " " + v[2]);
        }
    }


    private void addBodies() {
        Body sun = new Body("Sun", main.Constants.MASS_SUN, new double[]{0, 0, 0}, new double[]{0, 0, 0});
        Body mercury = new Body("Mercury", main.Constants.MASS_MERCURY, new double[]{-0.1300936053754522, -0.40059372164232543, -0.20048930201672596}, new double[]{0.021366395668016163, -0.004926299692875428, -0.004847433077772866});
        Body venus = new Body("Venus", main.Constants.MASS_VENUS, new double[]{-0.718302296345389, -0.04627424670211335, 0.02464063845542861}, new double[]{7.981175157753219E-4, -0.018491837481062413, -0.008369735338020125});
        Body earth = new Body("Earth", main.Constants.MASS_EARTH, new double[]{-1.771350992727098E-01, 8.874285223255191E-01, 3.847428990882070E-01}, new double[]{-1.720762506872895E-02, -2.898167717572411E-03, -1.256395052182784E-03});
        Body moon = new Body("Moon", main.Constants.MASS_MOON, new double[]{-1.790843809223965E-01, 8.856456304126460E-01, 3.842341853815847E-01}, new double[]{-1.683595459141215E-02, -3.282865544741707E-03, -1.430425208901575E-03});
        Body mars = new Body("Mars", main.Constants.MASS_MARS, new double[]{1.390715921746287, 0.001401217626814569, -0.036960167196011424}, new double[]{6.71499521033585E-4, 0.013814037515614361, 0.006317900433310847});
        Body jupiter = new Body("Jupiter", main.Constants.MASS_JUPITER, new double[]{4.001177161126057, 2.7365787240216024, 1.0755122808242419}, new double[]{-0.004568313526752718, 0.005881462129979568, 0.0026323030159255195});
        Body saturn = new Body("Saturn", main.Constants.MASS_SATURN, new double[]{6.406408859532808, 6.174657792651239, 2.274770783705428}, new double[]{-0.00429235187384325, 0.0035283445659715523, 0.0016419315191857945});
        Body uranus = new Body("Uranus", main.Constants.MASS_URANUS, new double[]{14.431856614381783, -12.506266259007408, -5.681690059289029}, new double[]{0.00267810559142015, 0.002462004302669807, 0.0010404094481152937});
        Body neptune = new Body("Neptune", main.Constants.MASS_NEPTUNE, new double[]{16.812046968052883, -22.980100505749114, -9.824427653612803}, new double[]{0.002579274259047737, 0.001668425282316438, 6.188152032295604E-4});
        Body pluto = new Body("Pluto", main.Constants.MASS_PLUTO, new double[]{-9.87535222992358, -27.978868119163504, -5.753691421762491}, new double[]{0.0030287508460142658, -0.001127593278936313, -0.001265129364676525});
        Body ceres = new Body("Ceres", main.Constants.MASS_CERES, new double[]{-2.379327705912632E+00, 5.456711318714194E-01, 7.412254807098526E-01}, new double[]{-3.584228273217894E-03, -9.845217307737637E-03, -3.904543826022410E-03});
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
}
