package ConfiguraLenguaje;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class cargarTexto {

    private JSONObject JSONStrings;
    private JSONObject JSONConfigs;

    private JSONObject JSONColors;

    private final String DireccionProyecto = System.getProperty("user.dir").replace("\\", "/");


    public cargarTexto(String nombreDeLaClase) {
        try {
            JSONStrings = new JSONObject(
                    new String(Files.readAllBytes(Paths.get(DireccionProyecto + "/FILES/Strings/" + nombreDeLaClase + ".json")), StandardCharsets.UTF_8)
            );
            JSONConfigs = new JSONObject(
                    new String(Files.readAllBytes(Paths.get(DireccionProyecto + "/FILES/Config/Config.json")), StandardCharsets.UTF_8)
            );
            JSONColors = new JSONObject(
                    new String(Files.readAllBytes(Paths.get(DireccionProyecto + "/FILES/Strings/Colors.json")), StandardCharsets.UTF_8)
            );
        }catch(IOException ignore){
            JSONStrings = new JSONObject();
            JSONConfigs = new JSONObject();
            JSONColors = new JSONObject();
        }
    }

    public String traerTexto(String valor){
        return texto(valor);
    }
    public String traerColor(String valor){
        return color(valor);
    }

    private String color(String valor) {
        return JSONColors.getString(valor);
    }

    private String texto(String key){
        if ("EN".equals(this.getConfigString("IdiomaDelPrograma"))) {
            return JSONStrings.getJSONArray(key).getString(1);
        }
        return JSONStrings.getJSONArray(key).getString(0);
    }

    public int FuenteMapa() {
        return JSONConfigs.getInt("TamanoTextoMapa");
    }

    public String getConfigString(String key) {
        try {
            return new JSONObject(
                    new String(Files.readAllBytes(Paths.get(DireccionProyecto + "/FILES/Config/Config.json")), StandardCharsets.UTF_8)
            ).getString(key);
        }catch (Exception ignore){
            return "";
        }
    }

    public int getConfigInteger(String key) {
        try {
            return new JSONObject(
                    new String(Files.readAllBytes(Paths.get(DireccionProyecto + "/FILES/Config/Config.json")), StandardCharsets.UTF_8)
            ).getInt(key);
        }catch (Exception ignore){
            return -1;
        }
    }

    public boolean getConfigBoolean(String key) {
        try {
            return new JSONObject(
                    new String(Files.readAllBytes(Paths.get(DireccionProyecto + "/FILES/Config/Config.json")), StandardCharsets.UTF_8)
            ).getBoolean(key);
        }catch (Exception ignore){
            return false;
        }
    }

    public boolean putConfigValue(String key, String value) {
        return putString(key,value);
    }

    public boolean putConfigValue(String key, int value) {
        return putInt(key,value);
    }

    public boolean putConfigValue(String key, boolean value) {
        return putBoolean(key,value);
    }

    private boolean putString(String key, String value) {
        JSONConfigs.put(key,value);
        saveConfigFile(JSONConfigs);
        return true;
    }

    private boolean putInt(String key, int value) {
        if((Objects.equals(key, "TamanoTextoMapa"))&&(value<=10)){
            JSONConfigs.put(key,11);
            saveConfigFile(JSONConfigs);
            return true;
        }else if((Objects.equals(key, "TamanoTextoMapa"))&&(value>=40)){
            JSONConfigs.put(key,39);
            saveConfigFile(JSONConfigs);
            return true;
        }else if((Objects.equals(key, "TiempoEspera"))&&(value<=0)){
            JSONConfigs.put(key,1);
            saveConfigFile(JSONConfigs);
            return true;
        }else if((Objects.equals(key, "TiempoEspera"))&&(value>2000)){
            JSONConfigs.put(key,2000);
            saveConfigFile(JSONConfigs);
            return true;
        }else if((Objects.equals(key, "TiempoInstruccion"))&&(value<=0)){
            JSONConfigs.put(key,1);
            saveConfigFile(JSONConfigs);
            return true;
        }else if((Objects.equals(key, "TiempoInstruccion"))&&(value>2000)){
            JSONConfigs.put(key,2000);
            saveConfigFile(JSONConfigs);
            return true;
        }

        JSONConfigs.put(key,value);
        saveConfigFile(JSONConfigs);
        return true;
    }

    private boolean putBoolean(String key, Boolean value) {
        JSONConfigs.put(key,value);
        saveConfigFile(JSONConfigs);
        return true;
    }

    private void saveConfigFile(JSONObject config) {
        try {
            FileWriter archivoEscrito = new FileWriter(DireccionProyecto+"/FILES/Config/Config.json");
            archivoEscrito.write(config.toString());
            archivoEscrito.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
