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
}
