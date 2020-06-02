package lexico;
import java.io.*;
import java.io.EOFException;
import java.util.ArrayList;

public class AnalizadorLexico implements Simbolos {

    private BufferedReader entrada;
    private PrintWriter salida; // para enviar el objeto a un archivo
    int numeroLinea;
    String linea;
    int indice;
    String[] tablaIdentificadores = new String[100];
    private int numeroIdentificadores;
    private Token actualToken;
    private char c;
    private int atributo;
    private double numeroReal = 0;
    private String lexema;
    final float pi = 3.1416f;
    public ArrayList<Simbolo> simbolos = new ArrayList<>();

    public AnalizadorLexico() {
        numeroLinea = 0;
        indice = 0;
        linea = null;
        numeroIdentificadores = 0;
        atributo = -1;
    }

    public static void main(String[] args) {
        AnalizadorLexico miAnalizadorLexico = new AnalizadorLexico();
        miAnalizadorLexico.compilar();
    }

    
    public Simbolo getSimbolo()
    {
        c = getNextChar();
        actualToken = getNextToken();
        return actualToken.getToken();
    }
    
    
    public void compilar() {
        //abrir archivos
        abrirArchivoEntrada();
        abrirArchivoSalida();
        //lee el primer caracter del archivo
        c = getNextChar();

        //obtiene el primer token
        actualToken = getNextToken();
        //obtiene uno a uno el resto de tokens del archivo
        while (actualToken.getToken() != Simbolo.FIN_DE_ARCHIVO) 
        {
            simbolos.add(actualToken.getToken());
           /* System.out.println(actualToken);
            if (actualToken.getToken() == Simbolo.IDENTIFICADOR) 
            {
               System.out.println("ID: " + lexema + ", INDICE: " + atributo);
            }
            if (actualToken.getToken() == Simbolo.CTE_ENTERA) {
                System.out.println("VALOR: " + atributo);
            }
            if(actualToken.getToken() == Simbolo.CTE_REAL)
            {
                System.out.println("VALOR: " + numeroReal);
            }
            if(actualToken.getToken() == Simbolo.CTE_BOOLEANA)
            {
                System.out.println("VALOR: " + lexema);
            }
            if (actualToken.getToken() == Simbolo.CTE_CHAR) {
                System.out.println("VALOR: " + lexema);
            }
            if(actualToken.getToken() == Simbolo.CTE_LITERAL)
            {
                System.out.println("VALOR: " + lexema);
            }*/
            actualToken = getNextToken();
        }
        
        simbolos.add(Simbolo.FIN_DE_ARCHIVO);
        //encontro el fin de archivo
        //System.out.println(actualToken);
        //cerrar archivos
        cerrarArchivoEntrada();
        cerrarArchivoSalida();
    }

    public Token getNextToken() {
        //salta espacios, caracteres fin de linea, tabs

        while (c == ' ') {
            c = getNextChar();
        }
        if (esLetra(c)) {
            //recoge todo el lexema
            lexema = "" + c;
            c = getNextChar();

            while (esLetra(c) || esDigito(c)) {
                lexema += c;
                c = getNextChar();

            }
            //checa si es palabra reservada
            int i = busca(Simbolos.tablaPalabrasReservadas, lexema);
            if (i == -1) 
            {
                if(lexema.equals("true") || lexema.equals("false"))
                {
                    return new Token(Simbolo.CTE_BOOLEANA);
                }
                
                atributo = buscaEInserta(lexema);
                return new Token(Simbolo.IDENTIFICADOR);
            } else {
                return new Token(Simbolo.valueOf(tablaSimbolosPalabrasReservadas[i]));
            }
        } else if (esDigito(c)) {
            int valor = c - '0';
            c = getNextChar();            
            
            while (esDigito(c)) {
                valor = valor * 10 + c - '0';
                c = getNextChar();

            }

            if (c == '.') {
                char punto = c;
                c = getNextChar();
                
                if(!esDigito(c))
                {
                    c = punto;
                    indice--;
                    atributo = valor;
                    return new Token(Simbolo.CTE_ENTERA);
                }
                
                numeroReal = valor;
                //System.out.println(numeroReal);
                double parteReal;
                int potencia = 1;
                while (esDigito(c)) {
                    parteReal = (c - 48);
                   
                    numeroReal = numeroReal + parteReal / Math.pow(10, potencia);
                    potencia++;
                    c = getNextChar();

                }
                //atributo = valor;
                return new Token(Simbolo.CTE_REAL);
            }
            atributo = valor;
            return new Token(Simbolo.CTE_ENTERA);
        } else if (c == '"') {
            c = getNextChar();
            lexema = "";
            while (c != '"') {
                lexema += c;
                c = getNextChar();
            }
            c = getNextChar();
            return new Token(Simbolo.CTE_LITERAL);

        }

        switch (c) {

            case '(':
                c = getNextChar();

                if (c == '*') {//es un comentario de varias lineas

                    c = getNextChar();

                    boolean finComentario = false;
                    while (!finComentario && c != '\0') {
                        while (c != '*' && c != '\0') {
                            c = getNextChar();
                        }
                        if (c == '\0') {
                            System.out.println("Error de lexico: Se encontro el fin de archivo y hubo un comentario sin cerrar");
                            return new Token(Simbolo.FIN_DE_ARCHIVO);
                        }
                        c = getNextChar();
                        if (c == ')') {//termino el comentario
                            c = getNextChar();
                            finComentario = true;
                            return getNextToken();
                        }

                    }
                    if (c == '\0') {
                        System.out.println("Error de lexico: Se encontro el fin de archivo y hubo un comentario sin cerrar");
                    }

                } else {
                    return new Token(Simbolo.PARENTESIS_IZQ);
                }

            /*if (c == '/') {//es un comentario hasta el fin de linea

                    c = getNextChar();

                    while (c != '\n') {
                        c = getNextChar();
                    }

                    return getNextToken();
                } else {
                    return new Token(Simbolo.OP_DIVISION);
                }*/
            case '/':
                c = getNextChar();
                return new Token(Simbolo.OP_DIVISION);

            case ':':
                c = getNextChar();
                if (c == '=') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_ASIGNACION);
                } else {
                    return new Token(Simbolo.DOS_PUNTOS);
                }
            case '=':
                c = getNextChar();

