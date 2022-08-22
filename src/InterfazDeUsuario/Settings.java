package InterfazDeUsuario;

import ConfiguraLenguaje.cargarTexto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Settings extends JFrame{
    private JPanel jpInterfazConfiguracion;
    private JLabel lblConfiguration;
    private JLabel lblIdiomaPrograma;
    private JComboBox cmbIdiomaPrograma;
    private JSeparator jSeparator2;
    private JLabel lblIdiomaCompilador;
    private JComboBox cmbIdiomaCompilador;
    private JLabel lblTamanoTextoMapa;
    private JSpinner spnTamanoTextoMapa;
    private JLabel lblTiempoInstruccion;
    private JSpinner spnTiempoInstruccion;
    private JLabel lblTiempoInstruccionEspera;
    private JSpinner spnTiempoInstruccionEspera;
    private JCheckBox cbxMostrarGenerador;
    private JLabel lblGeneradorCompilador;
    private JButton btnGuardar;
    private JLabel lblGuardarCambios;
    private JSeparator separadorCompiladores;
    private JSeparator separadorEspera;
    private final cargarTexto idioma = new cargarTexto("Settings");

    private Fenix fenix;

    public Settings(Fenix fenixFrame){

        super("{CONFIGURACION}");

        this.fenix = fenixFrame;

        init();

        btnGuardar.addActionListener(e -> {
            guardarCambios();
            fenix.setVisible(true);
            fenix.setEnabled(true);
            JOptionPane.showMessageDialog(
                    new JFrame(),
                    this.idioma.traerTexto("EscrituraExitosa"),
                    this.idioma.traerTexto("Title"),
                    JOptionPane.INFORMATION_MESSAGE
            );
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

    private void guardarCambios() {
        if (cmbIdiomaPrograma.getSelectedIndex() == 0) {
            idioma.putConfigValue("IdiomaDelPrograma", "EN");
        } else {
            idioma.putConfigValue("IdiomaDelPrograma", "ES");
        }

        switch(cmbIdiomaCompilador.getSelectedIndex()){
            case 1:
                idioma.putConfigValue("IdiomaDelCompilador", "EN");
                break;
            case 2:
                idioma.putConfigValue("IdiomaDelCompilador", "ES");
                break;
            default:
                idioma.putConfigValue("IdiomaDelCompilador", "DEFAULT");
        }

        idioma.putConfigValue("TamanoTextoMapa", (Integer) this.spnTamanoTextoMapa.getValue());
        idioma.putConfigValue("TiempoInstruccion", (Integer) this.spnTiempoInstruccion.getValue());
        idioma.putConfigValue("TiempoEspera", (Integer) this.spnTiempoInstruccionEspera.getValue());

        idioma.putConfigValue("MostrarOpcionGenerarCompiladores",this.cbxMostrarGenerador.isSelected());

    }

    private void init() {
        setContentPane(jpInterfazConfiguracion);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(
                System.getProperty("user.dir").replace("\\", "/")+"/FILES/Icons/Settings.png")
        );

        setTitle(idioma.traerTexto("Title"));


        lblConfiguration.setText(idioma.traerTexto("Configuracion"));
        lblIdiomaPrograma.setText(idioma.traerTexto("IdiomaPrograma"));
        lblIdiomaCompilador.setText(idioma.traerTexto("IdiomaCompilador"));
        lblTamanoTextoMapa.setText(idioma.traerTexto("TamannoMapa"));
        lblTiempoInstruccion.setText(idioma.traerTexto("TiempoInstruccion"));
        lblTiempoInstruccionEspera.setText(idioma.traerTexto("TiempoInstruccionEspera"));
        lblGeneradorCompilador.setText(idioma.traerTexto("OpcionesCompilador"));
        lblGuardarCambios.setText(idioma.traerTexto("GuardarCambios"));

        cbxMostrarGenerador.setText("");

        btnGuardar.setText(idioma.traerTexto("Guardar"));

        lblTiempoInstruccionEspera.show();

        cmbIdiomaPrograma.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
                this.idioma.traerTexto("Ingles"),
                this.idioma.traerTexto("Espannol")
        }));

        cmbIdiomaCompilador.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
                this.idioma.traerTexto("Default"),
                this.idioma.traerTexto("SoloIngles"),
                this.idioma.traerTexto("SoloEspannol")
        }));

        System.out.println(idioma.getConfigString("IdiomaDelPrograma"));
        System.out.println(idioma.getConfigString("IdiomaDelCompilador"));


        switch(idioma.getConfigString("IdiomaDelPrograma")){
            case "EN":
                cmbIdiomaPrograma.setSelectedIndex(0);
                break;
            case "ES":
                cmbIdiomaPrograma.setSelectedIndex(1);
                break;
        }

        switch(idioma.getConfigString("IdiomaDelCompilador")){
            case "EN":
                cmbIdiomaCompilador.setSelectedIndex(1);
                break;
            case "ES":
                cmbIdiomaCompilador.setSelectedIndex(2);
                break;
            case "DEFAULT":
                cmbIdiomaCompilador.setSelectedIndex(0);
                break;
        }

        spnTamanoTextoMapa.setValue(idioma.getConfigInteger("TamanoTextoMapa"));
        spnTiempoInstruccion.setValue(idioma.getConfigInteger("TiempoInstruccion"));
        spnTiempoInstruccionEspera.setValue(idioma.getConfigInteger("TiempoEspera"));

        cbxMostrarGenerador.setSelected(idioma.getConfigBoolean("MostrarOpcionGenerarCompiladores"));

        if(!idioma.getConfigBoolean("devVersion")){
            lblTiempoInstruccionEspera.hide();
            spnTiempoInstruccionEspera.hide();
            separadorEspera.hide();

            lblGeneradorCompilador.hide();
            cbxMostrarGenerador.hide();
            separadorCompiladores.hide();
        }
    }
}
