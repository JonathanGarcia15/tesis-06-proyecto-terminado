package Compilador.EN.AnalizadorLexico;
import static Compilador.EN.AnalizadorLexico.Tokens.*;

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
PROGRAMA=program|main_function|main
VARIABLE=variable|var
PARENTESISCIERRA=")"
BLANCO=[ |\t|\r]+
SALTOLINEA="\n"
AVANZAR=move
ESPERA=wait
IZQUIERDA=turn_left
DERECHA=turn_right
TOMAR=pick_up
SOLTAR=put_down
ELIMINAR=delete_object
PINTAR=paint_floor
DEJAPINTAR=stop_painting
SI=if
SINO=else
COMPARAR=compare
CASO=case
FIN=end
DOSPUNTOS=":"
PARA=for
DEFAULT=default
SINOSI=else_if
REPITEHASTA=do_over
REPITE=while
HACER=do
COLORES=blue|green|yellow|red
TERMINARBLOQUE=finish
IMPRIMIRVARIABLE=print_var|print_variable
IMPRIMIRCADENA=print
OBJETODELANTE=object_in_front
KABOOMDEFRENTE=bomb_in_front|kaboom_in_front
MURODELANTE=wall_in_front
QUETENGODELANTE=in_front_me
DESACTIVARKABOOM=disable_bomb|disable_kaboom
BOOLEANO=true|false
RANDOM=random

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