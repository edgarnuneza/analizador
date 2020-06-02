package lexico;
public class Token implements Simbolos{
    
    private Simbolo token;
    private Object atributo;
    
    public Token(Simbolo token) {
        this.token = token;
    }

    public Simbolo getToken() {
        return token;
    }

    public void setToken(Simbolo token) {
        this.token = token;
    }
    @Override
    public String  toString(){
        return token.toString();
    }       
}
    
    

