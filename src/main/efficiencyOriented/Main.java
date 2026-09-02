package main.efficiencyOriented;

import main.GUI;
import main.RKController;

public class Main {
     public static void main(String[] args) {
        RKController rkController = new RKController();
        rkController.run("RK5");
        /*GUI app = new GUI();
        app.execute();*/
    }
}
 