/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import apis.DBConnection;
import java.awt.BorderLayout;
import javafx.util.Pair;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author altron01
 */
public class TabPanel extends JPanel {
    
    DBConnection con;
    
    public TabPanel(){
        
        con = DBConnection.getInstance();
        
        this.setLayout(new BorderLayout());
        JPanel pnlMainPanel = new JPanel();
        pnlMainPanel.setLayout(new BorderLayout());
        JPanel pnlOptionsPanel = new JPanel();
        pnlOptionsPanel.setLayout(new BoxLayout(pnlOptionsPanel, BoxLayout.Y_AXIS));
        
        pnlOptionsPanel.add(new ComboBoxOption("Especialty: ", con.getDocteurEspetialty()));
        pnlOptionsPanel.add(new TextOption("Name: "));
        pnlOptionsPanel.add(new TextOption("SurName: "));
        
        pnlMainPanel.add(pnlOptionsPanel, "West");
        
        
        Pair<Object[][], String[]> val = con.selectDocteur();
        JTable tblModel = new JTable(val.getKey(), val.getValue());
        tblModel.setFillsViewportHeight(true);
        JScrollPane jspData = new JScrollPane(tblModel);
        
        pnlMainPanel.add(jspData, "East");
        
        this.add(pnlMainPanel, "North");
        
        //ReportPanel pnlReport = new ReportPanel("Caldas");
    }
    
}
