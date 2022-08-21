package Componentes.CargarMapa;

import ConfiguraLenguaje.cargarTexto;

import javax.swing.*;

public class CargaDeMapaCSV {

    private int[][] MapaBackend;
    private int Filas;
    private int Columnas;
    private String MapaHTML;
    private boolean status;
    private String message;

    public int getFilas() {
        return Filas;
    }

    public int getColumnas() {
        return Columnas;
    }

    public int[][] getMapaBackend() {
        return MapaBackend;
    }

    public String getMapaHTML() {
        return MapaHTML;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    private final cargarTexto idioma = new cargarTexto("CargaDeMapaCSV");

    public void generarMapa(String mapa){
        this.generarMapaBackEnd(mapa);
    }

    private void generarMapaBackEnd(String CSVMapa){
        int filas=1, columnas=1;
        for(int i=0; i<CSVMapa.length() ; i++){
            if(CSVMapa.charAt(i) == ','){
                columnas += 1;
            }else if(CSVMapa.charAt(i) == '\n'){
                filas+=1;
                columnas = 1;
            }
        }

        MapaBackend = new int[filas][columnas];
        Filas = filas;
        Columnas = columnas;
        MapaHTML = "";
        boolean personajeLeido = false;

        int jFilas=0;
        int kColumnas=0;
        for(int i=0; i<CSVMapa.length() ; i++){
            switch(CSVMapa.charAt(i)){
                case 'N': case 'n':
                    if(!personajeLeido){
                        MapaBackend[jFilas][kColumnas] = 1;
                    }else{
                        this.status=false;
                        this.message=idioma.traerTexto("ERRORPERSONAJE");
                        return;
                    }
                    break;
                case 'S': case 's':
                    if(!personajeLeido){
                        MapaBackend[jFilas][kColumnas] = 2;
                    }else{
                        this.status=false;
                        this.message=idioma.traerTexto("ERRORPERSONAJE");
                        return;
                    }
                    break;
                case 'E': case 'e':
                    if(!personajeLeido){
                        MapaBackend[jFilas][kColumnas] = 3;
                    }else{
                        this.status=false;
                        this.message=idioma.traerTexto("ERRORPERSONAJE");
                        return;
                    }
                    break;
                case 'W': case 'w':
                    if(!personajeLeido){
                        MapaBackend[jFilas][kColumnas] = 4;
                    }else{
                        this.status=false;
                        this.message=idioma.traerTexto("ERRORPERSONAJE");
                        return;
                    }
                    break;
                case 'X': case 'x':
                    MapaBackend[jFilas][kColumnas] = 5;
                    break;
                case 'K': case 'k':
                    MapaBackend[jFilas][kColumnas] = 6;
                    break;
                case 'O': case 'o':
                    MapaBackend[jFilas][kColumnas] = 7;
                    break;
                case 'P': case 'p':
                    MapaBackend[jFilas][kColumnas] = 8;
                    break;
                case ',':
                    kColumnas++;
                    break;
                case '\n':
                    jFilas++;
                    kColumnas = 0;
                    break;
                case ';':
                    //MapaBackend[jFilas][kColumnas] = 8;
                    generarMapaHTML();
                    status=true;
                    message=idioma.traerTexto("MAPACREADO");
                    return;
                default:
                    MapaBackend[jFilas][kColumnas] = 0;
                    //status=false;
                    //message=idioma.traerTexto("ERRORSIMBOLOS");
                    //return;
            }
        }
        generarMapaHTML();
        status=true;
        message=idioma.traerTexto("MAPACREADO");
    }

    private void imprimirMapa(){
        for(int i = 0; i<Filas ; i++ ){
            for(int j = 0; j<Columnas ; j++ ){
                System.out.print(this.MapaBackend[i][j]);
            }
            System.out.println();
        }
    }

    private void generarMapaHTML(){
        for(int i = 0; i<Filas ; i++ ){
            MapaHTML += "<tr>";
            for(int j = 0; j<Columnas ; j++ ){
                switch(this.MapaBackend[i][j]){
                    case 0:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">⬜</td>";
                        break;
                    case 1:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">⬆</td>";
                        break;
                    case 2:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">⬇</td>";
                        break;
                    case 3:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">➡</td>";
                        break;
                    case 4:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">⬅</td>";
                        break;
                    case 5:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">🏁</td>";
                        break;
                    case 6:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">💣</td>";
                        break;
                    case 7:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">📦</td>";
                        break;
                    case 8:
                        MapaHTML += "<td style=\"background-color:"+idioma.traerColor("DEFAULT")+";\">❌</td>";
                        break;
                    default:

                }
            }
            MapaHTML += "</tr>";
        }
        MapaHTML = "<html><body><br><table>" + MapaHTML + "</table><br></body></html>";
    }

}
