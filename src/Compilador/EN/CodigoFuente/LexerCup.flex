package Compilador.EN.Compiler;
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
PROGRAMA=program
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

