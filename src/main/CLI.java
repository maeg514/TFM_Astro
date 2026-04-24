package main;

import javax.swing.*;
import java.awt.*;

public class CLI {
    private final RKController rkController;

    private JFrame frame;
    private JTextArea textArea;

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
        initUI();
        while (true) {
            JList<String> list = new JList<>(opciones);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    new JScrollPane(list),
                    "Elige una opción",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                break;
            }

            String seleccion = list.getSelectedValue();

            if (seleccion == null || seleccion.equals("Salir")) {
                JOptionPane.showMessageDialog(null, "Saliendo");
                break;
            }

            JOptionPane.showMessageDialog(null, "Elegiste " + seleccion);

            rkController.run(seleccion);

            ObjectManagement objects = rkController.getObjects();
            String summary = objects.prettyPrintRADec(null);

            updateUI(summary);

            objects.addBodies();

        }
    }

    private void close() {
        System.out.println("Cerrando la aplicación");
    }

    private void initUI() {
        frame = new JFrame("Matriz");
        textArea = new JTextArea(20, 100);
        textArea.setEditable(false);

        frame.add(new JScrollPane(textArea));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        textArea.setBackground(Constants.colorBackground);
        textArea.setForeground(Constants.colorText);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
    }

    private void updateUI(String text) {
        textArea.setText(text);
        textArea.setCaretPosition(0);
        textArea.setEditable(false);
    }
}
