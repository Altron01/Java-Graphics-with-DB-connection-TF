package components;

import apis.DBConnection;
import java.awt.BorderLayout;
import javafx.util.Pair;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TabPatients extends JPanel {
    
    DBConnection con;

    public TabPatients() {
        con = DBConnection.getInstance();
        
        this.setLayout(new BorderLayout());
        JPanel pnlMainPanel = new JPanel();
        pnlMainPanel.setLayout(new BorderLayout());
        JPanel pnlOptionsPanel = new JPanel();
        pnlOptionsPanel.setLayout(new BoxLayout(pnlOptionsPanel, BoxLayout.Y_AXIS));
        
        pnlOptionsPanel.add(new ComboBoxOption("Disease: ", con.getPatientDisease()));
        
        pnlOptionsPanel.add(new TextOption("Name: "));
        pnlOptionsPanel.add(new TextOption("SurName: "));
        pnlOptionsPanel.add(new TextOption("Addres: "));
        pnlOptionsPanel.add(new TextOption("Tlf: "));
        pnlOptionsPanel.add(new TextOption("Mutuelle: "));
        
        
        pnlMainPanel.add(pnlOptionsPanel, "West");
        pnlMainPanel.add(pnlOptionsPanel, "West");
        
        Pair<Object[][], String[]> val = con.selectPatient();
        
        JTable tblModel = new JTable(val.getKey(), val.getValue());
        tblModel.setFillsViewportHeight(true);
        
        JScrollPane jspData = new JScrollPane(tblModel);
        pnlMainPanel.add(jspData, "East");
        
        this.add(pnlMainPanel, "North");
    }
    
}