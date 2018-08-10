/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tf;

import apis.CharterManager;
import apis.DBConnection;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import components.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(800, 400);
        LoginPanel lPanel = new LoginPanel();
        frame.add(lPanel);
        LowerPanel pnlLower = new LowerPanel();
        frame.add(pnlLower);
        lPanel.btnLog.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!lPanel.testData()){
                    try {
                        pnlLower.setError("Incorrect User!!!!");
                        frame.revalidate();
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TF.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    frame.getContentPane().removeAll();
                    MainPanel pnlMain = new MainPanel();
                    frame.add(pnlMain);
                    frame.revalidate();
                    frame.pack();
                }
            }
        });
        frame.setVisible(true);
        frame.validate();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
