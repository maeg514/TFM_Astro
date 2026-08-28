package main.efficiencyOriented;

import main.Constants;
import main.Body;

import java.util.List;

public class RungeKutta {
    final int RK_STAGES;
    List<Body> bodies;
    final int NUMBER_OF_BODIES;
    double[][] pos_vel_Initial;
    double[][][] pos_vel_acc_Integration;
    final double[][] positionCoefficients;
    final double[] rkCoefficients;


    public RungeKutta(List<Body> bodies, double[][] coefPosition, double[] coefRK) {
        this.RK_STAGES = coefRK.length;
        this.bodies = bodies;
        this.NUMBER_OF_BODIES = bodies.size();
        this.pos_vel_Initial = bodiesIntoArray(bodies);
        this.pos_vel_acc_Integration = new double[NUMBER_OF_BODIES][3][3];
        this.positionCoefficients = coefPosition;
        this.rkCoefficients = coefRK;
    }


    public void RK(double a, double b, double h) {
        double[][] coefPosicion = positionCoefficients;
        double[] coefRunge = rkCoefficients;

        int N = (int) ((b - a) / h);
        double lastH = (b - a) % h;

        System.out.println("el valor de h es: " + h); // TODO Lanzar excepción y poner en un método el cálculo de N
        double time = 0;

        for (int i = 0; i < N + 1; i++) {

            time = a + N * h;

            if (i == N) {
                if (lastH == 0) break;
                h = lastH;
            }

            double[][][] k = new double[NUMBER_OF_BODIES][coefRunge.length][6];


            for (int j = 0; j < RK_STAGES; j++) {
                double[][] delta_PosVel = new double[NUMBER_OF_BODIES][6];
                for (int l = 0; l < bodies.size(); l++) {
                    for (int m = 0; m < 3; m++) {
                        if (j > 0) {
                            for (int n = 0; n < j; n++) {
                                delta_PosVel[l][m] += coefPosicion[j - 1][n] * k[l][n][m];
                                delta_PosVel[l][m + 3] += coefPosicion[j - 1][n] * k[l][n][m + 3];
                            }
                        } else {
                            delta_PosVel[l][m] = 0;
                            delta_PosVel[l][m + 3] = 0;
                        }

                        pos_vel_acc_Integration[l][0][m] = pos_vel_Initial[l][m] + delta_PosVel[l][m];
                        pos_vel_acc_Integration[l][1][m] = pos_vel_Initial[l][m + 3] + delta_PosVel[l][m + 3];
                    }

                }


                for (int l = 0; l < bodies.size(); l++) {
                    funcionYPrima(l);
                }

                for (int l = 0; l < bodies.size(); l++) {
                    for (int m = 0; m < 3; m++) {
                        k[l][j][m + 3] = pos_vel_acc_Integration[l][2][m] * h;
                        k[l][j][m] = pos_vel_acc_Integration[l][1][m] * h;
                    }
                }

            }


            for (int l = 0; l < bodies.size(); l++) {
                for (int m = 0; m < 3; m++) {
                    double positionIncrement = 0;
                    double velIncrement = 0;
                    for (int j = 0; j < coefRunge.length; j++) {
                        positionIncrement += coefRunge[j] * k[l][j][m];
                        velIncrement += coefRunge[j] * k[l][j][m + 3];
                    }
                    pos_vel_Initial[l][m] += positionIncrement;
                    pos_vel_Initial[l][m + 3] += velIncrement;

                }
            }


            time += h;


        }

        System.out.println("Time (integration end): " + time);

    }

    public void funcionYPrima(int l) {
        Body body = bodies.get(l);
        double[] posBody = pos_vel_acc_Integration[l][0];
        double[] accBody = new double[3];
        for (int b = 0; b < bodies.size(); b++) {
            Body bodyLoop = bodies.get(b);
            double[] posBodyLoop = pos_vel_acc_Integration[b][0];
            double distance = distanceBetweenBodies(l, b);


            if (bodyLoop.equals(body)) continue;


            if (bodyLoop.getName().equals("Sun")) {
                relativisticAcceleration(l, b, accBody, distance);
                if (body.getName().equals("Apophis")) {
                    apophisNGF(l, b, accBody, distance);
                }
            }

            if (bodyLoop.getName().equals("Earth") && (body.getName().equals("Moon") || body.getName().equals("2024 YR4"))) {
                earthsFlatteningFactor(posBody, posBodyLoop, accBody, distance);
            }

            double GMr3 = -Constants.G * bodyLoop.getMass() / Math.pow(distance, 3);
            for (int j = 0; j < 3; j++) {
                accBody[j] += GMr3 * (posBody[j] - posBodyLoop[j]);
            }
            pos_vel_acc_Integration[l][2] = accBody;
        }


    }

    /**
     * Calculates relativistic accelerations for bodies interacting with the Sun.
     *
     * @param l Index number for the affected body.
     * @param b Index number for the Sun.
     * @param ac Actual value for the acceleration of the minor body.
     * @param d Distance between the asteroid and the Sun.
     */
    private void relativisticAcceleration(int l, int b, double[] ac, double d) {
        double pv = 0, v2 = 0;
        double[] posBody = pos_vel_acc_Integration[l][0];
        double[] velBody = pos_vel_acc_Integration[l][1];
        double[] posBodyLoop = pos_vel_acc_Integration[b][0];
        double[] velBodyLoop = pos_vel_acc_Integration[b][1];
        for (int j = 0; j < 3; j++) {
            double dp = posBody[j] - posBodyLoop[j];
            double dv = velBody[j] - velBodyLoop[j];
            v2 += dv * dv;
            pv += dp * dv;
        }
        for (int j = 0; j < 3; j++) {
            double a = (4 * Constants.G * (posBody[j] - posBodyLoop[j]) / Math.pow(d, 4));
            double bb = (v2 * (posBody[j] - posBodyLoop[j]) / Math.pow(d, 3));
            double c = (4 * pv / Math.pow(d, 3)) * (velBody[j] - velBodyLoop[j]);
            ac[j] += Constants.mu * (a - bb + c);
        }
    }

