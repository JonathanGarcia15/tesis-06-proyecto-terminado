package Componentes.MaquinaVirtual;

import org.json.JSONObject;

import javax.swing.*;

public class EjecutarPrograma implements Runnable {

    private JLabel lblResultados;
    private JTextArea txtResultados;
    private int Filas;
    private int Columnas;
    private int[][] MapaBackend;
    private JSONObject JSONPrograma;
    private final RunCode runCode = new RunCode();

    public EjecutarPrograma() {
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
        return MapaBackend != null && JSONPrograma.getBoolean("status");
    }

    public void ejecutaPrograma(){
        runCode.setJSONPrograma(this.JSONPrograma);
        runCode.setTxtResultados(this.txtResultados);
        runCode.setLblResultados(this.lblResultados);
        runCode.setFilas(this.Filas);
        runCode.setColumnas(this.Columnas);
        runCode.setMapaBackend(this.MapaBackend);
        Thread hilo = new Thread(this, "EjecutarPrograma");
        hilo.start();
    }

    @Override
    public void run() {
        runCode.ejecutaPrograma();
    }
}
