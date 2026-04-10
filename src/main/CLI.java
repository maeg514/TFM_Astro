package main;

import main.efficiencyOriented.RKController;

import javax.swing.*;

public class CLI {
    private final RKController rkController;

    public CLI() {
        this.rkController = new RKController();
    }

    public void execute() {
        init();
        run();
        close();
    }

    private void init() {
        System.out.println("Iniciando el programa");
    }

    private void run() {
        String[] opciones = {"RK5", "RK4I", "RK4C", "Salir"};
        int eleccion = -1;
        while (eleccion != 3) {
            eleccion = JOptionPane.showOptionDialog(
                    null,
                    "Elige una opción:",
                    "Menú",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
            String rkType = null;

            switch (eleccion) {
                case 0:
                    JOptionPane.showMessageDialog(null, "Elegiste RK5");
                    rkType = "RK5";
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Elegiste RK4I");
                    rkType = "RK5";
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Elegiste RK4C");
                    rkType = "RK5";
                default:
                    JOptionPane.showMessageDialog(null, "Saliendo");
            }
            if (rkType != null) rkController.run(rkType);

        }
    }

    private void close() {
        System.out.println("Cerrando la aplicación");
    }
}
