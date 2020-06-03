package sintaxis;
import java.util.ArrayList;
import lexico.*;

public class AnalizadorSintaxis implements Simbolos
{
    private AnalizadorLexico analizadorLexico = new AnalizadorLexico();
    private Simbolo simbolo;
    private int simboloActual = 0;
    private ArrayList<Simbolo> listaSimbolos;
    private boolean bandera = true;
    
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
                        
                        System.out.println("Se fue a instrucciones");
                        simboloActual++;
                        instruccion();
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
                System.out.println("No tiene cuerpo");
                break;
            } 
            if(listaSimbolos.get(simboloActual) == Simbolo.VAR_SIM)
            {
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
            }
            if(listaSimbolos.get(simboloActual) == Simbolo.CONST_SIM)
            {
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
            }
        }
        
        
        return true;
    }
    
    public boolean tipo()
    {
        Simbolo tipo = listaSimbolos.get(simboloActual);
        if(tipo == Simbolo.INTEGER_SIM || tipo == Simbolo.REAL_SIM || tipo == Simbolo.BOOLEAN_SIM || tipo == Simbolo.CHARACTER_SIM)
        {
            return true;
        }
        error();
        return false;
    }
    
    public boolean instruccion()
    {
        while(listaSimbolos.get(simboloActual) != Simbolo.END_SIM && bandera)
        {
            simbolo = listaSimbolos.get(simboloActual);
            if(listaSimbolos.get(simboloActual) == Simbolo.IDENTIFICADOR)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(listaSimbolos.get(simboloActual) == Simbolo.OP_ASIGNACION)
                {
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
                }
            }
            
            if(listaSimbolos.get(simboloActual) == Simbolo.LLAVE_DER)
            {
                break;
            }
                        
            if(listaSimbolos.get(simboloActual) == Simbolo.IF_SIM)
            {
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
            }
            
            if(listaSimbolos.get(simboloActual) == Simbolo.WHILE_SIM)
            {
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
            }
            
           if(listaSimbolos.get(simboloActual) == Simbolo.FOR_SIM)
           {
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
            if (listaSimbolos.get(simboloActual) == Simbolo.OP_ASIGNACION)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(expresionSimple())
                {
                    return true;
                }
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
        } while (listaSimbolos.get(simboloActual) == Simbolo.OP_SUMA || listaSimbolos.get(simboloActual) == Simbolo.OP_RESTA || listaSimbolos.get(simboloActual) == Simbolo.OP_OR);
        
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
            
        } while (listaSimbolos.get(simboloActual) == Simbolo.OP_MULTIPLICACION || listaSimbolos.get(simboloActual) == Simbolo.OP_DIVISION || listaSimbolos.get(simboloActual) == Simbolo.OP_MODULO || listaSimbolos.get(simboloActual) == Simbolo.OP_AND);
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
               listaSimbolos.get(simboloActual + 1) == Simbolo.OP_DESIGUALDAD)
            {
                simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                /*simboloActual++;
                simbolo = listaSimbolos.get(simboloActual);
                if(expresionSimple())
                {
                    return true;
                }
                else
                {
                    return false;
                }*/
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
                    return true;
                }
                else
                {
                    return false;
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
        
        
        return false;
    }
    
    public void error()
    {
        System.out.println("Hay un error de sintaxis");
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

    public static void main(String[] args) 
    {
        AnalizadorSintaxis analizadorSintaxis = new AnalizadorSintaxis();
    }
    
}

