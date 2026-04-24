package main.efficiencyOriented;

import main.Constants;
import main.Body;

import java.util.List;
/**
 * @deprecated Esta clase ha sido reemplazada por {@link RungeKutta}.
 * Usa RungeKutta para nuevas implementaciones.
 */
@Deprecated()
public class RK4 {
    static final int RK_ORDER = 4;
    List<Body> bodies;
    final int NUMBER_OF_BODIES;
    double[][] pos_vel_Initial;
    double[][][] pos_vel_acc_Integration;

    public RK4(List<Body> bodies) {
        this.bodies = bodies;
        this.NUMBER_OF_BODIES = bodies.size();
        this.pos_vel_Initial = bodiesIntoArray(bodies);
        this.pos_vel_acc_Integration = new double[NUMBER_OF_BODIES][3][3];
    }


    public void RK4(double a, double b, double h) {
        double[] coefPosicion = new double[]{0, 0.5, 0.5, 1}; // API Matriz
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
            double[][][] delta_PosVel = new double[NUMBER_OF_BODIES][2][3];

            for (int j = 0; j < RK_ORDER; j++) {

                for (int l = 0; l < bodies.size(); l++) {
                    for (int m = 0; m < 3; m++) {
                        if (j > 0) {
                            delta_PosVel[l][0][m] = coefPosicion[j] * k[l][j - 1][m];
                            delta_PosVel[l][1][m] = coefPosicion[j] * k[l][j - 1][m + 3];
                        } else {
                            delta_PosVel[l][0][m] = 0;
                            delta_PosVel[l][1][m] = 0;
                        }

                        pos_vel_acc_Integration[l][0][m] = pos_vel_Initial[l][m] + delta_PosVel[l][0][m];
                        pos_vel_acc_Integration[l][1][m] = pos_vel_Initial[l][m + 3] + delta_PosVel[l][1][m];
                    }
                    //System.out.println(Arrays.toString(delta_PosVel[l][0]));

                }


                for (int l = 0; l < bodies.size(); l++) {
                    funcionYPrima(l, h);
                }

                for (int l = 0; l < bodies.size(); l++) {
                    for (int m = 0; m < 3; m++) {
                        k[l][j][m + 3] = pos_vel_acc_Integration[l][2][m] * h;
                        k[l][j][m] = pos_vel_acc_Integration[l][1][m] * h;
                        //System.out.println(Arrays.toString(pos_vel_acc_Integration[l][2]) + " a ");
                        //System.out.println(Arrays.toString(pos_vel_acc_Integration[l][1]) + "v ");

                    }
                    //System.out.println(Arrays.toString(k[l][j]));
                }

                delta_PosVel = new double[NUMBER_OF_BODIES][2][3];

            }


            for (int l = 0; l < bodies.size(); l++) {
                for (int m = 0; m < 3; m++) {
                    double positionIncrement = 0;
                    double velIncrement = 0;
                    for (int j = 0; j < coefRunge.length; j++) {
                        positionIncrement += coefRunge[j] * k[l][j][m];
                        velIncrement += coefRunge[j] * k[l][j][m + 3];
                        //System.out.println(positionIncrement + " p " + coefRunge[j] + " / " + k[l][j][m]);
                        //System.out.println(velIncrement + "v " + k[l][j][m + 3]);
                    }
                    pos_vel_Initial[l][m] += positionIncrement;
                    pos_vel_Initial[l][m + 3] += velIncrement;

                }
            }


            time += h;


        }

