package Compilador.Default.AnalizadorLexico;

import ConfiguraLenguaje.cargarTexto;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class AnalizarLexicoDefault {
    //resultadoDelAnalisis contendrá el objeto JSON con el resultado exitoso o con error
    private final JSONObject resultadoDelAnalisis  = new JSONObject();
    private final cargarTexto idioma = new cargarTexto("AnalizarLexico");
    //Getter de resultadoDelAnalisis
    public JSONObject getResultadoDelAnalisis() {
        return resultadoDelAnalisis;
    }
    public void EjecutarAnalisisLexico(String ProgramaFuente){
        this.AnalizarLexico(ProgramaFuente);
    }
    private void AnalizarLexico(String ProgramaFuente){

        //Instrucción para leer el programa fuente con el Analizador Léxico
        Lexer analisisLexico = new Lexer(new StringReader(ProgramaFuente));

        String tmp;
        ArrayList<JSONObject> lecturaTokens = new ArrayList<>();
        int numeroDeLinea = 1;
        while (true) {
            Tokens token;
            //Leyendo el token a analizar.
            try {
                token = analisisLexico.yylex();
            } catch (IOException Exception) {
                resultadoDelAnalisis.put("status",false);
                resultadoDelAnalisis.put("message",Exception);
                resultadoDelAnalisis.put("tokens",lecturaTokens);
                return;
            }

            if(token!=null){
                JSONObject objetoTemporal;
                switch (token) {
                    case ERROR:
                        resultadoDelAnalisis.put("status",false);
                        tmp = idioma.traerTexto("ErrorLexico") + analisisLexico.yytext() + idioma.traerTexto("Linea") + numeroDeLinea ;
                        resultadoDelAnalisis.put("message",tmp);
                        resultadoDelAnalisis.put("tokens",lecturaTokens);
                        return;
                    case SALTOLINEA:
                        numeroDeLinea++;
                        objetoTemporal = new JSONObject();
                        objetoTemporal.put("lexema",token.toString());
                        objetoTemporal.put("line",numeroDeLinea);
                        objetoTemporal.put("token", analisisLexico.yytext());
                        lecturaTokens.add(objetoTemporal);
                        break;
                    default:
                        objetoTemporal = new JSONObject();
                        objetoTemporal.put("lexema",token.toString());
                        objetoTemporal.put("line",numeroDeLinea);
                        objetoTemporal.put("token", analisisLexico.yytext());
                        lecturaTokens.add(objetoTemporal);
                        break;
                }
            }
            if (token == null) {
                //Se ha terminado de analizar el programa fuente con éxito.
                resultadoDelAnalisis.put("status",true);
                resultadoDelAnalisis.put("message",idioma.traerTexto("Exito"));
                resultadoDelAnalisis.put("tokens",lecturaTokens);
                return;
            }
        }
    }
}

