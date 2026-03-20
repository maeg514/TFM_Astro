package initialTest;

public class RungeKuttaBase {

    public double funcionYPrima(double time, double position) {
        //return -2*position+Math.pow(time, 3.) * Math.exp(-2*time);
        return 2 * time * position;
    }


    public void RK4(double startingPoint, double endingPoint, double numIterations, double initialPos) {
        double step = (endingPoint - startingPoint) / numIterations;
        double[] coefPosicion = new double[]{0, 0.5, 0.5, 1};
        double[] coefRunge = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};
        double position = initialPos;
        double time = startingPoint;

        System.out.println("el valor de h es: " + step);
        for (int i = 0; i < numIterations; i++) {
            time = startingPoint + step * i;// Es necesario volver a reasignar el valor del tiempo, para que no pierda precisión
            double[] k = new double[coefRunge.length];
            for (int j = 0; j < coefPosicion.length; j++) {
                double positionLoopK = 0;
                if (j > 0) {
                    positionLoopK = coefPosicion[j] * k[j - 1];
                }
                k[j] = funcionYPrima(time + coefPosicion[j] * step, position + positionLoopK) * step;
                System.out.println("  " + j + " " + k[j]);
            }

            double positionIncrement = 0;
            for (int j = 0; j < k.length; j++) {
                positionIncrement += coefRunge[j] * k[j];
            }
            position += positionIncrement;
            time += step;
            System.out.println(time + " " + position);
        }
    }

}

