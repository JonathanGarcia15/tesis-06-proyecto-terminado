package ConfiguraLenguaje;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class cargarTexto {
    public cargarTexto(String nombreDeLaClase) {
        this.nombreDeLaClase = nombreDeLaClase;
    }

    private final String nombreDeLaClase;

    private final String DireccionProyecto = System.getProperty("user.dir").replace("\\", "/");

    public String traerTexto(String valor){
        return texto(valor);
    }
    public String traerColor(String valor){
        return color(valor);
    }

    private String color(String valor) {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Strings/Colors.json")))
            ).getString(valor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int tiempoEsperaInstruccion(){
        return tiempoDeInstruccion();
    }

    public int esperaTiempo(){
        return tiempoDeEspera();
    }

    private int tiempoDeEspera() {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getInt("TiempoInstruccion");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int tiempoDeInstruccion() {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getInt("TiempoInstruccion");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String idiomaDelCompilador(){
        return this.traeConfiguracionIdiomaCompilador().getString("IdiomaDelCompilador");
    }

    private String texto(String key){
        JSONObject Cadenas = this. traeObjeto();
        JSONObject IdiomaPreferido = this. traeConfiguracionIdiomaPrograma();

        if ("EN".equals(IdiomaPreferido.getString("IdiomaDelPrograma"))) {
            return Cadenas.getJSONArray(key).getString(1);
        }
        return Cadenas.getJSONArray(key).getString(0);
    }

    private JSONObject traeObjeto(){
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Strings/"+nombreDeLaClase+".json")))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject traeConfiguracionIdiomaPrograma(){
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject traeConfiguracionIdiomaCompilador(){
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ocultarGenerarCompiladores() {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getBoolean("MostrarOpcionGenerarCompiladores");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int FuenteMapa() {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getInt("TamanoTextoMapa");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