                return new Token(Simbolo.OP_IGUALDAD);

            case '+':
                c = getNextChar();

                if (c == '+') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_INCREMENTO);
                } else {
                    return new Token(Simbolo.OP_SUMA);
                }
            case '-':
                c = getNextChar();

                if (c == '-') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_DECREMENTO);
                } else {
                    return new Token(Simbolo.OP_RESTA);
                }
            case '*':
                c = getNextChar();
                return new Token(Simbolo.OP_MULTIPLICACION);

            case '%':
                c = getNextChar();

                return new Token(Simbolo.OP_MODULO);

            case '>':
                c = getNextChar();

                if (c == '=') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_MAYOR_IGUAL);
                } else {
                    return new Token(Simbolo.OP_MAYOR_QUE);
                }
            case '<':
                c = getNextChar();

                if (c == '=') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_MENOR_IGUAL);
                } else {
                    return new Token(Simbolo.OP_MENOR_QUE);
                }
            case '!':
                c = getNextChar();

                if (c == '=') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_DESIGUALDAD);
                } else {
                    return new Token(Simbolo.OP_NOT);
                }
            case '&':
                c = getNextChar();

                if (c == '&') {
                    c = getNextChar();

                    return new Token(Simbolo.OP_AND);
                }
            // return new Token(Simbolo.OP_AND_BOLEANO);
            case '|':
                c = getNextChar();

                if (c == '|') {
                    c = getNextChar();
                    return new Token(Simbolo.OP_OR);
                }
            // return new Token(Simbolo.OP_OR_BOLEANO);

            case '\'':
                c = getNextChar();
                lexema = "" + c;
                c = getNextChar();
                if (c != '\'') {
                    System.out.println("Error: literal de tipo char mal construida");
                    return new Token(Simbolo.OTRO);
                }
                c = getNextChar();
                return new Token(Simbolo.CTE_CHAR);
            case ' ':
                c = getNextChar();
                return getNextToken();
            case '\n':
                c = getNextChar();
                return getNextToken();
            case '\t':
                c = getNextChar();
                return getNextToken();
            case ';':
                c = getNextChar();
                return new Token(Simbolo.PUNTO_Y_COMA);

            case ',':
                c = getNextChar();
                return new Token(Simbolo.COMA);
            case '{':
                c = getNextChar();
                return new Token(Simbolo.LLAVE_IZQ);
            case '}':
                c = getNextChar();
                return new Token(Simbolo.LLAVE_DER);

            case ')':
                c = getNextChar();
                return new Token(Simbolo.PARENTESIS_DER);
            case '\0':
                return new Token(Simbolo.FIN_DE_ARCHIVO);
            default:
                System.out.println("(" + numeroLinea + ")ERROR DE LEXICO: Caracter no valido: " + c);
                c = getNextChar();
                return new Token(Simbolo.OTRO);
        }
    }

    public char getNextChar() {
        if (linea != null && indice < linea.length()) {
            return linea.charAt(indice++);
        }

        try {

            linea = entrada.readLine();
            //System.out.println(""+linea);
            indice = 0;
            if (linea != null) {
                //enviar a archivo de listado
                salida.println(++numeroLinea + ":  " + linea);
                //if(indice < linea.length())
                //    return linea.charAt(indice++);
                //else{
                //System.out.println("regresando fin de linea");
                return '\n';
                //}
            } else {
                return '\0';
            }
        } catch (NullPointerException e) {
            return '\0'; // salir de metodo  
        } catch (EOFException e) {
            return '\0'; // salir de metodo

        } catch (IOException e) {
            System.err.println("Error al leer caracteres del archivo");
            return '\0';
        }

    }

    public boolean esLetra(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
            return true;
        }
        return false;
    }

    public boolean esDigito(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    public int busca(String[] tabla, String s) {
        for (int i = 0; i < tabla.length; i++) {
            if (tabla[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public int buscaEInserta(String s) {
        for (int i = 0; i < numeroIdentificadores; i++) {
            if (tablaIdentificadores[i].equals(s)) {
                return i;
            }
        }
        tablaIdentificadores[numeroIdentificadores] = s;
        return numeroIdentificadores++;
    }

    public void abrirArchivoEntrada() {
        try {
            File archivoEntrada = new File("entrada.txt");
            if (!archivoEntrada.exists()) {
                archivoEntrada.createNewFile();
            }
            entrada = new BufferedReader(new FileReader(archivoEntrada));

        } catch (FileNotFoundException e) {
            System.err.println("Error, archivo no existente.");

        } catch (IOException e) {
            System.err.println("Error al abrir el archivo.");
        }
    }

    public void abrirArchivoSalida() {
        // crear un archivo
        try {
            File archivoSalida = new File("salida.txt");
            if (!archivoSalida.exists()) {
                archivoSalida.createNewFile();
            }
            salida = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(archivoSalida)));
        } catch (IOException e) {
            System.err.println("Error al tratar de crear el archivo.");
        }
    }

    public void cerrarArchivoEntrada() {
        try {
            if (entrada != null) {
                entrada.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo.");
        }
    }

    public void cerrarArchivoSalida() {
        if (salida != null) {
            salida.close();
        }

    }

}
