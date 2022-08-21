package Main;

import InterfazDeUsuario.Fenix;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            //Cambiando el tema por default a uno de windows
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            JFrame frame = new Fenix();
            frame.setSize(700,450);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

    }
}
