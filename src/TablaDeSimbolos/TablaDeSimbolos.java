package TablaDeSimbolos;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class TablaDeSimbolos {
    private final ArrayList<JSONObject> tablaDeSimbolos =new ArrayList<>();

    public TablaDeSimbolos() {
        tablaDeSimbolos.add(
                new JSONObject()
                        .put("identifier","")
                        .put("value",-1)
                        .put("function","")
                        .put("type",-1)
        );
    }

    public ArrayList<JSONObject> getTablaDeSimbolos() {
        return tablaDeSimbolos;
    }

    public void agregarElementoATabla(String identificador, int valor, String funcion, int tipo){
        agregarElemento(identificador,valor,funcion,tipo);
    }
    public int valorDeUnIdentificador(String identificador, int tipo){
        return valorDeUnElemento(identificador,tipo);
    }

    public boolean existeUnElemento(String identificador, int tipo){
        return buscarElementoExistente(identificador,tipo);
    }

    public void limpiarTablaDeSimbolos(){
        tablaDeSimbolos.clear();
    }

    private boolean buscarElementoExistente(String identificador, int tipo){
        for (JSONObject tablaDeSimbolo : tablaDeSimbolos) {
            if (Objects.equals(tablaDeSimbolo.getString("identifier"), identificador)
                    && tablaDeSimbolo.getInt("type") == tipo) {
                return true;
            }
        }
        return false;
    }

    private void agregarElemento(String identificador, int valor,String funcion, int tipo){
        tablaDeSimbolos.add(
                new JSONObject()
                        .put("identifier",identificador)
                        .put("value",valor)
                        .put("function",funcion)
                        .put("type",tipo)
        );
    }

    private int valorDeUnElemento(String identificador, int tipo){
        int i;
        for(i=0; i<tablaDeSimbolos.size();i++){
            if(
                    (Objects.equals(tablaDeSimbolos.get(i).getString("identifier"), identificador)) &&
                            (tablaDeSimbolos.get(i).getInt("type") == tipo)
            ){
                return (tablaDeSimbolos.get(i).getInt("value"));
            }
        }
        return -999999999;
    }

    public boolean modificarElementoATabla(String identifier, int valor, int tipo) {
        return modificarValor(identifier,valor,tipo);
    }

    private boolean modificarValor(String identificador, int valor, int tipo){
        //Buscar que existe
        if(!buscarElementoExistente(identificador,tipo)){
            return false;
        }

        //Cambiando Valor
        for (JSONObject tablaDeSimbolo : tablaDeSimbolos) {
            if ((Objects.equals(tablaDeSimbolo.getString("identifier"), identificador))
                    && (tablaDeSimbolo.getInt("type") == tipo)) {
                tablaDeSimbolo.put("value", valor);
                return true;
            }
        }
        //System.out.println(identificador+"No encontrado");
        return false;
    }

    public void eliminarElementosPorID(String idVariablesTemporales) {
        this.eliminarElementos(idVariablesTemporales);
    }

    private void eliminarElementos(String idVariablesTemporales) {
        for(int i=0; i<tablaDeSimbolos.size();i++){
            if(Objects.equals(tablaDeSimbolos.get(i).getString("function"), idVariablesTemporales)){
                tablaDeSimbolos.remove(i);
                i=0;
            }
        }
    }
}
