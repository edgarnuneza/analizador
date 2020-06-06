package lexico;
public interface Simbolos {

    //terminar de definir los simbolos y palabras reservadas de Java
    public enum Simbolo {
        IDENTIFICADOR,
        PROGRAM_SIM,
        BEGIN_SIM,
        END_SIM,
        VAR_SIM,
        CONST_SIM,
        REAL_SIM,
        BOOLEAN_SIM,
        INTEGER_SIM,
        CHARACTER_SIM,
        IF_SIM,
        ELSE_SIM,
        WHILE_SIM,
        FOR_SIM,
        VOID_SIM,
        FUNCTION_SIM,
        RETURN_SIM,
        CTE_ENTERA,
        CTE_BOOLEANA,
        CTE_CHAR,
        CTE_LITERAL,
        CTE_REAL,
        OP_ASIGNACION,
        OP_DESIGUALDAD,
        OP_IGUALDAD,
        OP_MENOR_QUE,
        OP_MAYOR_QUE,
        OP_MENOR_IGUAL,
        OP_MAYOR_IGUAL,
        OP_MULTIPLICACION,
        OP_SUMA,
        OP_DIVISION,
        OP_RESTA,
        OP_AND,
        OP_OR,
        OP_INCREMENTO,
        OP_MODULO,
        OP_DECREMENTO,
        OP_NOT,
        PUNTO_Y_COMA,
        DOS_PUNTOS,
        COMA,
        PARENTESIS_IZQ,
        PARENTESIS_DER,
        LLAVE_IZQ,
        LLAVE_DER,
        ABRIR_COMENTARIO,
        CERRAR_COMENTARIO,
        FIN_DE_ARCHIVO,
        OTRO
    }

    public String[] tablaPalabrasReservadas
            = {"program", "begin", "end", "var", "const", "real",
                "boolean", "integer", "character", "if", "else",
                "while", "for", "void", "function", "return"};
    public String[] tablaSimbolosPalabrasReservadas
            = {"PROGRAM_SIM", "BEGIN_SIM", "END_SIM", "VAR_SIM", "CONST_SIM", "REAL_SIM",
                "BOOLEAN_SIM", "INTEGER_SIM", "CHARACTER_SIM", "IF_SIM", "ELSE_SIM",
                "WHILE_SIM", "FOR_SIM", "VOID_SIM", "FUNCTION_SIM", "RETURN_SIM"};
}
