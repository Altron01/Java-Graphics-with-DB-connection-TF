/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author altron01
 */
public class MainPanel extends JPanel{
    
    public MainPanel(){
        this.setLayout(new BorderLayout());
        
        JTabbedPane tbPanel = new JTabbedPane();
        
        TabPanel pnlDoctors = new TabPanel();
        tbPanel.addTab("Doctors", pnlDoctors);
        
        JPanel pnlNurses = new TabNurses();
        tbPanel.addTab("Nurses", pnlNurses);
        
        JPanel pnlPatients = new JPanel();
        tbPanel.addTab("Patients", pnlPatients);
        
        this.add(tbPanel, "North");
        
        LowerPanel pnlLowerPanel = new LowerPanel();
        this.add(pnlLowerPanel, "South");
    }
    
}
