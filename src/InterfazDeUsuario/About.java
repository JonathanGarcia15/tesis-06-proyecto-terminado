package InterfazDeUsuario;

import ConfiguraLenguaje.cargarTexto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class About extends JFrame {
    private JPanel jpAbout;
    private JTextArea txtChangelog;
    private JButton btnAceptar;
    private JLabel lblAbout;
    private JLabel lblChangelog;

    private final cargarTexto idioma = new cargarTexto("About");

    private Fenix fenix;

    public About(Fenix fenixFrame) {

        super("{CONFIGURACION}");

        this.fenix = fenixFrame;

        init();

        btnAceptar.addActionListener(e -> {
            fenix.setVisible(true);
            fenix.setEnabled(true);
            this.dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                fenix.setVisible(true);
                fenix.setEnabled(true);
            }
        });

    }

    private void init() {
        setContentPane(jpAbout);

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(
                System.getProperty("user.dir").replace("\\", "/")+"/FILES/Icons/About.png")
        );

        setTitle(idioma.traerTexto("AcercaDe"));

        lblAbout.setHorizontalAlignment(SwingConstants.CENTER);
        lblAbout.setVerticalAlignment(SwingConstants.CENTER);

        lblAbout.setText(idioma.traerTexto("About")
                .replace("NAME",idioma.getConfigString("TituloDelPrograma"))
                .replace("VERSION",
                        idioma.traerTexto("VersionText")+
                        idioma.getConfigString("Version")
                )
        );

        lblChangelog.setText(idioma.traerTexto("Cambios"));

        String string ="";
        String filePath = System.getProperty("user.dir").replace("\\", "/")+
                "/FILES/Strings/"+
                idioma.traerTexto("ChangelogFile");
        Charset encoding = Charset.defaultCharset();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), encoding);
            string = lines.stream().collect(Collectors.joining("\n"));
            txtChangelog.setText(string);
        } catch (IOException ex) {
            //Logger.getLogger(Mapas.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtChangelog.setEnabled(false);

        btnAceptar.setText(idioma.traerTexto("Aceptar"));
    }
}

