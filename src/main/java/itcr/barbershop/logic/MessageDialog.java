/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.logic;

/**
 *
 * @author Samantha
 */

//Idea de crear una clase para messageDialog obtenida de https://stackoverflow.com/questions/12214874/jdialog-modality-and-class-extending
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MessageDialog extends JDialog {
    public MessageDialog(String message) {
        super();
        setTitle("Message");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setModal(true);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        add(label);
    }
}

