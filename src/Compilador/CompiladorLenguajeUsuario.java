package Compilador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompiladorLenguajeUsuario {

    //Obtiene la ruta del proyecto:
    private final String DireccionProyecto = System.getProperty("user.dir").replace("\\", "/");

    public void GenerarAnalizadorLexico(){
        this.GenerarLexicoDefault();
        this.GenerarLexicoEN();
        this.GenerarLexicoES();
    }

    private void GenerarLexicoDefault() {
        this.GenerarLexico(
                    "/src/Compilador/Default/CodigoFuente/Lexer.flex",
                "/src/Compilador/Default/CodigoFuente/Lexer.java",
                "/src/Compilador/Default/AnalizadorLexico/Lexer.java"
                );
    }

    private void GenerarLexicoES() {
        this.GenerarLexico(
                "/src/Compilador/ES/CodigoFuente/Lexer.flex",
                "/src/Compilador/ES/CodigoFuente/Lexer.java",
                "/src/Compilador/ES/AnalizadorLexico/Lexer.java"
        );
    }

    private void GenerarLexicoEN() {
        this.GenerarLexico(
                "/src/Compilador/EN/CodigoFuente/Lexer.flex",
                "/src/Compilador/EN/CodigoFuente/Lexer.java",
                "/src/Compilador/EN/AnalizadorLexico/Lexer.java"
        );
    }

    private void GenerarLexico(String rutaArchivoFlex, String rutaArchivoLexerOriginal, String rutaArchivoLexerDestino) {

        //Ruta de los archivos
        String rutaLexico = DireccionProyecto + rutaArchivoFlex;

        File archivo;

        //Para leer Lexer.flex y generar el Analizador Léxico
        archivo = new File(rutaLexico);
        jflex.Main.generate(archivo);

        //Moviendo el archivo Lexer.java
        this.moverArchivos(rutaArchivoLexerOriginal,rutaArchivoLexerDestino);

    }

    public void GenerarAnalizadoresLexicoYSintactico(){
        this.GenerarLexicoYSintacticoDefault();
        this.GenerarLexicoYSintacticoES();
        this.GenerarLexicoYSintacticoEN();
    }

    private void GenerarLexicoYSintacticoDefault(){
        this.GenerarLexicoYSintactico(
                "/src/Compilador/Default/CodigoFuente/LexerCup.flex",
                "/src/Compilador/Default/CodigoFuente/LexerCup.java",
                "/src/Compilador/Default/Compiler/LexerCup.java",
                "/src/Compilador/Default/CodigoFuente/Sintax.cup",
                "/src/Compilador/Default/Compiler/sym.java",
                "/src/Compilador/Default/Compiler/Sintax.java"
        );
    }

    private void GenerarLexicoYSintacticoES(){
        this.GenerarLexicoYSintactico(
                "/src/Compilador/ES/CodigoFuente/LexerCup.flex",
                "/src/Compilador/ES/CodigoFuente/LexerCup.java",
                "/src/Compilador/ES/Compiler/LexerCup.java",
                "/src/Compilador/ES/CodigoFuente/Sintax.cup",
                "/src/Compilador/ES/Compiler/sym.java",
                "/src/Compilador/ES/Compiler/Sintax.java"
        );
    }

    private void GenerarLexicoYSintacticoEN(){
        this.GenerarLexicoYSintactico(
                "/src/Compilador/EN/CodigoFuente/LexerCup.flex",
                "/src/Compilador/EN/CodigoFuente/LexerCup.java",
                "/src/Compilador/EN/Compiler/LexerCup.java",
                "/src/Compilador/EN/CodigoFuente/Sintax.cup",
                "/src/Compilador/EN/Compiler/sym.java",
                "/src/Compilador/EN/Compiler/Sintax.java"
        );
    }

    private void GenerarLexicoYSintactico(String rutaArchivoFlex, String rutaArchivoLexerOriginal, String rutaArchivoLexerDestino,String rutaArchivoCUP, String rutaArchivoSYMDestino, String rutaArchivoSintaxDestino){
        //Generando Analizador Léxico
        //Ruta del archivo flex
        String rutaLexico = DireccionProyecto + rutaArchivoFlex;

        File archivo;

        //Para leer Lexer.flex y generar el Analizador Léxico
        archivo = new File(rutaLexico);
        jflex.Main.generate(archivo);

        //Generando Analizador Sintáctico
        //Ruta del archivo Cup
        String[] rutaS = {"-parser", "Sintax", DireccionProyecto + rutaArchivoCUP};

        try {
            java_cup.Main.main(rutaS);
        } catch (Exception e) {
            //eliminarArchivoEnCasoDeError("/src/Compilador/Default/CodigoFuente/LexerCup.java");
            throw new RuntimeException(e);
        }

        //Moviendo los archivos generados al package Compilador/Default/Compiler

        moverArchivos("/sym.java",rutaArchivoSYMDestino);
        moverArchivos("/Sintax.java",rutaArchivoSintaxDestino);
        moverArchivos(rutaArchivoLexerOriginal,rutaArchivoLexerDestino);

    }

    private void moverArchivos(String rutaDelArchivoExistente, String rutaNuevaDelArchivo){
        Path path = Paths.get(DireccionProyecto + rutaNuevaDelArchivo);
        //Eliminando si existe un archivo previo
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException ex) {
                //Logger.getLogger(Tangananica.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            Files.move(
                    Paths.get(DireccionProyecto+rutaDelArchivoExistente),
                    path
            );
        } catch (IOException ex) {
            //Logger.getLogger(Tangananica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
