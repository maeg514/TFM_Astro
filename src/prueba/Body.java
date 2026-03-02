package prueba;

public class Body {
    private String name;
    private double mass;
    private double[] positionInitial;
    private double[] velocitiesInitial;
    private double[] acceleration;
    public double[] position;
    public double[] velocities;

    public Body(String name, double mass, double[] positionInitial, double[] velocitiesInitial) {
        this.name = name;
        this.mass = mass;
        this.positionInitial = positionInitial;
        this.velocitiesInitial = velocitiesInitial;
        this.acceleration = new double[3];
        this.position = new double[3];
        this.velocities = new double[3];
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double[] getAceleration() {
        return acceleration;
    }

    public void setAceleration(double[] aceleration) {
        this.acceleration = aceleration;
    }

    public double[] getPositionInitial() {
        return positionInitial;
    }

    public double[] getVelocitiesInitial() {
        return velocitiesInitial;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setVelocitiesInitial(double[] velocitiesInitial) {
        this.velocitiesInitial = velocitiesInitial;
    }

    public void setPositionInitial(double[] positionInitial) {
        this.positionInitial = positionInitial;
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
        return velocitiesInitial[j] + velocities[j];
    }

    public double[] getVelocity() {
        double[] vel = new double[3];
        for (int i = 0; i < 3; i++) {
            vel[i] = velocitiesInitial[i] + velocities[i];
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

    public double[] distanceVector(Body body) {
        double[] vectorDistance = new double[3];
        for (int i = 0; i < position.length; i++) {
            vectorDistance[i] += body.getPosition(i) - this.getPosition(i);
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

    public void ra_dec(Body body, boolean real) {
        double ra, dec;
        double distance = this.distance(body);
        double time = distance / Constants.c;
        double[] skyPosition = body.getPositionInitial();
        double[] velocity = body.getVelocitiesInitial();
        if (real) {
            for (int j = 0; j < 3; j++) {
                skyPosition[j] = skyPosition[j] - velocity[j] * time;
            }
        }
        skyPosition = distanceVector(skyPosition);
        double h = Math.sqrt(Math.pow(skyPosition[0], 2) + Math.pow(skyPosition[1], 2));
        ra = Math.atan2(skyPosition[1], skyPosition[0]) * 180 / Math.PI;
        dec = Math.asin(skyPosition[2] / h) * 180 / Math.PI;
        System.out.println(body.getName() + " | " + ra + ": " + dec);
    }
}
