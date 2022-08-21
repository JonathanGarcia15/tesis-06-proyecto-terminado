package Compilador.EN.Compiler;

import ConfiguraLenguaje.cargarTexto;
import java_cup.runtime.Symbol;
import org.json.JSONObject;

import java.io.StringReader;

public class CompilerEN {
    private final JSONObject resultadoDelAnalisis = new JSONObject();
    private final cargarTexto idioma = new cargarTexto("Compiler");

    public JSONObject getResultadoDelAnalisis() {
        return resultadoDelAnalisis;
    }

    public void ejecutarCompilador(String codigoFuente){
        this.ejecutar(codigoFuente);
    }

    private void ejecutar(String codigoFuente){

        Compilador.EN.Compiler.Sintax compilador = new Sintax(new Compilador.EN.Compiler.LexerCup(new StringReader(codigoFuente)));

        try {
            compilador.parse();

            resultadoDelAnalisis.put("status",true);
            resultadoDelAnalisis.put("message",idioma.traerTexto("SinProbemas"));
            resultadoDelAnalisis.put("result", compilador.getResultadoJSON());

        } catch (Exception e) {

            String mensaje = "";
            try{
                Symbol simbolo = compilador.getError();
                resultadoDelAnalisis.put("status",false);
                mensaje += idioma.traerTexto("ErrorSintaxis");
                mensaje += simbolo.right + 1 ;
                mensaje += idioma.traerTexto("Columna");
                mensaje += (simbolo.left + 1);
                mensaje += idioma.traerTexto("AntesDe");
                mensaje += simbolo.value;
                resultadoDelAnalisis.put("message",mensaje);

            }catch(Exception error){
                resultadoDelAnalisis.put("status",false);
                mensaje = idioma.traerTexto("ErrorDesconocido");
                resultadoDelAnalisis.put("message",mensaje);
            }
        }
    }

}
