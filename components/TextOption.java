/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author altron01
 */
public class TextOption extends JPanel {
    
    JTextField cb;
    
    public TextOption(String title){
        this.setLayout(new FlowLayout());
        JLabel txt =new JLabel();
        txt.setText(title);
        this.add(txt);
        cb = new JTextField();
        cb.setColumns(10);
        this.add(cb);
    }
    
    public String getText(){
        return cb.getText();
    }
}
