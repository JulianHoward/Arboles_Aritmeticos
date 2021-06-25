package gui;

import aritmetico.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PanelArbol extends JPanel implements MouseListener, PropertyChangeListener {

    private final static Logger logger = (Logger) LogManager.getRootLogger();

    private ArbolAritmetico modelo;

    public PanelArbol(ArbolAritmetico obj) {
        modelo = obj;
        this.addMouseListener(this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(10000, 10000);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (modelo != null) {
            DibujoArbol dibujoArbol = new DibujoArbol(modelo);
            dibujoArbol.dibujar(g);
            logger.debug("Dibuja nuestro árbol");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (modelo == null)
            return;
        Arbol.Contenedor<ElementoAritmetico> raiz = modelo.getRaiz();
        Arbol.Contenedor<ElementoAritmetico> seleccionado = accederPosicion(raiz, e);
        ArbolAritmetico prueba = null;
        try {
            if (seleccionado != null) {
                prueba = new ArbolAritmetico(toStringAritmetico(seleccionado));
                prueba.setRaiz(seleccionado);
                String operacion = prueba.toString();
                JOptionPane.showMessageDialog(null, operacion);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }



    public Arbol.Contenedor<ElementoAritmetico> accederPosicion(Arbol.Contenedor<ElementoAritmetico> contenedor, MouseEvent e) {
        Arbol.Contenedor<ElementoAritmetico> seleccionado = null;
        for (Arbol.Contenedor<ElementoAritmetico> hijo : contenedor.getHijos()) {
            seleccionado = accederPosicion(hijo, e);
            if (seleccionado != null) {
                break;
            }
        }

        if (e.getX() >= contenedor.getPosX() && e.getX() <= (contenedor.getPosX() + 40)) {
            logger.debug("El click fue dado dentro de los parámetros de X");
            if (e.getY() >= contenedor.getPosY() && e.getY() <= (contenedor.getPosY() + 40)) {
                logger.debug("El click fue dado dentro de los parámetros de Y");
                return contenedor;
            }
        }

        return seleccionado;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
        logger.debug("Hace un repaint");
    }

    public ArbolAritmetico getModelo() {
        return modelo;
    }

    public void setModelo(ArbolAritmetico modelo) {
        this.modelo = modelo;
    }

    public String toStringAritmetico(Arbol.Contenedor<ElementoAritmetico> nodo) {
        ElementoAritmetico elementoAritmetico = nodo.getContenido();
        if (elementoAritmetico instanceof Numero) {
            return String.valueOf(((Numero) elementoAritmetico).getValor());
        }

        Operador operacion = (Operador) elementoAritmetico;
        String operacionString = operacion.getSimbolo();

        StringBuilder resultado = new StringBuilder();
        String separador = "";
        for (Arbol.Contenedor<ElementoAritmetico> hijo : nodo.getHijos()) {
            String hijoString = toStringAritmetico(hijo);
            resultado.append(separador).append(hijoString);
            separador = operacionString;
        }
        return resultado.toString();
    }
}