        System.out.println("Time (integration end): " + time);

    }


    public void RK4_v2(double a, double b, double h) {
        double[][] coefPosicion = new double[][]{{1 / 3.}, {-1 / 3., 1}, {1, -1, 1}}; // API Matriz
        double[] coefRunge = new double[]{1 / 8., 3 / 8., 3 / 8., 1 / 8.};
        //double[][] coefPosicion = new double[][]{{1 / 2.}, {0, 1 / 2.}, {0, 0, 1}}; // API Matriz
        //double[] coefRunge = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};


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

            double[][][] k = new double[NUMBER_OF_BODIES][coefRunge.length][6];


            for (int j = 0; j < RK_ORDER; j++) {
                double[][] delta_PosVel = new double[NUMBER_OF_BODIES][6];
                for (int l = 0; l < bodies.size(); l++) {
                    for (int m = 0; m < 3; m++) {
                        if (j > 0) {
                            for (int n = 0; n < j; n++) {
                                delta_PosVel[l][m] += coefPosicion[j - 1][n] * k[l][n][m];
                                //System.out.println(coefPosicion[j - 1][n] + " * " + k[l][n][m]);
                                delta_PosVel[l][m + 3] += coefPosicion[j - 1][n] * k[l][n][m + 3];
                            }
                        } else {
                            delta_PosVel[l][m] = 0;
                            delta_PosVel[l][m + 3] = 0;
                        }

                        pos_vel_acc_Integration[l][0][m] = pos_vel_Initial[l][m] + delta_PosVel[l][m];
                        pos_vel_acc_Integration[l][1][m] = pos_vel_Initial[l][m + 3] + delta_PosVel[l][m + 3];
                    }
                    //System.out.println(Arrays.toString(delta_PosVel[l][0]));

                }


                for (int l = 0; l < bodies.size(); l++) {
                    funcionYPrima(l, h);
                }

                for (int l = 0; l < bodies.size(); l++) {
                    for (int m = 0; m < 3; m++) {
                        k[l][j][m + 3] = pos_vel_acc_Integration[l][2][m] * h;
                        k[l][j][m] = pos_vel_acc_Integration[l][1][m] * h;
                        //System.out.println(Arrays.toString(pos_vel_acc_Integration[l][2]) + " a ");
                        //System.out.println(Arrays.toString(pos_vel_acc_Integration[l][1]) + "v ");

                    }
                    //System.out.println(Arrays.toString(k[l][j]));
                }

            }


            for (int l = 0; l < bodies.size(); l++) {
                for (int m = 0; m < 3; m++) {
                    double positionIncrement = 0;
                    double velIncrement = 0;
                    for (int j = 0; j < coefRunge.length; j++) {
                        positionIncrement += coefRunge[j] * k[l][j][m];
                        velIncrement += coefRunge[j] * k[l][j][m + 3];
                        //System.out.println(positionIncrement + " p " + coefRunge[j] + " / " + k[l][j][m]);
                        //System.out.println(velIncrement + "v " + k[l][j][m + 3]);
                    }
                    pos_vel_Initial[l][m] += positionIncrement;
                    pos_vel_Initial[l][m + 3] += velIncrement;

                }
            }


            time += h;


        }

        System.out.println("Time (integration end): " + time);

    }

    public void funcionYPrima(int l, double h) {
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

            if (bodyLoop.getName().equals("Earth") && (body.getName().equals("Moon") || body.getName().equals("Apophis"))) {
                earthsFlatteningFactor(posBody, posBodyLoop, accBody, distance);
            }

            double GMr3 = -Constants.G * bodyLoop.getMass() / Math.pow(distance, 3);
            for (int j = 0; j < 3; j++) {
                accBody[j] += GMr3 * (posBody[j] - posBodyLoop[j]);
            }
            pos_vel_acc_Integration[l][2] = accBody;
        }


    }

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
        double factor = -Constants.J2 / Math.pow(d, 7);
        double factor3 = (Math.pow(vectorAux[0], 2) + Math.pow(vectorAux[1], 2));
        double factor2 = 6 * Math.pow(vectorAux[2], 2) - 1.5 * factor3;

        ac[0] += factor * vectorAux[0] * factor2;
        ac[1] += factor * vectorAux[1] * factor2;
        ac[2] += factor * vectorAux[2] * (3 * Math.pow(vectorAux[2], 2) - 4.5 * factor3);
    }

    // Calculates accelerations on Apophis due to non-gravitational forces, see Marsden et al. (1973), Astron. J. 78, 211-225.
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
        double a2 = -2.901766720242E-14 / Math.sqrt(tx * tx + ty * ty + tz * tz);

        // X, Y and Z components of non-gravitational acceleration
        ac[0] -= (a1 * posAux[0] + a2 * tx) / r2;//Corregido esto, estaba mal el signo
        ac[1] -= (a1 * posAux[1] + a2 * ty) / r2;
        ac[2] -= (a1 * posAux[2] + a2 * tz) / r2;
    }

    private double[][] bodiesIntoArray(List<Body> bodies) {
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

    private double distanceBetweenBodies(int l, int b) {
        double r_2 = 0;
        double[] posBody = pos_vel_acc_Integration[l][0];
        double[] posBodyLoop = pos_vel_acc_Integration[b][0];

        for (int i = 0; i < posBody.length; i++) {
            r_2 += (posBody[i] - posBodyLoop[i]) * (posBody[i] - posBodyLoop[i]);
        }
        return Math.sqrt(r_2);
    }

    public double[][] getPos_vel_Initial() {
        return pos_vel_Initial;
    }
}
