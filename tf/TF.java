/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tf;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author altron01
 */
public class TF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 400);
        
        JTabbedPane Jtp = new JTabbedPane();;

        frame.add(Jtp);
        
        frame.setVisible(true);
        frame.pack();
        frame.validate();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
