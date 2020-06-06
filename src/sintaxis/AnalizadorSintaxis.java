package sintaxis;
import static java.lang.System.exit;
import java.util.ArrayList;
import lexico.*;

public class AnalizadorSintaxis implements Simbolos
{
    private AnalizadorLexico analizadorLexico = new AnalizadorLexico();
    private Simbolo simbolo;
    private int simboloActual = 0;
    private ArrayList<Simbolo> listaSimbolos;
    
    public AnalizadorSintaxis()
    {
        analizadorLexico.compilar();
        listaSimbolos = analizadorLexico.simbolos;
        programa();
    }

    public boolean programa()
    {
        if(listaSimbolos.get(simboloActual) == Simbolo.PROGRAM_SIM)
        {
            simboloActual++;
            if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
            {
                simboloActual++;
                if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                {
                    simboloActual++;
                    if(declaraciones())
                    {
                        simboloActual++;
                        if(instruccion())
                        {
                            simboloActual++;
                            if(funcion())
                            {
                                System.out.println("Compilación sin errores");
                            }
                            
                        }
  
                    }
                }
                    
            }
        }

        return false;
    }
    
    public boolean declaraciones()
    {
        
        while(listaSimbolos.get(simboloActual) != Simbolo.BEGIN_SIM)
        {
            if(listaSimbolos.get(simboloActual) == Simbolo.FIN_DE_ARCHIVO)
            {
                error();
                break;
            } 
            
            switch(listaSimbolos.get(simboloActual))
            {
                case VAR_SIM:
                    simboloActual++;
                    if(tipo())
                    {

                        do {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
                            {
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                                if(listaSimbolos.get(simboloActual) == Simbolo.OP_ASIGNACION)
                                {
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);
                                    constante();
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);

                                }
                            }
                        } while (listaSimbolos.get(simboloActual) == Simbolo.COMA);

                        if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                        }
                    }
                    else
                        error();
                    break;
                    
