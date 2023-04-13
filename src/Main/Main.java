package Main;

import InterfazDeUsuario.Fenix;
import org.json.JSONException;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            //Cambiando el tema por default a uno de windows
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(new JFrame(), "Only available for Windows","Windows only", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            try {
                JFrame frame = new Fenix();
                frame.setSize(800,550);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
            catch (JSONException e){
                JOptionPane.showMessageDialog(new JFrame(), "STRING FILES MAY BE CORRUPTED\nREINSTALL THE PROGRAM\nError: "+e,"FATAL ERROR", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });

    }
}
