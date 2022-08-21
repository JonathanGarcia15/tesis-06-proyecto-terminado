package Compilador.Default.AnalizadorLexico;
import static Compilador.Default.AnalizadorLexico.Tokens.*;

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
PROGRAMA=program|programa
VARIABLE=variable|var
PARENTESISCIERRA=")"
BLANCO=[ |\t|\r]+
SALTOLINEA="\n"
AVANZAR=move|avanzar
ESPERA=wait|espera
IZQUIERDA=turn_left|gira_izquierda
DERECHA=turn_right|gira_derecha
TOMAR=pick_up|toma_objeto
SOLTAR=put_down|soltar_objeto
ELIMINAR=delete_object|eliminar_objeto
PINTAR=paint_floor|pintar_suelo
DEJAPINTAR=stop_painting|dejar_de_pintar
SI=if|si
SINO=else|si_no
COMPARAR=compare|comparar
CASO=case|caso
FIN=end|fin
DOSPUNTOS=":"
PARA=for|para
DEFAULT=default
SINOSI=else_if|sinosi
REPITEHASTA=do_over|repite_hasta
REPITE=while|mientras
HACER=do|hacer
COLORES=azul|blue|green|verde|amarillo|yellow|red|rojo
TERMINARBLOQUE=finish|terminar
IMPRIMIRVARIABLE=print_var|imprimir_var|print_variable|imprimir_variable
IMPRIMIRCADENA=print|imprimir
OBJETODELANTE=object_in_front|tengo_objeto_delante
KABOOMDEFRENTE=bomb_in_front|tengo_bomba_delante|kaboom_in_front|tengo_kaboom_delante
MURODELANTE=wall_in_front|tengo_muro_delante
QUETENGODELANTE=in_front_me|delante_de_mi
DESACTIVARKABOOM=disable_bomb|desactivar_bomba|disable_kaboom|desactivar_kaboom
BOOLEANO=true|false|verdadero|falso
RANDOM=random|aleatorio

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