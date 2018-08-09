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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;



public class TabNurses extends JPanel {
    
    DBConnection con;
    
    public TabNurses() {
        
        
        
        con = DBConnection.getInstance();
        
        this.setLayout(new BorderLayout());
        JPanel pnlMainPanel = new JPanel();
        pnlMainPanel.setLayout(new BorderLayout());
        JPanel pnlOptionsPanel = new JPanel();
        pnlOptionsPanel.setLayout(new BoxLayout(pnlOptionsPanel, BoxLayout.Y_AXIS));
        /*
        pnlOptionsPanel.add(new ComboBoxOption("Code: ", con.getNurseCode()));
        pnlOptionsPanel.add(new ComboBoxOption("Rotation: ", con.getNurseRotation()));
        
        pnlOptionsPanel.add(new TextOption("Name: "));
        pnlOptionsPanel.add(new TextOption("SurName: "));
        pnlOptionsPanel.add(new TextOption("Adres: "));
        pnlOptionsPanel.add(new TextOption("Tlf: "));
        
        pnlMainPanel.add(pnlOptionsPanel, "West");
        pnlMainPanel.add(pnlOptionsPanel, "West");
        
        Pair<Object[][], String[]> val = con.selectNurse();
        
        JTable tblModel = new JTable(val.getKey(), val.getValue());
        tblModel.setFillsViewportHeight(true);
        
        JScrollPane jspData = new JScrollPane(tblModel);
        pnlMainPanel.add(jspData, "East");
        */
        this.add(pnlMainPanel, "North");
    }
    
}
