/*
 * Autor: Jhosef Reyes - Carné: 9390-24-4816
 * Curso: Autómatas y Lenguajes Formales
 */
package automatas.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Carátula con los datos del curso; al presionar {@code ENTER} (en cualquier
 * componente con foco) avanza al menú principal.
 */
public class PanelPortada extends JPanel {

    public PanelPortada(Navegador nav) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 41, 59));

        JPanel caja = new JPanel();
        caja.setOpaque(false);
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));

        caja.add(etiqueta("Universidad Mariano Gálvez", 28, true));
        caja.add(etiqueta(DatosCurso.CURSO, 22, false));
        caja.add(Box.createVerticalStrut(20));
        caja.add(etiqueta("Sección: " + DatosCurso.SECCION, 18, false));
        caja.add(etiqueta("Carné: "   + DatosCurso.CARNE,  18, false));
        caja.add(etiqueta("Catedrático: " + DatosCurso.CATEDRATICO, 16, false));
        caja.add(Box.createVerticalStrut(40));
        caja.add(etiqueta("Presione ENTER para continuar", 16, false));
        add(caja);

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "entrar");
        getActionMap().put("entrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.irA(VentanaPrincipal.MENU);
            }
        });
    }

    private JLabel etiqueta(String texto, int tam, boolean negrita) {
        JLabel l = new JLabel(texto);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", negrita ? Font.BOLD : Font.PLAIN, tam));
        l.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        return l;
    }
}