                case CONST_SIM:
                    simboloActual++;
                    if(tipo())
                    { 
                        //asignacion(); 
                        do 
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
                            {
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                                if(listaSimbolos.get(simboloActual) == Simbolo.OP_ASIGNACION)
                                {
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);
                                    if(constante())
                                    {
                                        simboloActual++;
                                        simbolo = listaSimbolos.get(simboloActual);
                                    }
                                }
                            }

                        } while (listaSimbolos.get(simboloActual) == Simbolo.COMA);

                        //simboloActual++;
                        if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                        }

                    }
                    break;
                    
                default:
                    error();
                    break;
            }
        }
   
        return true;
    }
    
    public boolean tipo()
    {
        Simbolo tipo = listaSimbolos.get(simboloActual);
        if(tipo == Simbolo.INTEGER_SIM || tipo == Simbolo.REAL_SIM || tipo == Simbolo.BOOLEAN_SIM || tipo == Simbolo.CHARACTER_SIM || tipo == Simbolo.VOID_SIM)
        {
            return true;
        }
 
        return false;
    }
    
    public boolean instruccion()
    {
        boolean bandera = true;
        while(listaSimbolos.get(simboloActual) != Simbolo.END_SIM && bandera)
        {
            simbolo = listaSimbolos.get(simboloActual);
            
            switch(listaSimbolos.get(simboloActual))
            {
                case IDENTIFICADOR:
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    
                    switch(listaSimbolos.get(simboloActual))
                    {
                        case OP_ASIGNACION:
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if(expresionSimple())
                            {
                                if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                                {
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);

                                }
                            }
                            break;
                        case PARENTESIS_IZQ: 
                            do {                            
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                                if(!expresion())
                                {
                                    error();
                                }
                            } while (listaSimbolos.get(simboloActual) == Simbolo.COMA);

                           if (listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                           {
                               simboloActual++;
                               simbolo = listaSimbolos.get(simboloActual);
                               if (listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                               {
                                   simboloActual++;
                                   simbolo = listaSimbolos.get(simboloActual);
                               }

                           }
                           break;

                        case OP_DECREMENTO:
                        case OP_INCREMENTO:
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if (listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                            {
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                            }
                            break;
                            
                        default:
                            error();
                            break;

                    }
                    break;
                    
                case LLAVE_DER:
                    bandera = false;
                    break;

                case IF_SIM:
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_IZQ)
                    {
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        if(expresion())
                        {

                            if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                            {
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                                if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_IZQ)
                                {
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);
                                    if(instruccion())
                                    {
                                        if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_DER)
                                        {
                                           simboloActual++;
                                            simbolo = listaSimbolos.get(simboloActual);
                                            if(listaSimbolos.get(simboloActual) == Simbolo.ELSE_SIM)
                                            {
                                                simboloActual++;
                                                simbolo = listaSimbolos.get(simboloActual);
                                                if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_IZQ)
                                                {
                                                    simboloActual++;
                                                    simbolo = listaSimbolos.get(simboloActual);
                                                    if(instruccion())
                                                    {

                                                        if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_DER)
                                                        {
                                                             simboloActual++;
                                                             simbolo = listaSimbolos.get(simboloActual);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    break;
                    
                case WHILE_SIM:
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_IZQ)
                    {
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        if(expresion())
                        {

                            if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                            {
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                                if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_IZQ)
                                {
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);
                                    if(instruccion())
                                    {
                                        if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_DER)
                                        {
                                            simboloActual++;
                                            simbolo = listaSimbolos.get(simboloActual);
                                        }
                                    }
                                }
                            }

                        }

                    }
                    break;
                            
                case RETURN_SIM:
                    boolean returnVacio = false;

                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);

                    if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                    {
                        returnVacio = true;
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                    }
                    if(expresionSimple() && !returnVacio)
                    {
                        if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                        }
                    }
                    break;
                    
                case FOR_SIM:
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_IZQ)
                    {
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        if(listaSimbolos.get(simboloActual) == Simbolo.VAR_SIM)
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if(tipo())
                            {
                                if(asignacionFor(false))
                                {
                                    if(forParte2())
                                    {
                                        if(instruccion())
                                        {
                                            if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_DER)
                                            {
                                                simboloActual++;
                                                simbolo = listaSimbolos.get(simboloActual);
                                            } 
                                        }
                                    }
                                }
                            }

                        }
                        if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
                        {
                            if(asignacionFor(true))
                            {
                                if(forParte2())
                                {
                                        if(instruccion())
                                        {
                                            if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_DER)
                                            {
                                                simboloActual++;
                                                simbolo = listaSimbolos.get(simboloActual);
                                            } 
                                        }
                                    }
                            }

                        }
                    }
                    break;
                    
                default:
                    error();
                    break;    
            }
        }
        
        return true;
    
    }
    
    public boolean forParte2()
    {
            
            if(expresion())
            {
                
                if(listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA)
                {
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if(asignacion())
                    {
                        
                        if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_IZQ)
                            {
                                simboloActual++;
                                simbolo = listaSimbolos.get(simboloActual);
                                return true;
                            }
                        }
                    }
                }
            }
        
        
        return false;
    }
    
    public boolean asignacionFor(boolean asignacion) {

            do {
                if(!asignacion)
                {
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                }
                asignacion = false;
                if (listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR) {
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if (listaSimbolos.get(simboloActual) == Simbolo.OP_ASIGNACION) {
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        constante();
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);

                    }
                }
            } while (listaSimbolos.get(simboloActual) == Simbolo.COMA);

            if (listaSimbolos.get(simboloActual) == Simbolo.PUNTO_Y_COMA) {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                return true;
            }
 
        return false;
    }
    
    public boolean asignacion()
    {
        if (listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
        {
            simboloActual++;
            simbolo = listaSimbolos.get(simboloActual);
            
            switch(listaSimbolos.get(simboloActual)) 
            {
                case OP_ASIGNACION:
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if(expresionSimple())
                    {
                        return true;
                    }
                    break;
                
                case OP_DECREMENTO:
                case OP_INCREMENTO:
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    return true;
                
                default:
                    error();
            }
            
           
        }
        return false;
    }
    
    public boolean expresionSimple()
    {
        boolean primeraIteracion = true;
        if(listaSimbolos.get(simboloActual) == Simbolo.OP_SUMA || listaSimbolos.get(simboloActual) == Simbolo.OP_RESTA)
        {
            simboloActual++;
            simbolo = listaSimbolos.get(simboloActual);
        }
        
        do {    
            if(!primeraIteracion)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
            }
            primeraIteracion = false;
            if(termino())
            {
                //simboloActual++;
                //simbolo = listaSimbolos.get(simboloActual);
            }
        } while (listaSimbolos.get(simboloActual) == Simbolo.OP_SUMA || listaSimbolos.get(simboloActual) == Simbolo.OP_RESTA);
        
        return true;
    }
    
    public boolean termino()
    {
        boolean primeraIteracion = true;
        do {
            if(!primeraIteracion)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
            }
            primeraIteracion = false;
            if(factor(true))
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
            }
            
        } while (listaSimbolos.get(simboloActual) == Simbolo.OP_MULTIPLICACION || listaSimbolos.get(simboloActual) == Simbolo.OP_DIVISION || listaSimbolos.get(simboloActual) == Simbolo.OP_MODULO);
        return true;
    }
    
    public boolean expresion()
    {
        if(expresionSimple())
        {
            if(listaSimbolos.get(simboloActual + 1)  == Simbolo.OP_MAYOR_IGUAL || 
               listaSimbolos.get(simboloActual + 1) == Simbolo.OP_MENOR_IGUAL ||
               listaSimbolos.get(simboloActual + 1) == Simbolo.OP_MENOR_QUE ||
               listaSimbolos.get(simboloActual + 1) == Simbolo.OP_MAYOR_QUE ||
               listaSimbolos.get(simboloActual + 1) == Simbolo.OP_IGUALDAD ||
               listaSimbolos.get(simboloActual + 1) == Simbolo.OP_DESIGUALDAD
               
                  )
            {
                
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);

            }
            if(listaSimbolos.get(simboloActual)  == Simbolo.OP_MAYOR_IGUAL || 
               listaSimbolos.get(simboloActual) == Simbolo.OP_MENOR_IGUAL ||
               listaSimbolos.get(simboloActual) == Simbolo.OP_MENOR_QUE ||
               listaSimbolos.get(simboloActual) == Simbolo.OP_MAYOR_QUE ||
               listaSimbolos.get(simboloActual) == Simbolo.OP_IGUALDAD ||
               listaSimbolos.get(simboloActual) == Simbolo.OP_DESIGUALDAD)
            {
                
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(expresionSimple())
                {
                    if(listaSimbolos.get(simboloActual) == Simbolo.OP_AND || listaSimbolos.get(simboloActual) == Simbolo.OP_OR)
                    {
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        if(expresion())
                        {
                            return true;
                        }
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
            
            if(listaSimbolos.get(simboloActual) == Simbolo.OP_AND || listaSimbolos.get(simboloActual) == Simbolo.OP_OR)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(expresion())
                {
                    return true;
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean factor(boolean opValido)
    {
        
        if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_IZQ)
        {
            simboloActual++;
            simbolo = listaSimbolos.get(simboloActual);
            if(expresion())
            {

                if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                {
                    return true;
                }
                
                int a = (simboloActual);
            }
            
        }
        if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
        {
            if(listaSimbolos.get(simboloActual + 1) == Simbolo.PARENTESIS_IZQ)
            {
                simboloActual += 1;
                simbolo = listaSimbolos.get(simboloActual);
                do {                            
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        if(!expresion())
                        {
                            error();
                        }
                } while (listaSimbolos.get(simboloActual) == Simbolo.COMA);
                        
                if (listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                {
                   return true;
                           
                }
            }
            return true;
        }
        if(constante() && opValido)
        {
            return true;
        }
        if(listaSimbolos.get(simboloActual) == Simbolo.OP_NOT)
        {
            simboloActual++;
            simbolo = listaSimbolos.get(simboloActual);
            if(factor(false))
            {
                return true;
                
            }
        }
      
        error();
        return false;
    }
    
    public void error()
    {
        System.out.println("Hay un error de sintaxis");
        exit(0);
    }

    public boolean constante()
    {
        
        Simbolo cons = listaSimbolos.get(simboloActual);
        if(listaSimbolos.get(simboloActual) == Simbolo.OP_SUMA || listaSimbolos.get(simboloActual) == Simbolo.OP_RESTA)
        {
            simboloActual++;
            cons = listaSimbolos.get(simboloActual);
            if(cons == Simbolo.CTE_ENTERA || cons == Simbolo.CTE_REAL)
            {
                return true;
            }
        }
        if(cons == Simbolo.CTE_BOOLEANA || cons == Simbolo.CTE_CHAR || cons == Simbolo.CTE_LITERAL || cons == Simbolo.CTE_ENTERA || cons == Simbolo.CTE_REAL)
        {
            return true;
        }
        return false;
        
    }
    
    public boolean parametros()
    {
        if(listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
        {
            return true;
        }
        do {
            if(tipo())
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
                {
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                }
                else
                {
                    return false;
                }
            }
            
        } while (listaSimbolos.get(simboloActual) == Simbolo.COMA);
        
        return true;
    }
    
    public boolean funcion()
    {
        while(listaSimbolos.get(simboloActual) != Simbolo.FIN_DE_ARCHIVO)
        {
            if(listaSimbolos.get(simboloActual) == Simbolo.FUNCTION_SIM)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(tipo())
                {
                    simboloActual++;
                    simbolo = listaSimbolos.get(simboloActual);
                    if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
                    {
                        simboloActual++;
                        simbolo = listaSimbolos.get(simboloActual);
                        if (listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_IZQ)
                        {
                            simboloActual++;
                            simbolo = listaSimbolos.get(simboloActual);
                            if(parametros())
                            {
                                if (listaSimbolos.get(simboloActual) == Simbolo.PARENTESIS_DER)
                                {
                                    simboloActual++;
                                    simbolo = listaSimbolos.get(simboloActual);
                                    if(declaraciones())
                                    {
                                        simboloActual++;
                                        simbolo = listaSimbolos.get(simboloActual);
                                        if(instruccion())
                                        {
                                            
                                            if (listaSimbolos.get(simboloActual) == Simbolo.END_SIM)
                                            {
                                                simboloActual++;
                                                simbolo = listaSimbolos.get(simboloActual);
                                            }
                                        }
                                                
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    

    public static void main(String[] args) 
    {
        AnalizadorSintaxis analizadorSintaxis = new AnalizadorSintaxis();
    }
    
}

