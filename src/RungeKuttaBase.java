public class RungeKuttaBase {

    public double funcionYPrima(double time, double position) {
        //return -2*position+Math.pow(time, 3.) * Math.exp(-2*time);
        return 2 * time * position;
    }


    public void RK4(double startingPoint, double endingPoint, double numIterations, double posInicial) {
        double step = (endingPoint - startingPoint) / numIterations;
        double[] coefPosicion = new double[]{0, 0.5, 0.5, 1};
        double[] coefRunge = new double[]{1 / 6., 1 / 3., 1 / 3., 1 / 6.};
        double pos = posInicial;

        System.out.println("el valor de h es: " + step);
        for (int i = 0; i < numIterations; i++) {
            double time = startingPoint + i * step;
            double[] k = new double[coefRunge.length];
            for (int j = 0; j < coefPosicion.length; j++) {
                double dpos = 0;
                if (j > 0) {
                    dpos = coefPosicion[j] * k[j - 1];
                }
                k[j] = funcionYPrima(time+, dpos+);
                System.out.println("  " + j + " " + k[j]);
            }

            double dpos = 0;
            for (int j = 0; j < k.length; j++) {
                dpos += coefRunge[j] * k[j];
            }
            pos += dpos;
            System.out.println(time + " " + pos);
        }
    }

}
}
