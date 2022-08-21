package Compilador.Default.Compiler;
import java_cup.runtime.Symbol;

%%

%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char

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

%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%

{IMPRIMIRCADENA} {
    return new Symbol(sym.IMPRIMIRCADENA, yychar, yyline, yytext());
}

{OPERADORLOGICO} {
    return new Symbol(sym.OPERADORLOGICO, yychar, yyline, yytext());
}

{RANDOM} {
    return new Symbol(sym.RANDOM, yychar, yyline, yytext());
}

{BOOLEANO} {
    return new Symbol(sym.BOOLEANO, yychar, yyline, yytext());
}

{OBJETODELANTE} {
    return new Symbol(sym.OBJETODELANTE, yychar, yyline, yytext());
}

{OPERADORARITMETICORESTA} {
    return new Symbol(sym.OPERADORARITMETICORESTA, yychar, yyline, yytext());
}

{KABOOMDEFRENTE} {
    return new Symbol(sym.KABOOMDEFRENTE, yychar, yyline, yytext());
}

{MURODELANTE} {
    return new Symbol(sym.MURODELANTE, yychar, yyline, yytext());
}

{QUETENGODELANTE} {
    return new Symbol(sym.QUETENGODELANTE, yychar, yyline, yytext());
}

{IMPRIMIRVARIABLE} {
    return new Symbol(sym.IMPRIMIRVARIABLE, yychar, yyline, yytext());
}

{TERMINARBLOQUE} {
    return new Symbol(sym.TERMINARBLOQUE, yychar, yyline, yytext());
}

{OPERADORNEGACION} {
    return new Symbol(sym.OPERADORNEGACION, yychar, yyline, yytext());
}

{OPERADORARITMETICOSUMA} {
    return new Symbol(sym.OPERADORARITMETICOSUMA, yychar, yyline, yytext());
}

{CADENA} {
    return new Symbol(sym.CADENA, yychar, yyline, yytext());
}

{PARA} {
    return new Symbol(sym.PARA, yychar, yyline, yytext());
}

{AVANZAR} {
    return new Symbol(sym.AVANZAR, yychar, yyline, yytext());
}

{DOSPUNTOS} {
    return new Symbol(sym.DOSPUNTOS, yychar, yyline, yytext());
}

{COLORES} {
    return new Symbol(sym.COLORES, yychar, yyline, yytext());
}

{SINOSI} {
    return new Symbol(sym.SINOSI, yychar, yyline, yytext());
}

{VARIABLE} {
    return new Symbol(sym.VARIABLE, yychar, yyline, yytext());
}

{PROGRAMA} {
    return new Symbol(sym.PROGRAMA, yychar, yyline, yytext());
}

{OPERADORARITMETICO} {
    return new Symbol(sym.OPERADORARITMETICO, yychar, yyline, yytext());
}

{ESPERA} {
    return new Symbol(sym.ESPERA, yychar, yyline, yytext());
}

{IZQUIERDA} {
    return new Symbol(sym.IZQUIERDA, yychar, yyline, yytext());
}

{DERECHA} {
    return new Symbol(sym.DERECHA, yychar, yyline, yytext());
}

{TOMAR} {
    return new Symbol(sym.TOMAR, yychar, yyline, yytext());
}

{SOLTAR} {
    return new Symbol(sym.SOLTAR, yychar, yyline, yytext());
}

{ELIMINAR} {
    return new Symbol(sym.ELIMINAR, yychar, yyline, yytext());
}

{DESACTIVARKABOOM} {
    return new Symbol(sym.DESACTIVARKABOOM, yychar, yyline, yytext());
}

{PINTAR} {
    return new Symbol(sym.PINTAR, yychar, yyline, yytext());
}

{DEJAPINTAR} {
    return new Symbol(sym.DEJAPINTAR, yychar, yyline, yytext());
}

{SI} {
    return new Symbol(sym.SI, yychar, yyline, yytext());
}

{SINO} {
    return new Symbol(sym.SINO, yychar, yyline, yytext());
}

{COMPARAR} {
    return new Symbol(sym.COMPARAR, yychar, yyline, yytext());
}

{CASO} {
    return new Symbol(sym.CASO, yychar, yyline, yytext());
}

{FIN} {
    return new Symbol(sym.FIN, yychar, yyline, yytext());
}

{DEFAULT} {
    return new Symbol(sym.DEFAULT, yychar, yyline, yytext());
}

{REPITEHASTA} {
    return new Symbol(sym.REPITEHASTA, yychar, yyline, yytext());
}

{REPITE} {
    return new Symbol(sym.REPITE, yychar, yyline, yytext());
}

{HACER} {
    return new Symbol(sym.HACER, yychar, yyline, yytext());
}

{OPERADORASIGNACION} {
    return new Symbol(sym.OPERADORASIGNACION, yychar, yyline, yytext());
}

{OPERADORDECOMPARACION} {
    return new Symbol(sym.OPERADORDECOMPARACION, yychar, yyline, yytext());
}

{SEPARADOR} {
    return new Symbol(sym.SEPARADOR, yychar, yyline, yytext());
}

{PUNTOYCOMA} {
    return new Symbol(sym.PUNTOYCOMA, yychar, yyline, yytext());
}

{COMENTARIO} { /* Se ignoran los comentarios */ }

{NUMERO} {
    return new Symbol(sym.NUMERO, yychar, yyline, yytext());
}

{IDENTIFICADOR} {
    return new Symbol(sym.IDENTIFICADOR, yychar, yyline, yytext());
}

{LLAVEABRE} {
    return new Symbol(sym.LLAVEABRE, yychar, yyline, yytext());
}

{LLAVECIERRA} {
    return new Symbol(sym.LLAVECIERRA, yychar, yyline, yytext());
}

{PARENTESISABRE} {
    return new Symbol(sym.PARENTESISABRE, yychar, yyline, yytext());
}

{PARENTESISCIERRA} {
    return new Symbol(sym.PARENTESISCIERRA, yychar, yyline, yytext());
}

{BLANCO} {/* Se ignoran los espacios en Blanco */}

{SALTOLINEA} {/* Se ignoran los saltos de Línea */}

. {
    return new Symbol(sym.ERROR, yychar, yyline, yytext());
}

