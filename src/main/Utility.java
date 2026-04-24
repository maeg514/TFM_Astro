package main;

import java.util.List;

public class Utility {

    public static double dateToJulianDay(int year, int month, int day, int hour, int minute, boolean julian) {
        if (month < 3) {
            year = year - 1;
            month = month + 12;
        }
        int a = year / 100;
        int b = 0;
        double c = (hour + minute / 60.0) / 24.0;
        if (!julian) b = 2 - a + a / 4;
        return (int) (365.25 * (year + 4716)) + (int) (30.6001 * (month + 1)) + day + b - 1524.5 + c;
    }

    public static void arrayPrinter(List<Body> bodiesToPrint) {
        for (Body bodyLoop : bodiesToPrint) {
            double[] p = bodyLoop.getPositionInitial();
            double[] v = bodyLoop.getVelocityInitial();
            System.out.println(bodyLoop.getName());
            System.out.println(p[0] + " " + p[1] + " " + p[2] + " " + v[0] + " " + v[1] + " " + v[2]);
        }
    }

    public static String matrixToString(double[][] matriz) {
        StringBuilder sb = new StringBuilder();

        for (double[] fila : matriz) {
            for (double valor : fila) {
                sb.append(String.format("%10.10f", valor)).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static double[][] bodiesIntoArray(List<Body> bodies) {
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


}
