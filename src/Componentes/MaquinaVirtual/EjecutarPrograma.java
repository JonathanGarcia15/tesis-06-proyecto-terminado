package Componentes.MaquinaVirtual;

import ConfiguraLenguaje.cargarTexto;
import org.json.JSONObject;

import javax.swing.*;
import java.nio.channels.ClosedByInterruptException;

public class EjecutarPrograma implements Runnable {

    private JLabel lblResultados;
    private JTextArea txtResultados;
    private int Filas;
    private int Columnas;
    private int[][] MapaBackend;
    private JSONObject JSONPrograma;
    private final RunCode runCode = new RunCode();

    private final cargarTexto idioma = new cargarTexto("EjecutarPrograma");
    private Boolean canStart;

    private Thread hilo;

    public EjecutarPrograma() {
        MapaBackend=null;
        canStart=true;
    }

    public Boolean getCanStart() {
        return canStart;
    }

    public void setLblResultados(JLabel lblResultados) {
        this.lblResultados = lblResultados;
    }

    public void setTxtResultados(JTextArea txtResultados) {
        this.txtResultados = txtResultados;
    }

    public void setFilas(int filas) {
        Filas = filas;
    }

    public void setColumnas(int columnas) {
        Columnas = columnas;
    }

    public void setMapaBackend(int[][] mapaBackend) {
        MapaBackend = mapaBackend;
    }

    public void setJSONPrograma(JSONObject JSONPrograma) {
        this.JSONPrograma = JSONPrograma;
    }

    public boolean isReady() {
        return MapaBackend != null && JSONPrograma.getBoolean("status") && canStart;
    }

    public void interrumpirProceso(){
        if(!canStart) {
            //int tiempoInst = idioma.getConfigInteger("TiempoInstruccion");
            //int tiempoEsp = idioma.getConfigInteger("TiempoEspera");

            //idioma.putConfigValue("TiempoInstruccion",1);
            //idioma.putConfigValue("TiempoEspera",1);

            hilo.stop();
            canStart = true;

            JOptionPane.showMessageDialog(
                    new JFrame(),
                    this.idioma.traerTexto("ProcesoInterrumpido"),
                    this.idioma.traerTexto("DetenerPrograma"),
                    JOptionPane.INFORMATION_MESSAGE
            );

            //idioma.putConfigValue("TiempoInstruccion",tiempoInst);
            //idioma.putConfigValue("TiempoEspera",tiempoEsp);

            runCode.printHTMLMap();
        }else{
            JOptionPane.showMessageDialog(
                    new JFrame(),
                    this.idioma.traerTexto("NoSePuedeDetener"),
                    this.idioma.traerTexto("DetenerPrograma"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void ejecutaPrograma(){
        if(canStart){
            hilo = new Thread(this, "EjecutarPrograma");
            hilo.start();
        }else{
            JOptionPane.showMessageDialog(
                    new JFrame(),
                    this.idioma.traerTexto("NoSePuedeIniciar"),
                    this.idioma.traerTexto("IniciarPrograma"),
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    @Override
    public void run() {
        canStart=false;
        runCode.setJSONPrograma(this.JSONPrograma);
        runCode.setTxtResultados(this.txtResultados);
        runCode.setLblResultados(this.lblResultados);
        runCode.setFilas(this.Filas);
        runCode.setColumnas(this.Columnas);
        runCode.setMapaBackend(this.MapaBackend);

        try{
            runCode.ejecutaPrograma();
        }catch(Exception ignored){}

        canStart=true;
    }

    public void limpiarMapa() {
        if(canStart){
            runCode.cleanMap();
        }else{
            JOptionPane.showMessageDialog(
                    new JFrame(),
                    this.idioma.traerTexto("NoSePuedeLimpiar"),
                    this.idioma.traerTexto("LimpiarMapa"),
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
