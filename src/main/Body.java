package main;

public class Body {
    private final String name;
    private final double mass;
    private double[] positionInitial;
    private double[] velocityInitial;
    private double[] acceleration;
    public double[] position;
    public double[] velocity;

    public Body(String name, double mass, double[] positionInitial, double[] velocityInitial) {
        this.name = name;
        this.mass = mass;
        this.positionInitial = positionInitial;
        this.velocityInitial = velocityInitial;
        this.acceleration = new double[3];
        this.position = new double[3];
        this.velocity = new double[3];
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

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getPosition(int j) {
        return positionInitial[j] + position[j];
    }

    public double[] getPosition() {
        double[] pos = new double[3];
        for (int i = 0; i < 3; i++) {
            pos[i] = positionInitial[i] + position[i];
        }
        return pos;
    }

    public double getVelocity(int j) {
        return velocityInitial[j] + velocity[j];
    }

    public double[] getVelocity() {
        double[] vel = new double[3];
        for (int i = 0; i < 3; i++) {
            vel[i] = velocityInitial[i] + velocity[i];
        }
        return vel;
    }

    public double distance(Body body) {
        double r_2 = 0;
        for (int i = 0; i < position.length; i++) {
            r_2 += Math.pow(this.getPosition(i) - body.getPosition(i), 2);
        }
        return Math.sqrt(r_2);
    }

    public double distance(double[] bodyVector) {
        double r_2 = 0;
        for (int i = 0; i < position.length; i++) {
            r_2 += Math.pow(this.getPosition(i) - bodyVector[i], 2);
        }
        return Math.sqrt(r_2);
    }

    public double[] distanceVector(Body body) {
        double[] vectorDistance = new double[3];
        double[] vectorBody = body.getPositionInitial();
        for (int i = 0; i < position.length; i++) {
            vectorDistance[i] += vectorBody[i] - this.positionInitial[i];
        }
        return vectorDistance;
    }

    public double[] distanceVector(double[] skyPosition) {
        double[] vectorDistance = new double[3];
        for (int i = 0; i < position.length; i++) {
            vectorDistance[i] = skyPosition[i] - this.positionInitial[i];
        }
        return vectorDistance;
    }

    public double[] relativeVelocityVector(Body body) {
        double[] vectorVelocity = new double[3];
        double[] vectorBody = body.getVelocityInitial();
        for (int i = 0; i < position.length; i++) {
            vectorVelocity[i] += vectorBody[i] - this.velocityInitial[i];
        }
        return vectorVelocity;
    }
}
