package Compilador;

import Compilador.Default.AnalizadorLexico.AnalizarLexicoDefault;
import Compilador.Default.Compiler.CompilerDefault;
import Compilador.EN.AnalizadorLexico.AnalizarLexicoEN;
import Compilador.EN.Compiler.CompilerEN;
import Compilador.ES.AnalizadorLexico.AnalizarLexicoES;
import Compilador.ES.Compiler.CompilerES;
import org.json.JSONObject;

public class EjecutarAnalizadores {

    public JSONObject AnalizarSoloLexico(String codigoFuente, String lenguaje){

        switch(lenguaje){
            case "ES":
                return this.ejecutarAnalizadorLexicoES(codigoFuente);
            case "EN":
                return this.ejecutarAnalizadorLexicoEN(codigoFuente);
            default:
                return this.ejecutarAnalizadorLexicoDefault(codigoFuente);
        }

    }

    public JSONObject EjecutarCompilador(String codigoFuente, String lenguaje){

        switch(lenguaje){
            case "ES":
                if(this.ejecutarAnalizadorLexicoES(codigoFuente).getBoolean("status")){
                    return this.ejecutarCompiladorES(codigoFuente);
                }else{
                    return this.ejecutarAnalizadorLexicoES(codigoFuente);
                }
            case "EN":
                if(this.ejecutarAnalizadorLexicoEN(codigoFuente).getBoolean("status")){
                    return this.ejecutarCompiladorEN(codigoFuente);
                }else{
                    return this.ejecutarAnalizadorLexicoEN(codigoFuente);
                }
            default:
                //Comprobando que el análisis Léxico esta correctamente escrito
                if(this.ejecutarAnalizadorLexicoDefault(codigoFuente).getBoolean("status")){
                    return this.ejecutarCompiladorDefault(codigoFuente);
                }else{
                    return this.ejecutarAnalizadorLexicoDefault(codigoFuente);
                }
        }

    }

    private JSONObject ejecutarAnalizadorLexicoDefault(String codigoFuente){
        //Analisis únicamente del Analizador Léxico por Default
        AnalizarLexicoDefault LexicoLenguajeNatural = new AnalizarLexicoDefault();
        LexicoLenguajeNatural.EjecutarAnalisisLexico(codigoFuente);
        return (LexicoLenguajeNatural.getResultadoDelAnalisis());
    }

    private JSONObject ejecutarCompiladorDefault(String codigoFuente){
        CompilerDefault compilador = new CompilerDefault();
        compilador.ejecutarCompilador(codigoFuente);
        return compilador.getResultadoDelAnalisis();
    }

    private JSONObject ejecutarAnalizadorLexicoEN(String codigoFuente){
        //Analisis únicamente del Analizador Léxico por EN
        AnalizarLexicoEN LexicoLenguajeNatural = new AnalizarLexicoEN();
        LexicoLenguajeNatural.EjecutarAnalisisLexico(codigoFuente);
        return (LexicoLenguajeNatural.getResultadoDelAnalisis());
    }

    private JSONObject ejecutarCompiladorEN(String codigoFuente){
        CompilerEN compilador = new CompilerEN();
        compilador.ejecutarCompilador(codigoFuente);
        return compilador.getResultadoDelAnalisis();
    }

    private JSONObject ejecutarAnalizadorLexicoES(String codigoFuente){
        //Analisis únicamente del Analizador Léxico por ES
        AnalizarLexicoES LexicoLenguajeNatural = new AnalizarLexicoES();
        LexicoLenguajeNatural.EjecutarAnalisisLexico(codigoFuente);
        return (LexicoLenguajeNatural.getResultadoDelAnalisis());
    }

    private JSONObject ejecutarCompiladorES(String codigoFuente){
        CompilerES compilador = new CompilerES();
        compilador.ejecutarCompilador(codigoFuente);
        return compilador.getResultadoDelAnalisis();
    }

}
