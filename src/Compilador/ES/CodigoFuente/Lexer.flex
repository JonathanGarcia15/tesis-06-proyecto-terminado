package Compilador.ES.AnalizadorLexico;
import static Compilador.ES.AnalizadorLexico.Tokens.*;

%%

%class Lexer
%type Tokens

OPERADORASIGNACION="<-"
OPERADORLOGICO="or"|"and"
OPERADORDECOMPARACION="=="|"!="|"<="|">="|"<"|">"
OPERADORNEGACION="!"
OPERADORARITMETICO="\*"|"\/"
OPERADORARITMETICORESTA="\-"
OPERADORARITMETICOSUMA="\+"
CADENA="\"" [^"\""]* "\""
SEPARADOR=","
PUNTOYCOMA=";"
COMENTARIO="..." [^...]* "..."|"//" [^"\n"]* "\n"
NUMERO=[0-9]+
IDENTIFICADOR=[a-zA-Z|"_"][a-zA-Z|0-9|"_"]*
LLAVEABRE="{"
LLAVECIERRA="}"
PARENTESISABRE="("
PROGRAMA=programa
VARIABLE=variable|var
PARENTESISCIERRA=")"
BLANCO=[ |\t|\r]+
SALTOLINEA="\n"
AVANZAR=avanzar
ESPERA=espera
IZQUIERDA=gira_izquierda
DERECHA=gira_derecha
TOMAR=toma_objeto
SOLTAR=soltar_objeto
ELIMINAR=eliminar_objeto
PINTAR=pintar_suelo
DEJAPINTAR=dejar_de_pintar
SI=si
SINO=si_no
COMPARAR=comparar
CASO=caso
FIN=fin
DOSPUNTOS=":"
PARA=para
DEFAULT=default
SINOSI=sinosi
REPITEHASTA=repite_hasta
REPITE=mientras
HACER=hacer
COLORES=azul|verde|amarillo|rojo
TERMINARBLOQUE=terminar
IMPRIMIRVARIABLE=imprimir_var|imprimir_variable
IMPRIMIRCADENA=imprimir
OBJETODELANTE=tengo_objeto_delante
KABOOMDEFRENTE=tengo_bomba_delante|tengo_kaboom_delante
MURODELANTE=tengo_muro_delante
QUETENGODELANTE=delante_de_mi
DESACTIVARKABOOM=desactivar_bomba|desactivar_kaboom
BOOLEANO=verdadero|falso
RANDOM=aleatorio

%%

{IMPRIMIRCADENA} {
    return IMPRIMIRCADENA;
}

{RANDOM} {
    return RANDOM;
}

{BOOLEANO} {
    return BOOLEANO;
}

{OBJETODELANTE} {
    return OBJETODELANTE;
}

{OPERADORARITMETICORESTA} {
    return OPERADORARITMETICORESTA;
}

{KABOOMDEFRENTE} {
    return KABOOMDEFRENTE;
}

{MURODELANTE} {
    return MURODELANTE;
}

{QUETENGODELANTE} {
    return QUETENGODELANTE;
}

{IMPRIMIRVARIABLE} {
    return IMPRIMIRVARIABLE;
}

{TERMINARBLOQUE} {
    return TERMINARBLOQUE;
}

{OPERADORNEGACION} {
    return OPERADORNEGACION;
}

{OPERADORARITMETICOSUMA} {
    return OPERADORARITMETICOSUMA;
}

{CADENA} {
    return CADENA;
}

{PARA} {
    return PARA;
}

{AVANZAR} {
    return AVANZAR;
}

{DOSPUNTOS} {
    return DOSPUNTOS;
}

{COLORES} {
    return COLORES;
}

{SINOSI} {
    return SINOSI;
}

{VARIABLE} {
    return VARIABLE;
}

{PROGRAMA} {
    return PROGRAMA;
}

{OPERADORARITMETICO} {
    return OPERADORARITMETICO;
}

{ESPERA} {
    return ESPERA;
}

{IZQUIERDA} {
    return IZQUIERDA;
}

{DERECHA} {
    return DERECHA;
}

{TOMAR} {
    return TOMAR;
}

{SOLTAR} {
    return SOLTAR;
}

{ELIMINAR} {
    return ELIMINAR;
}

{DESACTIVARKABOOM} {
    return DESACTIVARKABOOM;
}

{PINTAR} {
    return PINTAR;
}

{DEJAPINTAR} {
    return DEJAPINTAR;
}

{SI} {
    return SI;
}

{SINO} {
    return SINO;
}

{COMPARAR} {
    return COMPARAR;
}

{CASO} {
    return CASO;
}

{FIN} {
    return FIN;
}

{DEFAULT} {
    return DEFAULT;
}

{REPITEHASTA} {
    return REPITEHASTA;
}

{REPITE} {
    return REPITE;
}

{HACER} {
    return HACER;
}

{OPERADORASIGNACION} {
    return OPERADORASIGNACION;
}

{OPERADORLOGICO} {
    return OLOGICO;
}

{OPERADORDECOMPARACION} {
    return OCOMPARACION;
}

{SEPARADOR} {
    return SEPARADOR;
}

{PUNTOYCOMA} {
    return PUNTOYCOMA;
}

{COMENTARIO} {
    return COMENTARIO;
  }

{NUMERO} {
    return NUMERO;
}

{IDENTIFICADOR} {
    return IDENTIFICADOR;
}

{LLAVEABRE} {
    return LLAVEABRE;
}

{LLAVECIERRA} {
    return LLAVECIERRA;
}

{PARENTESISABRE} {
    return PARENTESISABRE;
}

{PARENTESISCIERRA} {
    return PARENTESISCIERRA;
}

{BLANCO} {/* Blanco */}

{SALTOLINEA} {
    return SALTOLINEA;
}

. {
    return ERROR;
}