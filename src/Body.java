public class Body {
    private String name;
    private double mass;
    private double[] positionInitial;
    private double[] velocitiesInitial;
    private double[] aceleration;
    private double[] deltas;

    public Body(String name, double mass, double[] positionInitial, double[] velocitiesInitial) {
        this.name = name;
        this.mass = mass;
        this.positionInitial = positionInitial;
        this.velocitiesInitial = velocitiesInitial;
        this.aceleration = new double[3];
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double[] getAceleration() {
        return aceleration;
    }

    public void setAceleration(double[] aceleration) {
        this.aceleration = aceleration;
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

    public double distance(Body body) {
        double r_2 = 0;
        for (int i = 0; i < positionInitial.length; i++) {
            r_2 += Math.pow(this.positionInitial[i] - body.getPositionInitial()[i], 2);
        }
        return Math.sqrt(r_2);
    }


}
