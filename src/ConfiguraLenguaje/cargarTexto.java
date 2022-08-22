package ConfiguraLenguaje;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class cargarTexto {

    ///private final String nombreDeLaClase;

    private JSONObject JSONStrings;
    private JSONObject JSONConfigs;

    private JSONObject JSONColors;

    private final String DireccionProyecto = System.getProperty("user.dir").replace("\\", "/");


    public cargarTexto(String nombreDeLaClase) {
        ///this.nombreDeLaClase = nombreDeLaClase;
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
        //JSONObject Cadenas = this. traeObjeto();
        //JSONObject IdiomaPreferido = this. traeConfiguracionIdiomaPrograma();

        if ("EN".equals(this.getConfigString("IdiomaDelPrograma"))) {
            return JSONStrings.getJSONArray(key).getString(1);
        }
        return JSONStrings.getJSONArray(key).getString(0);
    }

    public int FuenteMapa() {
        return JSONConfigs.getInt("TamanoTextoMapa");
    }

    public String getConfigString(String key) {
        return JSONConfigs.getString(key);
    }

    public int getConfigInteger(String key) {
        return JSONConfigs.getInt(key);
    }

    public boolean getConfigBoolean(String key) {
        return JSONConfigs.getBoolean(key);
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