    private void earthsFlatteningFactor(double[] posBody, double[] posBodyLoop, double[] ac, double d) {
        double[] vectorAux = new double[3];
        for (int i = 0; i < 3; i++) {
            vectorAux[i] = posBodyLoop[i] - posBody[i];
        }
        double global_factor = -Constants.J2 / Math.pow(d, 7);
        double factor_xy_squared_sum = (Math.pow(vectorAux[0], 2) + Math.pow(vectorAux[1], 2));
        double final_factor_xy = 6 * Math.pow(vectorAux[2], 2) - 1.5 * factor_xy_squared_sum;

        ac[0] += global_factor * vectorAux[0] * final_factor_xy;
        ac[1] += global_factor * vectorAux[1] * final_factor_xy;
        ac[2] += global_factor * vectorAux[2] * (3 * Math.pow(vectorAux[2], 2) - 4.5 * factor_xy_squared_sum);
    }


    /**
     * Calculates accelerations on Apophis due to non-gravitational forces, see Marsden et al. (1973), Astron. J. 78, 211-225.
     *
     * @param l Index number for the asteroid.
     * @param b Index number for the Sun.
     * @param ac Actual value for the acceleration of the minor body.
     * @param r Distance between the asteroid and the Sun.
     */
    private void apophisNGF(int l, int b, double[] ac, double r) {
        double[] posAux = new double[3];
        double[] velAux = new double[3];
        double[] posBody = pos_vel_acc_Integration[l][0];
        double[] posBodyLoop = pos_vel_acc_Integration[b][0];
        double[] velBody = pos_vel_acc_Integration[l][1];
        double[] velBodyLoop = pos_vel_acc_Integration[b][1];
        double pv = 0;
        for (int i = 0; i < 3; i++) {
            posAux[i] = posBodyLoop[i] - posBody[i];
            velAux[i] = velBodyLoop[i] - velBody[i];
            pv += posAux[i] * velAux[i];
        }

        // Within-orbital-plane transverse vector components
        double r2 = r * r;
        double tx = r2 * velAux[0] - pv * posAux[0];
        double ty = r2 * velAux[1] - pv * posAux[1];
        double tz = r2 * velAux[2] - pv * posAux[2];

        // Multiplication factors. NGF (A) values are read in AU/s^2. a3 = 0 for Apophis
        double a1 = 4.999999873689E-13 / r;
        double a2 = -2.901766720242E-14 / Math.sqrt(tx * tx + ty * ty + tz * tz); //Distinto valor que JPL // no hay valores para 2024 YR4?

        // X, Y and Z components of non-gravitational acceleration
        ac[0] -= (a1 * posAux[0] + a2 * tx) / r2;//Corregido esto, estaba mal el signo
        ac[1] -= (a1 * posAux[1] + a2 * ty) / r2;
        ac[2] -= (a1 * posAux[2] + a2 * tz) / r2;
    }

    /**
     * Receives a list of bodies and saves them in an array for the efficiency of the RK method.
     *
     * @param bodies List of bodies that participate in the numerical integration of this Java class.
     * @return Array containing the initial values for position and velocity.
     */
    public double[][] bodiesIntoArray(List<Body> bodies) {
        double[][] initial_posVel = new double[bodies.size()][6];
        for (int i = 0; i < bodies.size(); i++) {
            double[] pos = bodies.get(i).getPositionInitial().clone();
            double[] vel = bodies.get(i).getVelocityInitial().clone();

            for (int j = 0; j < 3; j++) {
                initial_posVel[i][j] = pos[j];
                initial_posVel[i][j + 3] = vel[j];
            }
        }
        return initial_posVel;
    }

    /**
     * Introduces the values calculated in the RK method to the corresponding bodies.
     */
    public void arrayIntoBodies(){
        for (int i = 0; i < pos_vel_Initial.length; i++) {
            Body bodyLoop = bodies.get(i);
            double[] posUpdate = new double[3];
            double[] velUpdate = new double[3];
            for (int j = 0; j < posUpdate.length; j++) {
                posUpdate[j] = pos_vel_Initial[i][j];
                velUpdate[j] = pos_vel_Initial[i][j+3];
            }
            bodyLoop.setPositionInitial(posUpdate);
            bodyLoop.setVelocityInitial(velUpdate);
        }
    }

    /**
     * Calculates the distance between two bodies.
     *
     * @param l Index number for the body distanced.
     * @param b Index number for the body which is the center of the coordinates.
     * @return Distance in AU.
     */
    private double distanceBetweenBodies(int l, int b) {
        double r_2 = 0;
        double[] posBody = pos_vel_acc_Integration[l][0];
        double[] posBodyLoop = pos_vel_acc_Integration[b][0];

        for (int i = 0; i < posBody.length; i++) {
            r_2 += (posBody[i] - posBodyLoop[i]) * (posBody[i] - posBodyLoop[i]);
        }
        return Math.sqrt(r_2);
    }


    /**
     * Returns the list of bodies used in the integration.
     *
     * @return List of bodies.
     */
    public List<Body> getBodies() {
        return bodies;
    }
}



