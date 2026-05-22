package main;

public class Body {
    private final String name;
    private final double mass;
    private double[] positionInitial;
    private double[] velocityInitial;
    private double[] acceleration;
    public double[] positionDelta;
    public double[] velocityDelta;

    public Body(String name, double mass, double[] positionInitial, double[] velocityInitial) {
        this.name = name;
        this.mass = mass;
        this.positionInitial = positionInitial;
        this.velocityInitial = velocityInitial;
        this.acceleration = new double[3];
        this.positionDelta = new double[3];
        this.velocityDelta = new double[3];
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double[] getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double[] acceleration) {
        this.acceleration = acceleration;
    }

    public double[] getPositionInitial() {
        return positionInitial;
    }

    public double[] getVelocityInitial() {
        return velocityInitial;
    }

    public void setVelocityInitial(double[] velocityInitial) {
        this.velocityInitial = velocityInitial;
    }

    public void setPositionInitial(double[] positionInitial) {
        this.positionInitial = positionInitial;
    }

    public void setVelocity(double[] velocityDelta) {
        this.velocityDelta = velocityDelta;
    }

    public void setPositionDelta(double[] positionDelta) {
        this.positionDelta = positionDelta;
    }

    public double getPosition(int j) {
        return positionInitial[j] + positionDelta[j];
    }

    public double[] getPosition() {
        double[] pos = new double[3];
        for (int i = 0; i < 3; i++) {
            pos[i] = positionInitial[i] + positionDelta[i];
        }
        return pos;
    }

    public double getVelocity(int j) {
        return velocityInitial[j] + velocityDelta[j];
    }

    public double[] getVelocity() {
        double[] vel = new double[3];
        for (int i = 0; i < 3; i++) {
            vel[i] = velocityInitial[i] + velocityDelta[i];
        }
        return vel;
    }

    public double distance(Body body) {
        double r_2 = 0;
        for (int i = 0; i < positionDelta.length; i++) {
            r_2 += Math.pow(this.getPosition(i) - body.getPosition(i), 2);
        }
        return Math.sqrt(r_2);
    }


    public double[] distanceVector(Body body) {
        double[] vectorDistance = new double[3];
        double[] vectorBody = body.getPositionInitial();
        for (int i = 0; i < positionDelta.length; i++) {
            vectorDistance[i] += vectorBody[i] - this.positionInitial[i];
        }
        return vectorDistance;
    }

    public double[] distanceVector(double[] skyPosition) {
        double[] vectorDistance = new double[3];
        for (int i = 0; i < positionDelta.length; i++) {
            vectorDistance[i] = skyPosition[i] - this.positionInitial[i];
        }
        return vectorDistance;
    }

    public double[] relativeVelocityVector(Body body) {
        double[] vectorVelocity = new double[3];
        double[] vectorBody = body.getVelocityInitial();
        for (int i = 0; i < positionDelta.length; i++) {
            vectorVelocity[i] += vectorBody[i] - this.velocityInitial[i];
        }
        return vectorVelocity;
    }

    public double[] ra_dec(Body body, double[] obsPos) {
        double ra, dec, distance;

        double[] skyPosition = body.getPositionInitial().clone();
        skyPosition = distanceVector(skyPosition);
        double[] velocity = body.getVelocityInitial();

        //Obspos is the topocentric correction for the position
        if (obsPos != null) {
            for (int i = 0; i < skyPosition.length; i++) {
                skyPosition[i] -= obsPos[i];
            }
        }

        //Relative position is calculated taking into account the time needed for light to travel
        distance = Utility.vectorLength(skyPosition);
        double time = distance / Constants.c;
        for (int j = 0; j < 3; j++) {
            skyPosition[j] = skyPosition[j] - velocity[j] * time;
        }

        double h = Math.sqrt(skyPosition[0] * skyPosition[0] + skyPosition[1] * skyPosition[1]);
        ra = Math.atan2(skyPosition[1], skyPosition[0]) * 180 / Math.PI;
        dec = Math.atan2(skyPosition[2], h) * 180 / Math.PI;

        return new double[]{ra, dec, distance};
    }



}
