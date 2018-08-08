/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tf;

import apis.CharterManager;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import components.*;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
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
        MainPanel pnlMain = new MainPanel();
        frame.add(pnlMain);
        
        frame.setVisible(true);
        frame.validate();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
