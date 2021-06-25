package aritmetico;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ArbolAritmetico extends Arbol<ElementoAritmetico> {

    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private PropertyChangeSupport observed;

    public ArbolAritmetico() {
        super();
    }

    public ArbolAritmetico(String expresion) throws Exception {
        raiz = leerExpresion(null, expresion);
        observed = new PropertyChangeSupport(this);
    }

    public void addObserver(PropertyChangeListener panel) {
        observed.addPropertyChangeListener(panel);
    }

    public void cambioOk() {
        observed.firePropertyChange("Modelo", 1, 2);
        logger.debug("Cambio Detectado");
    }

    private Contenedor<ElementoAritmetico> leerExpresion(
            Contenedor<ElementoAritmetico> padre, String expresionSucia)
            throws Exception {

        String expresion = expresionSucia.trim();

        int largo = expresion.length();

        int posicionActual = 0;
        int conteoParentesis = 0;

        double posibleNumeroExpresion = Double.MIN_VALUE;
        try {
            posibleNumeroExpresion = Double.parseDouble(expresion);
            Contenedor<ElementoAritmetico> resultadoNodo =
                    new Contenedor<>(new Numero(posibleNumeroExpresion));
            resultadoNodo.setPadre(padre);
            return resultadoNodo;
        } catch (Exception e) {

        }

        while (posicionActual < largo) {

            char caracterActual = expresion.charAt(posicionActual);

            if (caracterActual == ' ' || caracterActual == '\t') {
                posicionActual++;
                continue;
            }

            if (caracterActual == '.') {
                posicionActual++;
                logger.debug("Encuentra un . y la posición actual incrementa en una unidad");
                continue;
            }

            int numero = Integer.MIN_VALUE;
            try {
                numero = Integer.parseInt(String.valueOf(caracterActual));
                posicionActual++;
                continue;
            } catch (Exception q) {
                numero = Integer.MIN_VALUE;

            }

            if (caracterActual == '(') {
                conteoParentesis++;
                posicionActual++;
                logger.debug("Encuentra un paréntesis abierto");
                continue;
            }

            if (caracterActual == ')') {
                conteoParentesis--;
                posicionActual++;
                logger.debug("Encuentra un paréntesis cerrado");
                continue;
            }

            Operador posibleOperadorPrincipal = null;
            try {
                posibleOperadorPrincipal = new Operador(String.valueOf(caracterActual));
                if (conteoParentesis == 0) {



                    Contenedor<ElementoAritmetico> resultadoNodo =
                            new Contenedor<ElementoAritmetico>(posibleOperadorPrincipal);
                    resultadoNodo.setPadre(padre);

                    // 1. Crear el NODO IZQUIERDA
                    String izquierdaExpresion = expresion.substring(0, posicionActual);
                    Contenedor<ElementoAritmetico> izquierdaNodo = leerExpresion(resultadoNodo, izquierdaExpresion);
                    resultadoNodo.getHijos().add(izquierdaNodo);

                    // 2. Crear el NODO DERECHA
                    String derechaExpresion = expresion.substring(posicionActual + 1);
                    Contenedor<ElementoAritmetico> derechaNodo = leerExpresion(resultadoNodo, derechaExpresion);
                    resultadoNodo.getHijos().add(derechaNodo);

                    return resultadoNodo;
                } else {
                    posicionActual++;
                    continue;
                }
            } catch (Exception q) {
                posicionActual++;
                continue;
            }
        }

        if (expresion.startsWith("(") && expresion.endsWith(")") && conteoParentesis == 0)
            return leerExpresion(padre, expresion.substring(1, expresion.length() - 1));

        throw new Exception("Expresión no leída por errores al ingresarla");
    }

    public double evaluar() {
        if (raiz == null)
            return Double.MIN_VALUE;
        return evaluarContenedor(raiz);
    }

    public double evaluarContenedor(Contenedor<ElementoAritmetico> nodo) {
        if (nodo == null)
            return 0.0;

        ElementoAritmetico elementoAritmetico = nodo.getContenido();
        if (elementoAritmetico instanceof Numero) {
            return ((Numero) elementoAritmetico).getValor();
        }

        Operador operacion = (Operador) elementoAritmetico;
        if (operacion.getNombre().equals("Suma")) {
            logger.debug("Encuentra signo de suma '+' ");
            return sumarHijos(nodo);
        }
        if (operacion.getNombre().equals("Resta")) {
            logger.debug("Encuentra signo de resta '-' ");
            return restarHijos(nodo);
        }
        if (operacion.getNombre().equals("Multiplicacion")) {
            logger.debug("Encuentra signo de multiplicación '*' ");
            return multiplicarHijos(nodo);
        }
        if (operacion.getNombre().equals("Division")) {
            logger.debug("Encuentra signo de división '/' ");
            return dividirHijos(nodo);
        }

        return Double.MIN_VALUE;
    }

    private double dividirHijos(Contenedor<ElementoAritmetico> nodo) {
        double resultado = 0;
        boolean primero = true;
        for (Contenedor<ElementoAritmetico> hijo : nodo.getHijos()) {
            double evaluarHijo = evaluarContenedor(hijo);
            if (primero) {
                resultado += evaluarHijo;
                primero = false;
            } else {
                resultado /= evaluarHijo;
            }
        }
        return resultado;
    }

    private double multiplicarHijos(Contenedor<ElementoAritmetico> nodo) {
        double resultado = 0;
        boolean primero = true;
        for (Contenedor<ElementoAritmetico> hijo : nodo.getHijos()) {
            double evaluarHijo = evaluarContenedor(hijo);
            if (primero) {
                resultado += evaluarHijo;
                primero = false;
            } else {
                resultado *= evaluarHijo;
            }
        }
        return resultado;
    }

    private double restarHijos(Contenedor<ElementoAritmetico> nodo) {
        double resultado = 0;
        boolean primero = true;
        for (Contenedor<ElementoAritmetico> hijo : nodo.getHijos()) {
            double evaluarHijo = evaluarContenedor(hijo);
            if (primero) {
                resultado += evaluarHijo;
                primero = false;
            } else {
                resultado -= evaluarHijo;
            }
        }
        return resultado;
    }

    private double sumarHijos(Contenedor<ElementoAritmetico> nodo) {
        double resultado = 0;
        for (Contenedor<ElementoAritmetico> hijo : nodo.getHijos()) {
            double evaluarHijo = evaluarContenedor(hijo);
            resultado += evaluarHijo;
        }
        return resultado;
    }

    @Override
    public String toString() {
        return toStringAritmetico(raiz) + " = " + String.valueOf(evaluar());
    }

    public String toStringAritmetico(Contenedor<ElementoAritmetico> nodo) {
        ElementoAritmetico elementoAritmetico = nodo.getContenido();
        if (elementoAritmetico instanceof Numero) {
            return String.valueOf(((Numero) elementoAritmetico).getValor());
        }

        Operador operacion = (Operador) elementoAritmetico;
        String operacionString = operacion.getSimbolo();

        StringBuilder resultado = new StringBuilder();
        resultado.append("(");
        String separador = "";
        for (Contenedor<ElementoAritmetico> hijo : nodo.getHijos()) {
            String hijoString = toStringAritmetico(hijo);
            resultado.append(separador).append(hijoString);
            separador = operacionString;
        }
        resultado.append(")");
        return resultado.toString();
    }
}
