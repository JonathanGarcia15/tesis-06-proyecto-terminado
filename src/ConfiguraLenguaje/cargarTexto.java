package ConfiguraLenguaje;

import org.json.JSONObject;

import java.io.FileWriter;
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

    private String texto(String key){
        JSONObject Cadenas = this. traeObjeto();
        //JSONObject IdiomaPreferido = this. traeConfiguracionIdiomaPrograma();

        if ("EN".equals(this.getConfigString("IdiomaDelPrograma"))) {
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

    public int FuenteMapa() {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getInt("TamanoTextoMapa");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getConfigString(String key) {
        return getConfigStr(key);
    }

    private String getConfigStr(String key) {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getString(key);
        } catch (IOException e) {
            return "NO CONFIG FILE";
        }
    }

    public int getConfigInteger(String key) {
        return getConfigInt(key);
    }

    private int getConfigInt(String key) {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getInt(key);
        } catch (IOException e) {
            return -99999999;
        }
    }

    public boolean getConfigBoolean(String key) {
        return getConfigBool(key);
    }

    private boolean getConfigBool(String key) {
        try {
            return new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            ).getBoolean(key);
        } catch (IOException e) {
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
        try {
            JSONObject config = new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            );
            config.put(key,value);
            saveConfigFile(config);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean putInt(String key, int value) {
        try {
            JSONObject config = new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            );
            config.put(key,value);
            saveConfigFile(config);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean putBoolean(String key, Boolean value) {
        try {
            JSONObject config = new JSONObject(
                    new String (Files.readAllBytes(Paths.get(DireccionProyecto+"/FILES/Config/Config.json")))
            );
            config.put(key,value);
            saveConfigFile(config);
            return true;
        } catch (IOException e) {
            return false;
        }
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
