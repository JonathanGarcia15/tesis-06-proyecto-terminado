package Componentes.MaquinaVirtual;

import ConfiguraLenguaje.cargarTexto;
import TablaDeSimbolos.TablaDeSimbolos;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class RunCode {
    //Valores traídos de otras áreas
    private JLabel lblResultados;
    private JTextArea txtResultados;
    private JSONObject JSONPrograma;
    private int[][] MapaBackend;
    private int Filas;
    private int Columnas;
    private String MapaHTML;
    private int[][] MapaDeTrabajo;
    private int FilaPersonaje;
    private int ColumnaPersonaje;
    private int Personaje;
    private boolean tengoObjeto = false;
    private int DebajoDelPersonaje;
    private final cargarTexto idioma = new cargarTexto("RunCode");
    private TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
    private int pintura = -1;
    private final Stack<TablaDeSimbolos> pilaDePilas = new Stack<>();

    public void setFilas(int filas) {
        Filas = filas;
    }

    public void setColumnas(int columnas) {
        Columnas = columnas;
    }

    public void setLblResultados(JLabel lblResultados) {
        this.lblResultados = lblResultados;
    }

    public void setTxtResultados(JTextArea txtResultados) {
        this.txtResultados = txtResultados;
    }

    public void setJSONPrograma(JSONObject JSONPrograma) {
        this.JSONPrograma = JSONPrograma;
    }

    public void setMapaBackend(int[][] mapaBackend) {
        MapaBackend = mapaBackend;
    }

    public boolean isReady() {
        return MapaBackend != null && JSONPrograma.getBoolean("status");
    }

    public void ejecutaPrograma() {
        cleanMap();
        boolean resultado = this.resolverPrograma("main",JSONPrograma.getJSONArray("main"));
        if(resultado){
            agregarTexto(
                    idioma.traerTexto("ProgramaExitoso")
            );
        }else{
            agregarTextoError(
                    idioma.traerTexto("ProgramaFallido")
            );
        }
    }

    private int[][] generarMapaDeTrabajo(int[][] mapaBackend) {
        int[][] nuevoMapa = new int[Filas][Columnas];
        for(int i=0; i<Filas;i++){
            if (Columnas >= 0) System.arraycopy(mapaBackend[i], 0, nuevoMapa[i], 0, Columnas);
        }
        return nuevoMapa;
    }

    private boolean resolverPrograma(String funcionActual, JSONArray subprogram) {
        JSONObject temporal;
        for (int i = 0; i < subprogram.length(); i++) {
            temporal = subprogram.getJSONObject(i);
            switch (temporal.getString("instruction")) {
                case "TOMAR":
                    if (!TOMAR()) {
                        return false;
                    }
                    break;
                case "SOLTAR":
                    if (!SOLTAR()) {
                        return false;
                    }
                    break;
                case "ELIMINAR":
                    if (!ELIMINAR()) {
                        return false;
                    }
                    break;
                case "DESACTIVARKABOOM":
                    if (!DESACTIVARKABOOM()) {
                        return false;
                    }
                    break;
                case "DEJAPINTAR":
                    if (!DEJAPINTAR()) {
                        return false;
                    }
                    break;
                case "TERMINARBLOQUE":
                    return TERMINARBLOQUE();
                case "IZQUIERDA":
                    if (!IZQUIERDA(temporal)) {
                        return false;
                    }
                    break;
                case "DERECHA":
                    if (!DERECHA(temporal)) {
                        return false;
                    }
                    break;
                case "INCREMENTAVARIABLE":
                    if (!INCREMENTAVARIABLE(temporal)) {
                        return false;
                    }
                    break;
                case "DECREMENTAVARIABLE":
                    if (!DECREMENTAVARIABLE(temporal)) {
                        return false;
                    }
                    break;
                case "LLAMADAAFUNCION":
                    if (!LLAMADAAFUNCION(temporal)) {
                        return false;
                    }
                    break;
                case "IMPRIMIRCADENA":
                    if (!IMPRIMIRCADENA(temporal)) {
                        return false;
                    }
                    break;
                case "SI":
                    try{
                        if (!SI(temporal)) {
                            return false;
                        }
                    }catch(Exception e){
                        return false;
                    }
                    break;
                case "COMPARAR":
                    if (!COMPARAR(temporal)) {
                        return false;
                    }
                    break;
                case "FOR":
                    try{
                        if (!FOR(temporal)) {
                            return false;
                        }
                    }catch(Exception e){
                        return false;
                    }
                    break;
                case "HACER":
                    try{
                        if (!HACER(temporal)) {
                            return false;
                        }
                    }catch(Exception e){
                        return false;
                    }
                    break;
                case "REPITEHASTA":
                    try{
                        if (!REPITEHASTA(temporal)) {
                            return false;
                        }
                    }catch(Exception e){
                        return false;
                    }
                    break;
                case "PINTAR":
                    if (!PINTAR(temporal)) {
                        return false;
                    }
                    break;
                case "IMPRIMIRVARIABLE":
                    if (!IMPRIMIRVARIABLE(temporal)) {
                        return false;
                    }
                    break;
                case "DECLARAVARIABLE":
                    if (!DECLARAVARIABLE(temporal,funcionActual)) {
                        return false;
                    }
                    break;
                case "MODIFICAVARIABLE":
                    if (!MODIFICAVARIABLE(temporal)) {
                        return false;
                    }
                    break;
                case "AVANZAR":
                    if (!AVANZAR(temporal)) {
                        return false;
                    }
                    break;
                case "ESPERA":
                    if (!ESPERA(temporal)) {
                        return false;
                    }
                    break;
                default:

                    return false;
            }
        }
        return true;
    }

    private boolean LLAMADAAFUNCION(JSONObject temporal) {
        JSONArray ListaDeFunciones = JSONPrograma.getJSONArray("functions");
        //Comprobando que los parámetros se han escrito bien
        //Buscando que la función se encuentre en la tabla de símbolos:
        if(!tablaDeSimbolos.existeUnElemento(temporal.getString("name"),1)){
            //No existe el elemento
            agregarTextoError(
                    idioma.traerTexto("FuncionNoEncontrada")+
                            temporal.getString("name")
            );
            return false;
        }
        //Comprobando que tenga el mismo parámetro de entrada.
        if(tablaDeSimbolos.valorDeUnIdentificador(temporal.getString("name"),1)==-999999999){
            agregarTextoError(
                    idioma.traerTexto("NoSeEncontroValorID")
                            .replace("VAR",temporal.getString("identifier"))
            );
            return false;
        }
        if(!(tablaDeSimbolos.valorDeUnIdentificador(temporal.getString("name"),1) == temporal.getJSONArray("parameter").length())){
            //No tienen el mismo número de parámetros
            agregarTextoError(
                    idioma.traerTexto("NumeroDeParametrosDiferente")
            );
            return false;
        }
        ///Creando nueva tabla de símbolos y guardando la anterior en una pila
        pilaDePilas.add(tablaDeSimbolos);
        tablaDeSimbolos = new TablaDeSimbolos();
        funcionesATablaDeSimbolos();
        //Declarando las variables:
        for(int i=0;i<ListaDeFunciones.length();i++){
            //Buscando la función con parámetros:
            if(Objects.equals(ListaDeFunciones.getJSONObject(i).getString("name"), temporal.getString("name"))){
                JSONObject declarations = (JSONObject) ListaDeFunciones.get(i);
                ArrayList<JSONObject> list = (ArrayList<JSONObject>) declarations.get("parameter");
                for(int j=0;j<temporal.getJSONArray("parameter").length();j++){
                    JSONObject declaracion = (JSONObject) temporal.getJSONArray("parameter").get(j);
                    JSONObject tmp = list.get(j);
                    declaracion.put("identifier",tmp.getString("identifier"));
                    if(!DECLARAVARIABLE(
                            declaracion,
                            "main"
                    )){
                        return false;
                    }
                }
                //Resolviendo el programa
                boolean resultado = SresolverSubPrograma(declarations.getJSONArray("subprogram"));
                if(!resultado){
                    return false;
                }
            }
        }
        tablaDeSimbolos = pilaDePilas.pop();

        agregarTexto(
                idioma.traerTexto("InstruccionFuncion")+
                        temporal.getString("name")+
                        "() "+
                        idioma.traerTexto("Ejecutada")
        );

        return true;
    }

    private boolean TERMINARBLOQUE() {
        agregarTexto(
                idioma.traerTexto("TerminarBloqueUsuario")
        );
        agregarTexto(
                idioma.traerTexto("TerminarBloque")+
                        idioma.traerTexto("Ejecutada")
        );
        return true;
    }

    private boolean REPITEHASTA(JSONObject temporal) {
        String texto =
                idioma.traerTexto("InstruccionREPITEHASTA")+
                        idioma.traerTexto("Ejecutada")
                ;
        while(evaluarCondicion(temporal.getJSONObject("condition"))){
            boolean resultado = SresolverSubPrograma(temporal.getJSONArray("subprogram"));
            if(!resultado){
                return false;
            }
        }
        agregarTexto(texto);
        return true;
    }

    private boolean SresolverSubPrograma(JSONArray subprogram) {
        String IDSolucion = "subprogram" + Math.random();
        boolean resultado = resolverPrograma(IDSolucion,subprogram);
        tablaDeSimbolos.eliminarElementosPorID(IDSolucion);
        return resultado;
    }

    private boolean HACER(JSONObject temporal) {
        String texto =
                idioma.traerTexto("InstruccionHACER")+
                        idioma.traerTexto("Ejecutada")
        ;
        do{
            boolean resultado = SresolverSubPrograma(temporal.getJSONArray("subprogram"));
            if(!resultado){
                return false;
            }
        }while(evaluarCondicion(temporal.getJSONObject("condition")));
        agregarTexto(texto);
        return true;
    }

    private boolean FOR(JSONObject temporal) {
        //Ejecutando declaraciones:
        String idVariablesTemporales = "for" + Math.random();
        //Modificando variables existentes y creando variables nuevas:
        JSONObject tmp ;
        String texto =
                idioma.traerTexto("InstruccionFOR")+
                        idioma.traerTexto("Ejecutada")
                ;
        ArrayList<JSONObject> declarations = (ArrayList<JSONObject>) temporal.get("declarations");
        for (JSONObject declaration : declarations) {
            tmp = declaration;
            if (tmp.getInt("type") == 1) {
                //Modificar Variable (Modifica tabla de símbolos)
                //Buscando en Tabla de Símblos
                this.MODIFICAVARIABLE(tmp);
            } else if (tmp.getInt("type") == 2) {
                //Crear una variable temporal
                tmp = tmp.getJSONObject("valueFrom").put("identifier", tmp.getString("identifier"));
                this.DECLARAVARIABLE(tmp, idVariablesTemporales);
            }
        }
        //Evaluando Condición
        declarations = (ArrayList<JSONObject>) temporal.get("increments");
        while(evaluarCondicion(temporal.getJSONObject("condition"))){
            //Realizar instrucciones
            boolean resultado = SresolverSubPrograma(temporal.getJSONArray("subprogram"));
            if(!resultado){
                return false;
            }
            //Realizar incrementos o decrementos
            for (JSONObject declaration : declarations) {
                tmp = declaration;
                if (tablaDeSimbolos.existeUnElemento(
                        tmp.getString("identifier"),
                        0
                )) {
                    //El elemento a modificar fue declarado fuera del for
                    if (tmp.getInt("type") == 1) {
                        if (!(INCREMENTAVARIABLE(tmp))) {
                            return false;
                        }
                    } else if (tmp.getInt("type") == 2) {
                        if (!(DECREMENTAVARIABLE(tmp))) {
                            return false;
                        }
                    }
                }
            }
        }
        tablaDeSimbolos.eliminarElementosPorID(idVariablesTemporales);
        agregarTexto(texto);
        return true;
    }

    private boolean COMPARAR(JSONObject temporal) {
        int valorComparacion = 0;
        String texto =
            idioma.traerTexto("InstruccionCOMPARAR")+
                    idioma.traerTexto("Ejecutada")
        ;
        if(temporal.getInt("type")==1){
            if(tablaDeSimbolos.existeUnElemento(
                    temporal.getString("identifier"),
                    0
            )){
                valorComparacion = tablaDeSimbolos.valorDeUnIdentificador(
                        temporal.getString("identifier"),
                        0
                );
                if(valorComparacion==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",temporal.getString("identifier"))
                    );
                    return false;
                }
            }else{
                agregarTexto(texto);
                agregarTextoError(
                        idioma.traerTexto("ElValorError")+
                                temporal.getString("identifier") +
                                idioma.traerTexto("NoPuedeContinuar")
                );
                return false;
            }
        }else if(temporal.getInt("type")==2){
            valorComparacion = this.resolverQueHayFrente();
        }

        //Evalúa las condiciones:
        JSONObject tmp = temporal.getJSONObject("cases");
        while(!tmp.isEmpty()){
            if(compararCondicion(valorComparacion,tmp.getJSONObject("condition"))){
                boolean resultado = SresolverSubPrograma(tmp.getJSONArray("subprogram"));
                if(!resultado){
                    return false;
                }
                if(tmp.getBoolean("end")){
                    agregarTexto(texto);
                    return true;
                }
            }
            tmp = tmp.getJSONObject("continue");
        }

        //Realiza el caso por default:
        tmp = temporal.getJSONObject("default");
        boolean resultado = SresolverSubPrograma(tmp.getJSONArray("subprogram"));
        if(!resultado){
            return false;
        }
        agregarTexto(texto);
        return true;
    }

    private boolean compararCondicion(int valorComparacion, JSONObject condition) {
        int valorDerecho = 0;
        String operador = condition.getString("operator");
        if(condition.getInt("type")==0){
            valorDerecho = condition.getInt("value");
        }else if(condition.getInt("type")==1){
            JSONObject tmp = condition.getJSONObject("valueFrom");
            valorDerecho = generarAleatorio(tmp.getInt("from"), tmp.getInt("to"));
        }

        return condicionComparacionCumple(valorComparacion,operador,valorDerecho);
    }

    private boolean condicionComparacionCumple(int valorComparacion, String operador, int valorDerecho) {
        switch (operador){
            case "<":
                return valorComparacion < valorDerecho;
            case "<=":
                return valorComparacion <= valorDerecho;
            case ">":
                return valorComparacion > valorDerecho;
            case ">=":
                return valorComparacion >= valorDerecho;
            case "!=":
                return valorComparacion != valorDerecho;
            default:
                return valorComparacion == valorDerecho;
        }
    }

    private boolean SI(JSONObject temporal) {
        boolean condicion = this.evaluarCondicion(temporal.getJSONObject("condition"));
        String texto =
                idioma.traerTexto("InstruccionSI")+
                        idioma.traerTexto("Ejecutada")
        ;
        if(condicion){
            boolean resultado = SresolverSubPrograma(temporal.getJSONArray("subprogram"));
            if(!resultado){
                return false;
            }
            agregarTexto(texto);
            return true;
        }else if(temporal.getJSONArray("continue").length() != 0){
            //Aún hay más por continuar.
            JSONObject tmp;
            for(int i = temporal.getJSONArray("continue").length() -1 ; i >= 0 ; i--){
                tmp = (JSONObject) temporal.getJSONArray("continue").get(i);
                //Verificando que no sea un else simple
                if(Objects.equals(tmp.getString("instruction"), "SINO")){
                    boolean resultado = SresolverSubPrograma(tmp.getJSONArray("subprogram"));
                    if(!resultado){
                        return false;
                    }
                    agregarTexto(texto);
                    return true;
                }
                //Realiza los else if
                condicion = this.evaluarCondicion(tmp.getJSONObject("condition"));
                if(condicion){
                    boolean resultado = SresolverSubPrograma(temporal.getJSONArray("subprogram"));
                    if(!resultado){
                        return false;
                    }
                    agregarTexto(texto);
                    return true;
                }
            }
        }
        return true;
    }

    private boolean evaluarCondicion(JSONObject condition) {
        int valorIzquierdo = 0;
        int valorDerecho = 0;
        String tipoOperacion = "";
        switch (condition.getInt("type")){
            case 0:
                //Siempre falso o verdadero
                switch (condition.getString("value")){
                    case "true": case "verdadero":
                        return true;
                    case "false": case "falso":
                        return false;
                }
                break;
            case 1:
                //El valor proviene de un identificador
                valorIzquierdo = tablaDeSimbolos.valorDeUnIdentificador(
                        condition.getString("identifier"),
                        0
                );
                if(valorIzquierdo==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",condition.getString("identifier"))
                    );
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case 2:
                //El valor proviene de un número
                valorIzquierdo = condition.getInt("value");
                break;
            case 3:
                //El valor proviene de un número aleatorio
                valorIzquierdo = generarAleatorio(
                        condition.getJSONObject("valueFrom").getInt("from"),
                        condition.getJSONObject("valueFrom").getInt("to")
                );
                break;
            case 4:
                //comparación en paréntesis
                if(condition.getBoolean("value")){
                    //Se tiene que negar al final.
                    return !evaluarCondicion(condition.getJSONObject("valueFrom"));
                }else{
                    return evaluarCondicion(condition.getJSONObject("valueFrom"));
                }
            case 5:
                //comparación lógica
                boolean izquierda = evaluarCondicion(condition.getJSONObject("valueFrom"));
                boolean derecha = evaluarCondicion(condition.getJSONObject("compareWith"));
                String OperadorLogico = condition.getString("identifier");
                if(Objects.equals(OperadorLogico, "or")){
                    return (izquierda || derecha);
                }else if(Objects.equals(OperadorLogico, "and")){
                    return (izquierda && derecha);
                }
                break;
            case 6:
                //Pregunta si hay muro en frente
                return resolverMuroFrente();
            case 7:
                //Pregunta si hay muro en frente
                return resolverObjetoFrente();
            case 8:
                //Pregunta si hay muro en frente
                return resolverKaboomFrente();
            case 9:
                //Pregunta si hay muro en frente
                valorIzquierdo = resolverQueHayFrente();
                break;
        }

        condition = condition.getJSONObject("compareWith");
        switch (condition.getInt("type")){
            case 1:
                //El valor proviene de un identificador
                valorDerecho = tablaDeSimbolos.valorDeUnIdentificador(
                        condition.getString("identifier"),
                        0
                );
                if(valorDerecho==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",condition.getString("identifier"))
                    );
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                tipoOperacion = condition.getString("operator");
                break;
            case 2:
                //El valor proviene de un número
                valorDerecho = condition.getInt("value");
                tipoOperacion = condition.getString("operator");
                break;
            case 3:
                //El valor proviene de un número aleatorio
                valorDerecho = generarAleatorio(
                        condition.getJSONObject("valueFrom").getInt("from"),
                        condition.getJSONObject("valueFrom").getInt("to")
                );
                tipoOperacion = condition.getString("operator");
                break;
            case 9:
                //Pregunta si hay muro en frente
                valorDerecho = resolverQueHayFrente();
                break;
        }

        if(Objects.equals(tipoOperacion, "<")){
            return (valorIzquierdo < valorDerecho);
        }else if(Objects.equals(tipoOperacion, "<=")){
            return (valorIzquierdo <= valorDerecho);
        }else if(Objects.equals(tipoOperacion, ">")){
            return (valorIzquierdo > valorDerecho);
        }else if(Objects.equals(tipoOperacion, ">=")){
            return (valorIzquierdo >= valorDerecho);
        }else if(Objects.equals(tipoOperacion, "==")){
            return (valorIzquierdo == valorDerecho);
        }else if(Objects.equals(tipoOperacion, "!=")){
            return (valorIzquierdo != valorDerecho);
        }
        return false;
    }

    private boolean resolverKaboomFrente() {
        return resolverQueHayFrente()==3;
    }

    private boolean resolverObjetoFrente() {
        return resolverQueHayFrente()==2;
    }

    private boolean resolverMuroFrente() {
        return resolverQueHayFrente() == 1 || resolverQueHayFrente() == 4;
    }

    private int resolverQueHayFrente() {
        //Comprobando que no sea tope de mapa
        if(FilaEnFrente()==FilaPersonaje && ColumnaEnFrente()==ColumnaPersonaje){
            //Esta al tope del mapa (No puede avanzar)
            return 4;
        }
        switch(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]){
            case 0: case 9: case 10: case 11: case 12: case 14:
                //No hay obstáculo
                return 0;
            case 5: case 26: case 27: case 28: case 29: case 30:
                //El objeto es la meta
                return 5;
            case 6:
                //El objeto es una bomba
                return 3;
            case 7: case 15: case 16: case 17: case 18: case 19:
                //El objeto es un objeto que se puede mover
                return 2;
            case 8:
                //El objeto fijo
                return 1;
        }
        return -100;
    }

    private boolean IMPRIMIRCADENA(JSONObject temporal) {
        StringBuilder formarCadena = new StringBuilder();
        JSONObject temporal2;
        for(int i=0; i<temporal.getJSONArray("parameter").length();i++){
            temporal2 = temporal.getJSONArray("parameter").getJSONObject(i);
            switch (temporal2.getInt("type")){
                case 0:
                    if(tablaDeSimbolos.valorDeUnIdentificador(temporal2.getString("valueFrom"), 0)==-999999999){
                        agregarTextoError(
                                idioma.traerTexto("NoSeEncontroValorID")
                                        .replace("VAR",temporal2.getString("valueFrom"))
                        );
                        return false;
                    }
                    formarCadena.append(tablaDeSimbolos.valorDeUnIdentificador(
                            temporal2.getString("valueFrom"),
                            0
                    ));
                case 1:
                    formarCadena.append(temporal2.getString("string"));
            }
        }
        agregarTexto(formarCadena.toString());
        return true;
    }

    private void funcionesATablaDeSimbolos() {
        JSONObject temporal;
        ArrayList temporal2;
        for(int i=0;i<JSONPrograma.getJSONArray("functions").length();i++){
            temporal = JSONPrograma.getJSONArray("functions").getJSONObject(i);
            temporal2 = (ArrayList) temporal.get("parameter");
            tablaDeSimbolos.agregarElementoATabla(
                    temporal.getString("name"),
                    temporal2.size(),
                    "funciones",
                    1
            );
        }
    }

    private boolean DECREMENTAVARIABLE(JSONObject temporal) {

        int repetir = tablaDeSimbolos.valorDeUnIdentificador(
                temporal.getString("identifier"),
                0
        );
        repetir-=1;
        agregarTexto(
                temporal.getString("identifier")+
                        "-- " +
                        idioma.traerTexto("Ejecutada")
        );
        return tablaDeSimbolos.modificarElementoATabla(
                temporal.getString("identifier"),
                repetir,
                0
        );
    }

    private boolean INCREMENTAVARIABLE(JSONObject temporal) {
        int repetir = tablaDeSimbolos.valorDeUnIdentificador(
                temporal.getString("identifier"),
                0
        );
        repetir+=1;
        agregarTexto(
                temporal.getString("identifier")+
                        "++ " +
                        idioma.traerTexto("Ejecutada")
        );
        return tablaDeSimbolos.modificarElementoATabla(
                temporal.getString("identifier"),
                repetir,
                0
        );
    }

    private boolean DESACTIVARKABOOM() {
        String texto;
        if(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] == 6){
            MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 7;
            texto=
                    idioma.traerTexto("InstruccionKaboomDesactivada")
            ;
        }else{
            texto=
                    idioma.traerTexto("NingunaBomba")
            ;
        }
        actualizarMapaString(texto);
        return true;
    }

    private boolean ELIMINAR() {
        String texto;
        int objetoFrente = MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()];
        switch (objetoFrente){
            case 6:
                MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = 13;
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 13;
                agregarTextoError(idioma.traerTexto("TocasteKaboom"));
                actualizarMapa();
                return false;
            case 7:
                //Elimina el objeto en frente de nuestro personaje
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 20 ;
                actualizarMapa();
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 0 ;
                break;
            case 15:
                //Elimina el objeto en frente de nuestro personaje
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 21 ;
                actualizarMapa();
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 9 ;
                break;
            case 16:
                //Elimina el objeto en frente de nuestro personaje
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 22 ;
                actualizarMapa();
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 10 ;
                break;
            case 17:
                //Elimina el objeto en frente de nuestro personaje
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 23 ;
                actualizarMapa();
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 11 ;
                break;
            case 18:
                //Elimina el objeto en frente de nuestro personaje
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 24 ;
                actualizarMapa();
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 12 ;
                break;
            case 19:
                //Elimina el objeto en frente de nuestro personaje
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 25 ;
                actualizarMapa();
                MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 14 ;
                break;
            default:
                actualizarMapaString(idioma.traerTexto("NingunObjeto"));
        }
        texto=
                idioma.traerTexto("InstruccionEliminarObjeto")+
                        idioma.traerTexto("Ejecutada")
        ;
        actualizarMapaString(texto);
        return true;
    }

    private boolean SOLTAR() {
        String texto = "";
        if(!tengoObjeto){
            texto=
                    idioma.traerTexto("SinObjeto")
            ;
            actualizarMapaString(texto);
            return true;
        }
        if(tengoEspacioParaDejarObjeto()){
            int colorPintado = MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()];
            switch (colorPintado){
                case 0:
                    //No hay Nada en frente
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 7;
                    break;
                case 6:
                    //Kaboom
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = 13;
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 13;
                    agregarTextoError(idioma.traerTexto("TocasteKaboom"));
                    actualizarMapa();
                    return false;
                case 9:
                    //No hay Nada en frente y el suelo es de color rojo
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 15;
                    break;
                case 10:
                    //No hay Nada en frente y el suelo es de color azul
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 16;
                    break;
                case 11:
                    //No hay Nada en frente y el suelo es de color verde
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 17;
                    break;
                case 12:
                    //No hay Nada en frente y el suelo es de color amarillo
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 18;
                    break;
                case 14:
                    //No hay Nada en frente y el suelo es de color orange
                    MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 19;
                    break;
                default:
                    //Hay una bandera (5,26-30), Un objeto (7,15-19), Un objeto inaccesible (8)
                    actualizarMapaString(idioma.traerTexto("SinEspacio"));
                    return true;
            }
            tengoObjeto = false;
            texto=
                    idioma.traerTexto("InstruccionSoltar")+
                            idioma.traerTexto("Ejecutada")
            ;
        }/*else{
            texto=
                    idioma.traerTexto("SinEspacio")
            ;
            tengoObjeto = true;
        }////*/
        actualizarMapaString(texto);
        return true;
    }

    private boolean tengoEspacioParaDejarObjeto(){
        switch (Personaje){
            case 1:
                //Mirando al norte
                return FilaPersonaje != 0;
            case 2:
                //Mirando al sur
                return FilaPersonaje != (Filas - 1);
            case 3:
                //Mirando al este
                return ColumnaPersonaje != (Columnas - 1);
            case 4:
                //Mirando al oeste
                return ColumnaPersonaje != 0;
        }
        return false;
    }

    private boolean TOMAR() {
        String texto;
        if(tengoObjeto){
            //El personaje ya tiene un objeto, no puede tener 2 a la vez
            texto=
                    idioma.traerTexto("YaTengoObjeto")
            ;
        }else if(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] == 6){
            //Toma una bomba, el juego termina
            MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = 13;
            MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 13;
            texto=
                    idioma.traerTexto("TomasteKaboom")
            ;
            agregarTextoError(texto);
            actualizarMapa();
            return false;
        }else if(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] != 7){
            //No hay objeto que tomar
            texto=
                    idioma.traerTexto("NingunObjeto")
            ;
        }else{
            //Toma el objeto en frente de nuestro personaje
            tengoObjeto = true;
            MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 0 ;
            texto=
                    idioma.traerTexto("InstruccionObjeto")+
                            idioma.traerTexto("Ejecutada")
            ;
        }
        actualizarMapaString(texto);
        return true;
    }

    private boolean DEJAPINTAR() {
        pintura = -1;
        agregarTexto(
                idioma.traerTexto("InstruccionDejarPintar")+
                        idioma.traerTexto("Ejecutada")
        );
        return true;
    }

    private boolean PINTAR(JSONObject temporal) {
        String texto =
                idioma.traerTexto("InstruccionPINTAR")+
                        idioma.traerTexto("Ejecutada")
        ;
        boolean haybandera = (DebajoDelPersonaje==5)||(DebajoDelPersonaje==26)||(DebajoDelPersonaje==27)||
                (DebajoDelPersonaje==28)||(DebajoDelPersonaje==29)||(DebajoDelPersonaje==30);
        switch (temporal.getString("color")){
            case "azul": case "blue":
                if(haybandera){
                    pintura = 27;
                }else{
                    pintura = 10;
                }
                break;
            case "red": case "rojo":
                if(haybandera){
                    pintura = 26;
                }else{
                    pintura = 9;
                }
                break;
            case "verde": case "green":
                if(haybandera){
                    pintura = 28;
                }else{
                    pintura = 11;
                }
                break;
            case "amarillo": case "yellow":
                if(haybandera){
                    pintura = 29;
                }else{
                    pintura = 12;
                }
                break;
            default:
                if(haybandera){
                    pintura = 30;
                }else{
                    pintura = 14;
                }
        }
        DebajoDelPersonaje = pintura;
        agregarTexto(texto);
        actualizarMapa();
        return true;
    }

    private boolean IMPRIMIRVARIABLE(JSONObject temporal) {
        int valorDeLaVariable = tablaDeSimbolos.valorDeUnIdentificador(
                temporal.getString("identifier"),
                0
        );
        if(valorDeLaVariable==-999999999){
            agregarTextoError(
                    idioma.traerTexto("NoSeEncontroValorID")
                            .replace("VAR",temporal.getString("identifier"))
            );
            return false;
        }
        agregarTexto(
                idioma.traerTexto("ElValorDeLaVariable")+
                        temporal.getString("identifier")+
                        idioma.traerTexto("EsElValor")+
                        valorDeLaVariable
        );
        return true;
    }

    private boolean ESPERA(JSONObject temporal) {
        temporal = temporal.getJSONObject("parameter");
        int repetir;

        switch (temporal.getInt("type")) {
            case 0:
                agregarTexto(
                        idioma.traerTexto("Esperando")+
                                "1"+
                                idioma.traerTexto("Segundos")
                );
                esperaTiempo();
                break;
            case 1:
                //El valor viene de un identificador
                repetir = tablaDeSimbolos.valorDeUnIdentificador(
                        temporal.getString("identifier"),
                        0
                );
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",temporal.getString("identifier"))
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    agregarTexto(
                            idioma.traerTexto("Esperando")+
                                    repetir+
                                    idioma.traerTexto("Segundos")
                    );
                    esperaTiempo();
                }
                break;
            case 2:
                //El valor viene de un valor numérico
                repetir = temporal.getInt("value");
                agregarTexto(
                        idioma.traerTexto("Esperando")+
                                repetir+
                                idioma.traerTexto("Segundos")
                );
                for(int i=0;i<repetir;i++){
                    esperaTiempo();
                }
                break;
            case 3:
                //El valor viene de un número aleatorio
                temporal = temporal.getJSONObject("valueFrom");
                repetir = generarAleatorio(temporal.getInt("from"),temporal.getInt("to"));
                agregarTexto(
                        idioma.traerTexto("Esperando")+
                                repetir+
                                idioma.traerTexto("Segundos")
                );
                for(int i=0;i<repetir;i++){
                    esperaTiempo();
                }
                break;
            case 4:
                //El valor viene de una operación aritmética
                temporal = temporal.getJSONObject("valueFrom");
                repetir = resolverOperacionAritmetica(temporal);
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("ErrorOperacionAritmetica")
                    );
                    return false;
                }
                agregarTexto(
                        idioma.traerTexto("Esperando")+
                                repetir+
                                idioma.traerTexto("Segundos")
                );
                for(int i=0;i<repetir;i++){
                    esperaTiempo();
                }
                break;
        }
        return true;
    }

    private void esperaTiempo() {
        try {
            Thread.sleep(idioma.getConfigInteger("TiempoInstruccion"));
        } catch (InterruptedException e) {
            this.lblResultados.setText(MapaHTML);
        }
    }

    private boolean IZQUIERDA(JSONObject temporal) {
        temporal = temporal.getJSONObject("parameter");
        int repetir;

        String texto =
                    idioma.traerTexto("InstruccionIzquierda")+
                            idioma.traerTexto("Ejecutada")
                ;

        switch (temporal.getInt("type")) {
            case 0:
                Personaje = getIzquierda();
                MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                actualizarMapaString(texto);
                break;
            case 1:
                //El valor viene de un identificador
                repetir = tablaDeSimbolos.valorDeUnIdentificador(
                        temporal.getString("identifier"),
                        0
                );
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",temporal.getString("identifier"))
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    Personaje = getIzquierda();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
            case 2:
                //El valor viene de un valor numérico
                repetir = temporal.getInt("value");
                for(int i=0;i<repetir;i++){
                    Personaje = getIzquierda();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
            case 3:
                //El valor viene de un número aleatorio
                temporal = temporal.getJSONObject("valueFrom");
                repetir = generarAleatorio(temporal.getInt("from"),temporal.getInt("to"));
                for(int i=0;i<repetir;i++){
                    Personaje = getIzquierda();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
            case 4:
                //El valor viene de una operación aritmética
                temporal = temporal.getJSONObject("valueFrom");
                repetir = resolverOperacionAritmetica(temporal);
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("ErrorOperacionAritmetica")
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    Personaje = getIzquierda();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
        }
        return true;
    }

    private boolean DERECHA(JSONObject temporal) {
        temporal = temporal.getJSONObject("parameter");
        int repetir;

        String texto =
                idioma.traerTexto("InstruccionDerecha")+
                        idioma.traerTexto("Ejecutada")
                ;

        switch (temporal.getInt("type")) {
            case 0:
                Personaje = getDerecha();
                MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                actualizarMapaString(texto);
                break;
            case 1:
                //El valor viene de un identificador
                repetir = tablaDeSimbolos.valorDeUnIdentificador(
                        temporal.getString("identifier"),
                        0
                );
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",temporal.getString("identifier"))
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    Personaje = getDerecha();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
            case 2:
                //El valor viene de un valor numérico
                repetir = temporal.getInt("value");
                for(int i=0;i<repetir;i++){
                    Personaje = getDerecha();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
            case 3:
                //El valor viene de un número aleatorio
                temporal = temporal.getJSONObject("valueFrom");
                repetir = generarAleatorio(temporal.getInt("from"),temporal.getInt("to"));
                for(int i=0;i<repetir;i++){
                    Personaje = getDerecha();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
            case 4:
                //El valor viene de una operación aritmética
                temporal = temporal.getJSONObject("valueFrom");
                repetir = resolverOperacionAritmetica(temporal);
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("ErrorOperacionAritmetica")
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    Personaje = getDerecha();
                    MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = Personaje;
                    actualizarMapaString(texto);
                }
                break;
        }
        return true;
    }

    private int getDerecha(){
        switch (Personaje){
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 2;
            case 4:
                return 1;
        }
        return -1;
    }

    private int getIzquierda(){
        switch (Personaje){
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 1;
            case 4:
                return 2;
        }
        return -1;
    }

    private boolean AVANZAR(JSONObject temporal) {
        temporal = temporal.getJSONObject("parameter");
        int repetir;

        String texto =
                idioma.traerTexto("InstruccionAvanzar")+
                        "() "+
                        idioma.traerTexto("Ejecutada")
        ;

        switch (temporal.getInt("type")) {
            case 0:
                if(!avanzarSimple()){
                    actualizarMapaString(texto);
                    return false;
                }
                actualizarMapaString(texto);
                break;
            case 1:
                //El valor viene de un identificador
                repetir = tablaDeSimbolos.valorDeUnIdentificador(
                        temporal.getString("identifier"),
                        0
                );
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",temporal.getString("identifier"))
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    if(!avanzarSimple()){
                        actualizarMapaString(texto);
                        return false;
                    }
                    actualizarMapaString(texto);
                }
                break;
            case 2:
                //El valor viene de un valor numérico
                repetir = temporal.getInt("value");
                for(int i=0;i<repetir;i++){
                    if(!avanzarSimple()){
                        actualizarMapaString(texto);
                        return false;
                    }
                    actualizarMapaString(texto);
                }
                break;
            case 3:
                //El valor viene de un número aleatorio
                temporal = temporal.getJSONObject("valueFrom");
                repetir = generarAleatorio(temporal.getInt("from"),temporal.getInt("to"));
                for(int i=0;i<repetir;i++){
                    if(!avanzarSimple()){
                        actualizarMapaString(texto);
                        return false;
                    }
                    actualizarMapaString(texto);
                }
                break;
            case 4:
                //El valor viene de una operación aritmética
                temporal = temporal.getJSONObject("valueFrom");
                repetir = resolverOperacionAritmetica(temporal);
                if(repetir==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("ErrorOperacionAritmetica")
                    );
                    return false;
                }
                for(int i=0;i<repetir;i++){
                    if(!avanzarSimple()){
                        actualizarMapaString(texto);
                        return false;
                    }
                    actualizarMapaString(texto);
                }
                break;
        }
        return true;
    }

    private boolean avanzarSimple(){
        //Sin valor, solo se mueve 1 vez
        if(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==6){
            //Es bomba (termina el juego)
            MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = DebajoDelPersonaje;
            MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = 13;
            actualizarMapa();
            agregarTextoError(
                    idioma.traerTexto("PersonajeMuerto")
            );
            return false;
        }else if(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==8 || MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==7){
            //Cualquier cosa menos espacios en blanco y banderas. (No se debe mover)
            agregarTexto(
                    idioma.traerTexto("ObjetoNoTraspasable")
            );
        }else if(((MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==5)||(DebajoDelPersonaje==26)||
                (MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==27)||(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==28)||
                (MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==29)||(MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()]==30))&&pintura!=-1){
            //Enfrente está una bandera.
            MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = DebajoDelPersonaje;
            switch (pintura){
                case 9:
                    DebajoDelPersonaje = 26;
                    break;
                case 10:
                    DebajoDelPersonaje = 27;
                    break;
                case 11:
                    DebajoDelPersonaje = 28;
                    break;
                case 12:
                    DebajoDelPersonaje = 29;
                    break;
                case 14:
                    DebajoDelPersonaje = 30;
                    break;
            }
            MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = Personaje;
            FilaPersonaje=FilaEnFrente();
            ColumnaPersonaje=ColumnaEnFrente();
        }else{
            MapaDeTrabajo[FilaPersonaje][ColumnaPersonaje] = DebajoDelPersonaje;
            if(pintura==-1){
                DebajoDelPersonaje = MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()];
            }else{
                DebajoDelPersonaje = pintura;
            }
            MapaDeTrabajo[FilaEnFrente()][ColumnaEnFrente()] = Personaje;
            FilaPersonaje=FilaEnFrente();
            ColumnaPersonaje=ColumnaEnFrente();
        }
        return true;
    }

    private void actualizarMapa() {
        generarMapaHTML();
        try {
            Thread.sleep(idioma.getConfigInteger("TiempoInstruccion"));
        } catch (InterruptedException e) {
            this.lblResultados.setText(MapaHTML);
        }
        this.lblResultados.setText(MapaHTML);
    }

    private void actualizarMapaString(String texto) {
        generarMapaHTML();
        try {
            Thread.sleep(idioma.getConfigInteger("TiempoInstruccion"));
        } catch (InterruptedException e) {
            this.lblResultados.setText(MapaHTML);
        }
        agregarTexto(texto);
        this.lblResultados.setText(MapaHTML);
    }

    private boolean MODIFICAVARIABLE(JSONObject temporal) {
        String texto =
                idioma.traerTexto("InstruccionMODIFICAVARIABLE")+
                        idioma.traerTexto("Ejecutada")
                ;
        //Verifica si existe:
        if (!tablaDeSimbolos.existeUnElemento(
                temporal.getString("identifier"),
                0)) {
            agregarTexto(texto);
            agregarTextoError(
                    idioma.traerTexto( "LaVariableExiste") +
                            temporal.getString("identifier") +
                            idioma.traerTexto( "NoEstaDeclarada")
            );
            return false;
        }

        String identificador = temporal.getString("identifier");
        temporal = temporal.getJSONObject("valueFrom");

        switch (temporal.getInt("type")) {
            case 1:
                //Valor numérico
                agregarTexto(texto);
                return tablaDeSimbolos.modificarElementoATabla(
                        identificador,
                        temporal.getInt("value"),
                        0
                );
            case 2:
                //Número Aleatorio
                JSONObject aleatorio = temporal.getJSONObject("random");
                agregarTexto(texto);
                return tablaDeSimbolos.modificarElementoATabla(
                        identificador,
                        generarAleatorio(aleatorio.getInt("from"), aleatorio.getInt("to")),
                        0
                );
            case 3:
                //Operación Aritmética
                int resultado = this.resolverOperacionAritmetica(temporal.getJSONObject("operation"));
                if (resultado == -999999999) {
                    agregarTexto(texto);
                    agregarTextoError(
                            idioma.traerTexto("OperacionError")
                    );
                    return false;
                }
                agregarTexto(texto);
                return tablaDeSimbolos.modificarElementoATabla(
                        identificador,
                        resultado,
                        0
                );
            case 4:
                //De otra variable.
                if (!tablaDeSimbolos.existeUnElemento(temporal.getString("valueFrom"),  0)) {
                    agregarTextoError(
                            idioma.traerTexto( "ErrorAlTraerValor") +
                                    temporal.getString("valueFrom") +
                                    idioma.traerTexto( "AsginarAlValor") +
                                    identificador
                    );
                    agregarTexto(texto);
                    return false;
                }
                agregarTexto(texto);
                return tablaDeSimbolos.modificarElementoATabla(
                        identificador,
                        tablaDeSimbolos.valorDeUnIdentificador(temporal.getString("valueFrom"), 0),
                        0
                );
            case 5:
                //El valor proviene de una función QUETENGODELANTE()
                agregarTexto(texto);
                return tablaDeSimbolos.modificarElementoATabla(
                        identificador,
                        resolverQueHayFrente(),
                        0
                );
        }
        agregarTexto(texto);
        return true;
    }

    private boolean DECLARAVARIABLE(JSONObject temporal,String funcion) {
        //Comprobando que no exista el identificador a agregar
        //Una variable temporal no debe estar declarada antes por ningún otro lado. No puede haber 2 variables a la vez con el mismo nombre.
        if (tablaDeSimbolos.existeUnElemento(temporal.getString("identifier"),  0)) {
            agregarTexto(
                    idioma.traerTexto("InstruccionDECLARAVARIABLE")+
                            idioma.traerTexto("Ejecutada")
            );
            agregarTextoError(
                    idioma.traerTexto( "LaVariableExiste") +
                            temporal.getString("identifier") +
                            idioma.traerTexto( "EstaDeclarada")
            );
            return false;
        }
        switch (temporal.getInt("type")) {
            case 0:
                //No hay valor de asignación
                tablaDeSimbolos.agregarElementoATabla(
                        temporal.getString("identifier"),
                        0,
                        funcion,
                        0
                );
                break;
            case 1:
                //Valor numérico
                tablaDeSimbolos.agregarElementoATabla(
                        temporal.getString("identifier"),
                        temporal.getInt("value"),
                        funcion,
                        0
                );
                break;
            case 2:
                //Número Aleatorio
                JSONObject aleatorio = temporal.getJSONObject("random");
                tablaDeSimbolos.agregarElementoATabla(
                        temporal.getString("identifier"),
                        generarAleatorio(aleatorio.getInt("from"), aleatorio.getInt("to")),
                        funcion,
                        0
                );
                break;
            case 3:
                //Operación Aritmética
                int resultado = this.resolverOperacionAritmetica(temporal.getJSONObject("operation"));
                if(resultado==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("ErrorOperacionAritmetica")
                    );
                    return false;
                }
                tablaDeSimbolos.agregarElementoATabla(
                        temporal.getString("identifier"),
                        resultado,
                        funcion,
                        0
                );
                break;
            case 4:
                //De otra variable.
                if (!tablaDeSimbolos.existeUnElemento(temporal.getString("valueFrom"),  0)) {
                    agregarTextoError(
                            idioma.traerTexto( "ErrorAlTraerValor") +
                                    temporal.getString("valueFrom") +
                                    idioma.traerTexto( "AsginarAlValor") +
                                    temporal.getString("identifier")
                    );
                    return false;
                }
                tablaDeSimbolos.agregarElementoATabla(
                        temporal.getString("identifier"),
                        tablaDeSimbolos.valorDeUnIdentificador(temporal.getString("valueFrom"),  0),
                        funcion,
                        0
                );
                break;
            case 5:
                //El valor proviene de una función QUETENGODELANTE()
                tablaDeSimbolos.agregarElementoATabla(
                        temporal.getString("identifier"),
                        resolverQueHayFrente(),
                        funcion,
                        0
                );
                break;
        }
        return true;
    }

    private int resolverOperacionAritmetica(JSONObject operation) {
        String OperacionAEvaluar = getStringOperacion(operation);
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine textoAOperacion = mgr.getEngineByName("JavaScript");
        if (!OperacionAEvaluar.contains("ERROR")) {
            try {
                float flotante = Float.parseFloat(textoAOperacion.eval(OperacionAEvaluar).toString());
                return Math.round(flotante);
            } catch (Exception exe) {
                return -999999999;
            }
        }
        return -999999999;
    }

    private String getStringOperacion(JSONObject operation) {
        String OperacionARealizar = "";
        JSONObject tmp;

        switch (operation.getInt("type")) {
            case 0:
                //Usuario abrió un paréntesis
                if (Objects.equals(operation.getString("operator"), "")) {
                    OperacionARealizar += operation.getString("operator") + "(";
                    if (!Objects.equals(operation.getJSONObject("operation").toString(), "{}")) {
                        OperacionARealizar += getStringOperacion(operation.getJSONObject("operation"));
                    }
                    OperacionARealizar += ")";
                } else {
                    OperacionARealizar += operation.getString("operator");
                    if (!Objects.equals(operation.getJSONObject("operation").toString(), "{}")) {
                        OperacionARealizar += getStringOperacion(operation.getJSONObject("operation"));
                    }
                }
                if (!Objects.equals(operation.getJSONObject("random").toString(), "{}")) {
                    OperacionARealizar += getStringOperacion(operation.getJSONObject("random"));
                }
                break;
            case 1:
                //Es un número simple
                OperacionARealizar += operation.getString("operator");
                OperacionARealizar += operation.getInt("value");
                if (!Objects.equals(operation.getJSONObject("operation").toString(), "{}")) {
                    OperacionARealizar += getStringOperacion(operation.getJSONObject("operation"));
                }
                break;
            case 2:
                //Es de un valor de una variable
                //Verificando que exista la variable:
                if (!tablaDeSimbolos.existeUnElemento(operation.getString("valueFrom"),  0)) {
                    agregarTextoError(
                            idioma.traerTexto( "ErrorAlTraerValorOperacion") +
                                    operation.getString("valueFrom")
                    );
                    OperacionARealizar += "ERROR";
                }
                OperacionARealizar += operation.getString("operator");
                OperacionARealizar += tablaDeSimbolos.valorDeUnIdentificador(operation.getString("valueFrom"),  0);
                if(tablaDeSimbolos.valorDeUnIdentificador(operation.getString("valueFrom"),0)==-999999999){
                    agregarTextoError(
                            idioma.traerTexto("NoSeEncontroValorID")
                                    .replace("VAR",operation.getString("valueFrom"))
                    );
                    return "ERROR";
                }
                if (!Objects.equals(operation.getJSONObject("operation").toString(), "{}")) {
                    OperacionARealizar += getStringOperacion(operation.getJSONObject("operation"));
                }
                break;
            case 3:
                //Numero aleatorio
                tmp = operation.getJSONObject("random");
                OperacionARealizar += operation.getString("operator");
                OperacionARealizar += generarAleatorio(tmp.getInt("from"), tmp.getInt("to"));
                if (!Objects.equals(operation.getJSONObject("operation").toString(), "{}")) {
                    OperacionARealizar += getStringOperacion(operation.getJSONObject("operation"));
                }
                break;
        }
        return OperacionARealizar;
    }


    public void resetMapa() {
        MapaDeTrabajo = MapaBackend;
        actualizarMapaPorMovimientos();
    }

    private void actualizarMapaPorMovimientos() {
        generarMapaHTML();
        this.lblResultados.setText(this.MapaHTML);
    }

    private void generarMapaHTML() {
        MapaHTML="";
        for (int i = 0; i < Filas; i++) {
            MapaHTML += "<tr>";
            for (int j = 0; j < Columnas; j++) {
                switch (this.MapaDeTrabajo[i][j]) {
                    case 0:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">⬜</td>";
                        break;
                    case 1:
                        if (DebajoDelPersonaje == 9) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">⬆</td>";
                        } else if (DebajoDelPersonaje == 10) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">⬆</td>";
                        } else if (DebajoDelPersonaje == 11) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">⬆</td>";
                        } else if (DebajoDelPersonaje == 12) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">⬆</td>";
                        } else {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">⬆</td>";
                        }
                        break;
                    case 2:
                        if (DebajoDelPersonaje == 9) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">⬇</td>";
                        } else if (DebajoDelPersonaje == 10) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">⬇</td>";
                        } else if (DebajoDelPersonaje == 11) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">⬇</td>";
                        } else if (DebajoDelPersonaje == 12) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">⬇</td>";
                        } else {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">⬇</td>";
                        }
                        break;
                    case 3:
                        if (DebajoDelPersonaje == 9) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">➡</td>";
                        } else if (DebajoDelPersonaje == 10) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">➡</td>";
                        } else if (DebajoDelPersonaje == 11) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">➡</td>";
                        } else if (DebajoDelPersonaje == 12) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">➡</td>";
                        } else {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">➡</td>";
                        }
                        break;
                    case 4:
                        if (DebajoDelPersonaje == 9) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">⬅</td>";
                        } else if (DebajoDelPersonaje == 10) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">⬅</td>";
                        } else if (DebajoDelPersonaje == 11) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">⬅</td>";
                        } else if (DebajoDelPersonaje == 12) {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">⬅</td>";
                        } else {
                            MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">⬅</td>";
                        }
                        break;
                    case 5:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">🏁</td>";
                        break;
                    case 6:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">💣</td>";
                        break;
                    case 7:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">📦</td>";
                        break;
                    case 8:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">❌</td>";
                        break;
                    case 9:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">🟥</td>";
                        break;
                    case 10:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">🟦</td>";
                        break;
                    case 11:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">🟩</td>";
                        break;
                    case 12:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">🟨</td>";
                        break;
                    case 13:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">🔥</td>";
                        break;
                    case 14:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("COLORDEFAULT") + ";\">🟧</td>";
                        break;
                    case 15:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">📦</td>";
                        break;
                    case 16:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">📦</td>";
                        break;
                    case 17:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">📦</td>";
                        break;
                    case 18:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">📦</td>";
                        break;
                    case 19:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("COLORDEFAULT") + ";\">📦</td>";
                        break;
                    case 20:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("DEFAULT") + ";\">✨</td>";
                        break;
                    case 21:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">✨</td>";
                        break;
                    case 22:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">✨</td>";
                        break;
                    case 23:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">✨</td>";
                        break;
                    case 24:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">✨</td>";
                        break;
                    case 25:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("COLORDEFAULT") + ";\">✨</td>";
                        break;
                    case 26:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("RED") + ";\">🏁</td>";
                        break;
                    case 27:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("BLUE") + ";\">🏁</td>";
                        break;
                    case 28:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("GREEN") + ";\">🏁</td>";
                        break;
                    case 29:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("YELLOW") + ";\">🏁</td>";
                        break;
                    case 30:
                        MapaHTML += "<td style=\"background-color:" + idioma.traerColor("COLORDEFAULT") + ";\">🏁</td>";
                        break;
                    default:

                }
            }
            MapaHTML += "</tr>";
        }
        MapaHTML = "<html><body><br><table>" + MapaHTML + "</table><br></body></html>";
    }

    private int FilaEnFrente() {
        switch (Personaje) {
            case 1:
                if (FilaPersonaje != 0) {
                    return FilaPersonaje - 1;
                } else {
                    return FilaPersonaje;
                }
            case 2:
                if (FilaPersonaje != (Filas - 1)) {
                    return FilaPersonaje + 1;
                } else {
                    return FilaPersonaje;
                }
            default:
                return FilaPersonaje;
        }
    }

    private int ColumnaEnFrente() {
        switch (Personaje) {
            case 3:
                if (ColumnaPersonaje != (Columnas - 1)) {
                    return ColumnaPersonaje + 1;
                } else {
                    return ColumnaPersonaje;
                }
            case 4:
                if (ColumnaPersonaje != 0) {
                    return ColumnaPersonaje - 1;
                } else {
                    return ColumnaPersonaje;
                }
            default:
                return ColumnaPersonaje;
        }
    }

    private void verificaPersonaje() {
        for (int i = 0; i < Filas; i++) {
            for (int j = 0; j < Columnas; j++) {
                if ((MapaBackend[i][j] == 1) || (MapaBackend[i][j] == 2) || (MapaBackend[i][j] == 3) || (MapaBackend[i][j] == 4)) {
                    FilaPersonaje = i;
                    ColumnaPersonaje = j;
                    Personaje = MapaBackend[i][j];
                    return;
                }
            }
        }
        agregarTextoError(idioma.traerTexto( "PersonajeNoEcontrado"));
    }

    private void agregarTextoError(String texto) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date date = new Date();
        this.txtResultados.setText(txtResultados.getText() + "\n" + formatter.format(date) + texto);
        this.txtResultados.setForeground(Color.red);
    }

    private void agregarTexto(String texto) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date date = new Date();
        this.txtResultados.setText(txtResultados.getText() + "\n" + formatter.format(date) +  texto);
        txtResultados.setForeground(new Color(25, 111, 61));
    }

    private int generarAleatorio(int MIN, int MAX) {
        if (MIN == -1 && MAX == -1) {
            //Regresa el valor entre 1 y 100
            return (int) (Math.floor(Math.random() * (100 - 1 + 1)) + 1);
        } else {
            return (int) (Math.floor(Math.random() * (MAX - MIN + 1)) + MIN);
        }
    }

    private void imprimirMapa(){
        for(int i=0; i<Filas;i++){
            for(int j=0; j<Columnas;j++){
                System.out.print(MapaDeTrabajo[i][j]+",");
            }
            System.out.print("\n");
        }System.out.print("\n");
    }

    public void cleanMap() {
        tablaDeSimbolos.limpiarTablaDeSimbolos();
        //Se trabajará sobre Mapa de Trabajo, se guarda el original de MapaBackend
        DebajoDelPersonaje = 0;
        MapaDeTrabajo = generarMapaDeTrabajo(MapaBackend);
        //actualizarMapaPorMovimientos();
        tengoObjeto = false;
        actualizarMapa();
        verificaPersonaje();
        funcionesATablaDeSimbolos();
    }

    public void printHTMLMap(){
        generarMapaHTML();
        this.lblResultados.setText(MapaHTML);
    }
}
