package InterfazDeUsuario;

import Compilador.CompiladorLenguajeUsuario;
import Compilador.EjecutarAnalizadores;
import Componentes.CargarMapa.CargaDeMapaCSV;
import Componentes.MaquinaVirtual.EjecutarPrograma;
import ConfiguraLenguaje.cargarTexto;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private JMenuItem guardarCodigo;
    private JMenuItem preferencias;
    private JMenuItem salir;

    //Submenus de MenuBar de codigo
    private JMenuItem compilar;
    private JMenuItem ejecutar;
    private JMenuItem compilarYEjecutar;

    //Sumbenus de editar

    private JMenuItem detenerPrograma;
    private JMenuItem limpiarMapa;
    private JMenuItem recargarEditor;
    private JMenuItem recargarMapa;

    //Submenus de MenuBar de Examples
    private JMenuItem cargarEjemplo1;
    private JMenuItem cargarEjemplo2;
    private JMenuItem cargarEjemplo3;
    private JMenuItem cargarEjemplo4;


    //Submenus de MenuBar de compilador
    private JMenuItem recompilarLexico;
    private JMenuItem recompilarSintactico;

    private JMenuItem about;


    private final CompiladorLenguajeUsuario generadorDeCompilador = new CompiladorLenguajeUsuario();
    private final EjecutarPrograma ejecutarPrograma = new EjecutarPrograma();
    private final cargarTexto idioma = new cargarTexto("Fenix");

    CompiladorLenguajeUsuario CompiladorLexico;
    EjecutarAnalizadores Analizadores;

    String rutaMapaDelUsuario;
    String rutaCodigoDelUsuario;

    String rutaCodigoDestinoDelUsuario;

    public Fenix(){
        super("{Fenix}");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(
                System.getProperty("user.dir").replace("\\", "/")+"/FILES/Icons/Fenix.png")
        );

        CompiladorLexico = new CompiladorLenguajeUsuario();
        Analizadores = new EjecutarAnalizadores();

        crearMenuBar();
        actualizarIdioma();

        this.abrirMapa("Start");

        ejecutarPrograma.setLblResultados(this.lblResultados);
        ejecutarPrograma.setTxtResultados(this.txtResultados);

        //Elementos del menú File:
        //File: El usuario presiona abrir mapa
        abrirMapa.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            JFileChooser exploradorDeArchivos = new JFileChooser();
            int response = exploradorDeArchivos.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                abrirMapaSeleccionadoUsuario(new File(exploradorDeArchivos.getSelectedFile().getAbsolutePath()));
            }
        });

        //File: El usuario presiona abrir programa
        abrirCodigo.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            JFileChooser exploradorDeArchivos = new JFileChooser();
            int response = exploradorDeArchivos.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                abrirProgramaSeleccionadoUsuario(new File(exploradorDeArchivos.getSelectedFile().getAbsolutePath()));
            }
        });

        //File: El usuario guarda su código en el archivo
        guardarCodigo.addActionListener(e -> guardarArchivoCodigo());

        //File: El usuario abre la ventana de configuración
        preferencias.addActionListener(e -> abrirVentanaConfiguracion());

        //File: El usuario cierra el programa
        salir.addActionListener(e -> System.exit(0));

        //Elementos del menú Run:

        //Run: Este método se ejecuta cuando solo se requiere únicamente el Análisis Léxico del código fuente:
        compilar.addActionListener(e -> AnalizarLexico());

        //Run: Este código se ejecuta cuando se ejecuta el compilador
        ejecutar.addActionListener(e -> AnalizarLexicoYSintactico());

        //Run: Este fragmento es para ejecutar el programa
        compilarYEjecutar.addActionListener(e -> this.ejecutarPrograma());

        //Elementos del menú Compiler:

        //Compiler: Este método se ejecuta  cuando se requiere la generación del Analizador Léxico:
        recompilarLexico.addActionListener(e -> generadorDeCompilador.GenerarAnalizadorLexico());

        //Compiler: Este método se ejecuta cuando se requiere la generación de los Analizadores Léxico y Sintáctico
        recompilarSintactico.addActionListener(e -> generadorDeCompilador.GenerarAnalizadoresLexicoYSintactico());

        //Elementos del menú Edit

        //Edit: Detener el programa que se está ejecutando.
        detenerPrograma.addActionListener(e -> ejecutarPrograma.interrumpirProceso());

        //Edit: Limpiar el mapa sin abrir archivos.
        limpiarMapa.addActionListener(e -> ejecutarPrograma.limpiarMapa());

        //Edit: Recargar el programa según el archivo abierto a cómo se cargó originalmente.
        recargarEditor.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            txtResultados.setText("");
            if(rutaCodigoDelUsuario!=null){
                abrirProgramaSeleccionadoUsuario(null);
            }
        });

        //Edit: Recargar el mapa según el archivo abierto a cómo se cargó originalmente.
        recargarMapa.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            txtResultados.setText("");
            abrirMapaSeleccionadoUsuario(null);
        });

        //Elementos del menú Examples:

        //Examples: Este fragmento de código se ejecuta para cargar los mapas y programas de prueba.
        cargarEjemplo1.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            txtResultados.setText("");
            this.abrirMapa("Example1");
            this.abrirPrograma("Example1");
        });
        cargarEjemplo2.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            txtResultados.setText("");
            this.abrirMapa("Example2");
            this.abrirPrograma("Example2");
        });
        cargarEjemplo3.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            txtResultados.setText("");
            this.abrirMapa("Example3");
            this.abrirPrograma("Example3");
        });
        cargarEjemplo4.addActionListener(e -> {
            if(!ejecutarPrograma.getCanStart()) {
                accionInvalida();
                return;
            }
            txtResultados.setText("");
            this.abrirMapa("Example4");
            this.abrirPrograma("Example4");
        });

        //About: Abre la ventana "Acerca de"
        about.addActionListener(e -> abrirAbout());

    }

    private void accionInvalida() {
        JOptionPane.showMessageDialog(
                new JFrame(),
                this.idioma.traerTexto("AccionNoValida"),
                this.idioma.traerTexto("Advertencia"),
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void abrirAbout() {
        JFrame frame = new About(this);
        frame.setSize(399,416);
        frame.setResizable(false);
        frame.setVisible(true);
        this.setEnabled(false);
    }

    private void abrirVentanaConfiguracion() {
        JFrame frame = new Settings(this);
        frame.setSize(657,364);
        frame.setResizable(false);
        frame.setVisible(true);
        this.setEnabled(false);
    }

    private void guardarArchivoCodigo() {
        if(!ejecutarPrograma.getCanStart()) {
            accionInvalida();
            return;
        }
        File archivo;
        if(rutaCodigoDelUsuario==null){
            JFileChooser exploradorDeArchivos = new JFileChooser();
            int response = exploradorDeArchivos.showSaveDialog(null);
            if(response==JFileChooser.APPROVE_OPTION){
                archivo = exploradorDeArchivos.getSelectedFile();
                System.out.println(archivo);
                rutaCodigoDelUsuario = String.valueOf(archivo);
            }
        }
        if(rutaCodigoDelUsuario!=null){
            try {
                FileWriter archivoEscrito = new FileWriter(rutaCodigoDelUsuario.replace("/","\\"));
                archivoEscrito.write(this.txtCodigoFuente.getText());
                archivoEscrito.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void abrirProgramaSeleccionadoUsuario(File ruta) {
        String string;
        if(ruta != null){
            rutaCodigoDelUsuario = ruta.toString().replace("\\", "/");
        }
        String filePath = rutaCodigoDelUsuario;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            string = String.join("\n", lines);
            txtCodigoFuente.setText(string);
            agregarTexto(idioma.traerTexto("ProgramaCargado"));
        } catch (IOException ignored) {}
    }

    private void abrirMapaSeleccionadoUsuario(File ruta) {
        String string = "";
        if(ruta!=null){
            rutaMapaDelUsuario = ruta.toString().replace("\\", "/");
        }
        String filePath = rutaMapaDelUsuario;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            string = String.join("\n", lines);
        } catch (IOException ignored) {}

        CargaDeMapaCSV map = new CargaDeMapaCSV();
        map.generarMapa(string);
        if(map.isStatus()){
            lblResultados.setText(map.getMapaHTML());
            agregarTexto(map.getMessage());
        }else{
            agregarTextoError(map.getMessage());
        }
        ejecutarPrograma.setColumnas(map.getColumnas());
        ejecutarPrograma.setFilas(map.getFilas());
        ejecutarPrograma.setMapaBackend(map.getMapaBackend());
    }

    private void abrirPrograma(String IDEjemplo) {
        String string;
        String filePath = System.getProperty("user.dir").replace("\\", "/")+"/FILES/Examples/"+IDEjemplo+"/ExampleProgram.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            string = String.join("\n", lines);
            txtCodigoFuente.setText(string);
            agregarTexto(idioma.traerTexto("ProgramaCargado"));
        } catch (IOException ignored) {}
    }

    private void ejecutarPrograma() {
        //Ejecuta el compilador
        AnalizarLexicoYSintactico();
        if(ejecutarPrograma.isReady()){
            txtResultados.setText("");
            ejecutarPrograma.ejecutaPrograma();
        }else{
            agregarTextoError(idioma.traerTexto("NotReady"));
        }
    }

    private void abrirMapa(String IDEjemplo) {
        String string ="";
        String filePath = System.getProperty("user.dir").replace("\\", "/")+"/FILES/Examples/"+IDEjemplo+"/TestMap.csv";
        rutaMapaDelUsuario = filePath;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            string = String.join("\n", lines);

        } catch (IOException ignored) {}

        CargaDeMapaCSV map = new CargaDeMapaCSV();
        map.generarMapa(string);
        if(map.isStatus()){
            lblResultados.setText(map.getMapaHTML());
            agregarTexto(map.getMessage());
        }else{
            agregarTextoError(map.getMessage());
        }

        ejecutarPrograma.setColumnas(map.getColumnas());
        ejecutarPrograma.setFilas(map.getFilas());
        ejecutarPrograma.setMapaBackend(map.getMapaBackend());
    }

    private void AnalizarLexicoYSintactico(){
        if(!ejecutarPrograma.getCanStart()) {
            accionInvalida();
            return;
        }
        JSONObject resultado;
        resultado = Analizadores.EjecutarCompilador(leerCodigoFuenteDelUsuario(),idioma.getConfigString("IdiomaDelCompilador"));
        //StringBuilder tmp = new StringBuilder();
        //Analizando los resultados:
        if(resultado.getBoolean("status")){
            ejecutarPrograma.setJSONPrograma(resultado.getJSONObject("result"));
            imprimirCodigoDestino(resultado.getJSONObject("result"));
            agregarTexto(resultado.getString("message"));
        }else{
            //Errores en el resultado
            ejecutarPrograma.setJSONPrograma(resultado);
            agregarTextoError(resultado.getString("message"));
        }

    }

    private void imprimirCodigoDestino(JSONObject result) {
        String DireccionOutput = System.getProperty("user.dir").replace("\\", "/")+"/FILES/Output/output.json";
        try {
            FileWriter archivoEscrito = new FileWriter(DireccionOutput.replace("/","\\"));
            archivoEscrito.write(String.valueOf(result));
            archivoEscrito.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void AnalizarLexico(){
        if(!ejecutarPrograma.getCanStart()) {
            accionInvalida();
            return;
        }
        //En este objeto se guardará el resultado del Analisis Léxico:
        JSONObject resultado;
        //Ejecutando el Analisis Léxico:
        resultado = Analizadores.AnalizarSoloLexico(leerCodigoFuenteDelUsuario(),"");

        //Declaración de una cadena temporal.

        //Analizando los resultados:
        if(resultado.getBoolean("status")){
            agregarTexto(resultado.getString("message"));
        }else{
            //Errores en el resultado
            agregarTextoError(resultado.getString("message"));
        }
    }

    private void actualizarIdioma(){
        setTitle(idioma.getConfigString("TituloDelPrograma"));
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
        compilador = new JMenu(idioma.traerTexto("JMenuCompilador"));
        ejemplos = new JMenu(idioma.traerTexto("JMenuEjemplos"));
        acercaDe = new JMenu(idioma.traerTexto("JMenuAcercade"));

        abrirMapa = new JMenuItem(idioma.traerTexto("JMenuItemAbrirMapa"));
        abrirCodigo = new JMenuItem(idioma.traerTexto("JMenuItemAbrirCodigo"));
        guardarCodigo = new JMenuItem(idioma.traerTexto("JMenuItemGuardarCodigo"));
        preferencias = new JMenuItem(idioma.traerTexto("JMenuItemPreferencias"));
        salir = new JMenuItem(idioma.traerTexto("JMenuItemSalir"));

        compilar = new JMenuItem(idioma.traerTexto("JMenuItemCompilar"));
        ejecutar = new JMenuItem(idioma.traerTexto("JMenuItemEjecutar"));
        compilarYEjecutar = new JMenuItem(idioma.traerTexto("JMenuItemCompilarYEjecutar"));

        recompilarLexico = new JMenuItem(idioma.traerTexto("JMenuItemRecompilarLexico"));
        recompilarSintactico = new JMenuItem(idioma.traerTexto("JMenuItemRecompilarSintactico"));

        detenerPrograma = new JMenuItem(idioma.traerTexto("JMenuItemdetenerPrograma"));
        limpiarMapa = new JMenuItem(idioma.traerTexto("JMenuItemlimpiarMapa"));
        recargarEditor = new JMenuItem(idioma.traerTexto("JMenuItemrecargarEditor"));
        recargarMapa = new JMenuItem(idioma.traerTexto("JMenuItemrecargarMapa"));

        cargarEjemplo1 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo1"));
        cargarEjemplo2 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo2"));
        cargarEjemplo3 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo3"));
        cargarEjemplo4 = new JMenuItem(idioma.traerTexto("JMenuItemCargarEjemplo4"));

        about = new JMenuItem(idioma.traerTexto("JMenuItemAbout"));

        //Construyendo el MenuBar
        menuBar.add(archivo);
        menuBar.add(codigo);
        menuBar.add(editar);
        menuBar.add(ejemplos);
        menuBar.add(acercaDe);

        archivo.add(abrirMapa);
        archivo.add(new JSeparator());
        archivo.add(abrirCodigo);
        archivo.add(guardarCodigo);
        archivo.add(new JSeparator());
        archivo.add(preferencias);
        archivo.add(salir);

        codigo.add(compilar);
        codigo.add(ejecutar);
        codigo.add(new JSeparator());
        codigo.add(compilarYEjecutar);

        editar.add(detenerPrograma);
        editar.add(new JSeparator());
        editar.add(limpiarMapa);
        editar.add(new JSeparator());
        editar.add(recargarEditor);
        editar.add(recargarMapa);

        ejemplos.add(cargarEjemplo1);
        ejemplos.add(cargarEjemplo2);
        ejemplos.add(cargarEjemplo3);
        ejemplos.add(cargarEjemplo4);

        acercaDe.add(about);

        if(!idioma.getConfigBoolean("MostrarOpcionGenerarCompiladores")){
            menuBar.add(compilador);
            compilador.add(recompilarLexico);
            compilador.add(recompilarSintactico);
        }

        //Mostrando el MenuBar
        this.setJMenuBar(menuBar);

        //Creando shorcuts
        abrirMapa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        abrirCodigo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        guardarCodigo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        preferencias.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        salir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));

        compilar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        ejecutar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, java.awt.event.InputEvent.CTRL_MASK));
        compilarYEjecutar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));

        detenerPrograma.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        limpiarMapa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        recargarEditor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        recargarMapa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));

        cargarEjemplo1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        cargarEjemplo2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        cargarEjemplo3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        cargarEjemplo4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));

        about.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));

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
}
