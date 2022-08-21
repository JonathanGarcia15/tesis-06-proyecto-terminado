package InterfazDeUsuario;

import Compilador.CompiladorLenguajeUsuario;
import Compilador.EjecutarAnalizadores;
import Componentes.CargarMapa.CargaDeMapaCSV;
import Componentes.MaquinaVirtual.EjecutarPrograma;
import ConfiguraLenguaje.cargarTexto;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Fenix extends JFrame{
    private JPanel jpnInterfazDeUsuario;
    private JLabel lblEditorCode;
    private JLabel lblLog;
    private JLabel lblResultados;
    private JTextArea txtCodigoFuente;
    private JTextArea txtResultados;
    private JScrollPane jscpResultados;
    private JScrollPane jscpCodigoFuente;
    private JScrollPane scrResultados;

    //Declaración de MenuBar
    private JMenuBar menuBar;

    //Elementos principales de MenuBar
    private JMenu archivo;
    private JMenu codigo;
    private JMenu editar;
    private JMenu ejemplos;
    private JMenu compilador;
    private JMenu acercaDe;

    //Submenus de MenuBar de Archivo
    private JMenuItem abrirMapa;
    private JMenuItem abrirCodigo;

    //Submenus de MenuBar de codigo
    private JMenuItem compilar;
    private JMenuItem ejecutar;
    private JMenuItem compilarYEjecutar;

    //Submenus de MenuBar de editar
    private JMenuItem cargarEjemplo1;
    private JMenuItem cargarEjemplo2;
    private JMenuItem cargarEjemplo3;
    private JMenuItem cargarEjemplo4;
    private JMenuItem cargarProgramaDePrueba1;

    //Submenus de MenuBar de compilador
    private JMenuItem recompilarLexico;
    private JMenuItem recompilarSintactico;


    private final CompiladorLenguajeUsuario generadorDeCompilador = new CompiladorLenguajeUsuario();
    private final EjecutarPrograma ejecutarPrograma = new EjecutarPrograma();
    private final cargarTexto idioma = new cargarTexto("Fenix");

    CompiladorLenguajeUsuario CompiladorLexico;
    EjecutarAnalizadores Analizadores;


    public Fenix(){
        super("{Fenix}");

        CompiladorLexico = new CompiladorLenguajeUsuario();
        Analizadores = new EjecutarAnalizadores();

        crearMenuBar();
        actualizarIdioma();

        if(idioma.ocultarGenerarCompiladores()){
            compilador.hide();
        }

        ejecutarPrograma.setLblResultados(this.lblResultados);
        ejecutarPrograma.setTxtResultados(this.txtResultados);

        //Este método se ejecuta  cuando se requiere la generación de el Analizador Léxico:
        recompilarLexico.addActionListener(e -> {
            //Generador del Analizador Lexico
            generadorDeCompilador.GenerarAnalizadorLexico();
        });

        //Este método se ejecuta cuando se requiere la generación de los Analizadores Léxico y Sintáctico
        recompilarSintactico.addActionListener(e -> {
            //Generando Solamente Léxico y Sintáctico
            generadorDeCompilador.GenerarAnalizadoresLexicoYSintactico();
        });

        //Este código se ejecuta cuando se ejecuta el compilador
        ejecutar.addActionListener(e -> {
            AnalizarLexicoYSintactico();
        });

        //Este método se ejecuta cuando solo se requiere únicamente el Análisis Léxico del código fuente:
        compilar.addActionListener(e -> {
            AnalizarLexico();
        });

        //Este fragmento de código se ejecuta para cargar los mapas y programas de prueba.
        cargarEjemplo1.addActionListener(e -> {
            this.abrirMapa("Example1");
            this.abrirPrograma("Example1");
        });
        cargarEjemplo2.addActionListener(e -> {
            this.abrirMapa("Example2");
            this.abrirPrograma("Example2");
        });
        cargarEjemplo3.addActionListener(e -> {
            this.abrirMapa("Example3");
            this.abrirPrograma("Example3");
        });
        cargarEjemplo4.addActionListener(e -> {
            this.abrirMapa("Example4");
            this.abrirPrograma("Example4");
        });

        //Este fragmento es para ejecutar el programa
        compilarYEjecutar.addActionListener(e ->{
            this.ejecutarPrograma();
        });


    }

    private void abrirPrograma(String IDEjemplo) {
        String string ="";
        String filePath = System.getProperty("user.dir").replace("\\", "/")+"/FILES/Examples/"+IDEjemplo+"/ExampleProgram.txt";
        Charset encoding = Charset.defaultCharset();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), encoding);
            string = lines.stream().collect(Collectors.joining("\n"));
            txtCodigoFuente.setText(string);
        } catch (IOException ex) {
            //Logger.getLogger(Mapas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ejecutarPrograma() {
        //Ejecuta el comilador
        AnalizarLexicoYSintactico();
        if(ejecutarPrograma.isReady()){
            ejecutarPrograma.ejecutaPrograma();
        }
    }

    private void abrirMapa(String IDEjemplo) {
        String string ="";
        String filePath = System.getProperty("user.dir").replace("\\", "/")+"/FILES/Examples/"+IDEjemplo+"/TestMap.csv";
        Charset encoding = Charset.defaultCharset();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), encoding);
            string = String.join("\n", lines);

        } catch (IOException ex) {
            //Logger.getLogger(Mapas.class.getName()).log(Level.SEVERE, null, ex);
        }

        CargaDeMapaCSV map = new CargaDeMapaCSV();
        map.generarMapa(string);
        if(map.isStatus()){
            lblResultados.setText(map.getMapaHTML());
            txtResultados.setText(map.getMessage());
            txtResultados.setForeground(new Color(25, 111, 61));
        }else{
            txtResultados.setText(map.getMessage());
            txtResultados.setForeground(Color.red);
        }

        ejecutarPrograma.setColumnas(map.getColumnas());
        ejecutarPrograma.setFilas(map.getFilas());
        ejecutarPrograma.setMapaBackend(map.getMapaBackend());
        //lblResultados.setText(map.get);
    }

    private void AnalizarLexicoYSintactico(){
        JSONObject resultado;
        resultado = Analizadores.EjecutarCompilador(leerCodigoFuenteDelUsuario(),idioma.idiomaDelCompilador());
        //StringBuilder tmp = new StringBuilder();
        //Analizando los resultados:
        if(resultado.getBoolean("status")){
            //tmp.append("\n").append("JSON recibido: ").append(resultado);
            //tmp.append("\n").append("JSON del Programa: \n").append(resultado.getJSONObject("result"));
            ejecutarPrograma.setJSONPrograma(resultado.getJSONObject("result"));
            txtResultados.setText(resultado.getString("message") + "\n");
            txtResultados.setForeground(new Color(25, 111, 61));
        }else{
            //Errores en el resultado
            //tmp.append("\n").append("JSON recibido: ").append(resultado);
            ejecutarPrograma.setJSONPrograma(resultado);
            txtResultados.setText(resultado.getString("message"));
            txtResultados.setForeground(Color.red);
        }

    }

    private void AnalizarLexico(){
        //En este objeto se guardará el resultado del Analisis Léxico:
        JSONObject resultado;
        //Ejecutando el Analisis Léxico:
        resultado = Analizadores.AnalizarSoloLexico(leerCodigoFuenteDelUsuario(),"");

        //Declaración de una cadena temporal.
        //StringBuilder tmp = new StringBuilder();

        //Analizando los resultados:
        if(resultado.getBoolean("status")){
                /*
                //Enlistando los tokens recibidos
                JSONObject objetoTemporal;
                for(int i=0;i<resultado.getJSONArray("tokens").length();i++){
                    objetoTemporal = (JSONObject) resultado.getJSONArray("tokens").get(i);
                    if(objetoTemporal.get("token") == "SALTOLINEA"){
                        tmp.append("\n");
                    }else{
                        tmp.append("\"").append(objetoTemporal.get("token")).append("\"");
                        tmp.append("(").append(objetoTemporal.get("lexema")).append(") ");
                    }
                }//*/
            //tmp.append("\n").append("JSON recibido: ").append(resultado);
            txtResultados.setText(resultado.getString("message") + "\n");
            txtResultados.setForeground(new Color(25, 111, 61));
        }else{
            //Errores en el resultado
            //tmp.append("\n").append("JSON recibido: ").append(resultado);
            txtResultados.setText(resultado.getString("message"));
            txtResultados.setForeground(Color.red);
        }
    }

    private void actualizarIdioma(){

        setTitle(idioma.traerTexto("TituloDelPrograma"));
        lblEditorCode.setText(idioma.traerTexto("JLabelLblEditorCode"));
        lblLog.setText(idioma.traerTexto("JLabelLblLog"));
        lblResultados.setText(idioma.traerTexto("JLabelLblResultados"));
        txtCodigoFuente.setText(idioma.traerTexto("JTextAreaTxtCodigoFuente"));
        lblResultados.setFont(new Font("Segoe UI Emoji",Font.PLAIN, idioma.FuenteMapa()));
    }
    private String leerCodigoFuenteDelUsuario(){
        return txtCodigoFuente.getText();
    }

    private void crearMenuBar(){
        //Inicializando los objetos creados
        menuBar = new JMenuBar();
        setContentPane(jpnInterfazDeUsuario);

        archivo = new JMenu(idioma.traerTexto("JMenuArchivo"));
        codigo = new JMenu(idioma.traerTexto("JMenuCodigo"));
        editar = new JMenu(idioma.traerTexto("JMenuEditar"));
        ejemplos = new JMenu(idioma.traerTexto("JMenuEjemplos"));
        compilador = new JMenu(idioma.traerTexto("JMenuCompilador"));
        acercaDe = new JMenu(idioma.traerTexto("JMenuAcercade"));

        abrirMapa = new JMenuItem(idioma.traerTexto("JMenuItemAbrirMapa"));
        abrirCodigo = new JMenuItem(idioma.traerTexto("JMenuItemAbrirCodigo"));

        compilar = new JMenuItem(idioma.traerTexto("JMenuItemCompilar"));
        ejecutar = new JMenuItem(idioma.traerTexto("JMenuItemEjecutar"));
        compilarYEjecutar = new JMenuItem(idioma.traerTexto("JMenuItemCompilarYEjecutar"));

        cargarEjemplo1 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo1"));
        cargarEjemplo2 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo2"));
        cargarEjemplo3 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo3"));
        cargarEjemplo4 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo4"));

        cargarProgramaDePrueba1 = new JMenuItem(idioma.traerTexto("JMenuItemCargarProgramaDePrueba1"));

        recompilarLexico = new JMenuItem(idioma.traerTexto("JMenuItemRecompilarLexico"));
        recompilarSintactico = new JMenuItem(idioma.traerTexto("JMenuItemRecompilarSintactico"));

        //Construyendo el MenuBar
        menuBar.add(archivo);
        menuBar.add(codigo);
        menuBar.add(editar);
        menuBar.add(ejemplos);
        menuBar.add(compilador);
        menuBar.add(acercaDe);

        archivo.add(abrirMapa);
        archivo.add(abrirCodigo);

        codigo.add(compilar);
        codigo.add(ejecutar);
        codigo.add(compilarYEjecutar);

        editar.add(cargarProgramaDePrueba1);

        ejemplos.add(cargarEjemplo1);
        ejemplos.add(cargarEjemplo2);
        //ejemplos.add(cargarEjemplo3);
        //ejemplos.add(cargarEjemplo4);

        compilador.add(recompilarLexico);
        compilador.add(recompilarSintactico);

        //Mostrando el MenuBar
        this.setJMenuBar(menuBar);
    }

}
