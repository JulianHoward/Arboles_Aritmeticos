package gui;

import aritmetico.Arbol;
import aritmetico.ArbolAritmetico;
import aritmetico.Persona;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FrameArbol extends JFrame {

    private final static Logger logger = (Logger) LogManager.getRootLogger();

    private ArbolAritmetico modelo;
    private PanelArbol panel;

    public FrameArbol(){
        init();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void init() {
        setSize(800, 400);

        JMenuBar bar = new JMenuBar();

        JMenu menu = new JMenu("Expresiones");
        JMenu mnuHelp = new JMenu("Help");

        JMenuItem item1 = new JMenuItem("Ingresar Nueva Expresion");
        item1.addActionListener(e -> {
            try {
                String expresión = JOptionPane.showInputDialog(null, "Ingrese la expresion");
                if(expresión == null)
                    return;
                modelo = new ArbolAritmetico(expresión);
                modelo.addObserver(panel);
                panel.setModelo(modelo);
                modelo.cambioOk();
                logger.debug("Usuario ingresa nueva expresión");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        JMenuItem item2 = new JMenuItem("Exit");
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resp = JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea cerrar mi increible app?", "Cerrar app", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    logger.debug("Usuario sale de la ventana y finaliza app");
                    System.exit(0);
                }
            }
        });
        mnuHelp.add(item2);


        panel = new PanelArbol(modelo);
        JScrollPane scroller = new JScrollPane(panel);
        menu.add(item1);
        bar.add(menu);
        bar.add(mnuHelp);
        this.setJMenuBar(bar);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(scroller, BorderLayout.CENTER);

        this.pack();
    }


}
