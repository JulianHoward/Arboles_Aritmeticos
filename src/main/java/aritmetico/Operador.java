package aritmetico;

public class Operador extends ElementoAritmetico {

    private String simbolo;
    private String nombre;

    public Operador(String simbolo){
        this.simbolo = simbolo;
        if(simbolo.equals("+")){
            nombre = "Suma";
        }
        if(simbolo.equals("-")){
            nombre = "Resta";
        }
        if(simbolo.equals("*")){
            nombre = "Multiplicacion";
        }
        if(simbolo.equals("/")){
            nombre = "Division";
        }
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getNombre() {
        return nombre;
    }

}
