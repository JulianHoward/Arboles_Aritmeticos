package aritmetico;

import gui.FrameArbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class TestArbolAritmetico {

    private final static Logger logger = (Logger) LogManager.getRootLogger();

    public static void main(String[] args) {

        try {

            FrameArbol frameArbol = new FrameArbol();
            logger.debug("Creando mi frame: visual para el usuario");
            frameArbol.setVisible(true);
            logger.debug("Hago visible el frame para el usuario");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
