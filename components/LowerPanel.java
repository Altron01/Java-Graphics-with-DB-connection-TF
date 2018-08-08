/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author altron01
 */
public class LowerPanel extends JPanel {
    
    public JLabel txtMsg;
    
    public LowerPanel(){
        
        this.setLayout(new BorderLayout());
        txtMsg = new JLabel();
        txtMsg.setText("No Error Found");
        this.add(txtMsg, "West");
        Quit btnQuit = new Quit();
        this.add(btnQuit);
    }
}
