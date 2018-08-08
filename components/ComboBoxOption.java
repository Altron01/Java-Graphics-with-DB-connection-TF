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

/**
 *
 * @author altron01
 */
public class ComboBoxOption extends JPanel{
    
    public ComboBoxOption(String title, String[] data){
        this.setLayout(new FlowLayout());
        JLabel txt =new JLabel();
        txt.setText(title);
        this.add(txt);
        JComboBox cb = new JComboBox(data);
        this.add(cb);
    }
    
}
