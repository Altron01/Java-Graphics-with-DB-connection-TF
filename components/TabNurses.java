/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import apis.DBConnection;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javafx.util.Pair;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class TabNurses extends JPanel {
    
    DBConnection con;
    JTable tblModel;
    DefaultTableModel dataModel;
    ReportPanel pnlReport;
    
    public TabNurses() {
        
        con = DBConnection.getInstance();
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel pnlMainPanel = new JPanel();
        pnlMainPanel.setLayout(new BorderLayout());
        JPanel pnlOptionsPanel = new JPanel();
        pnlOptionsPanel.setLayout(new BoxLayout(pnlOptionsPanel, BoxLayout.Y_AXIS));
        
        ComboBoxOption cbRot = new ComboBoxOption("Rotation: ", con.getRotation());
        pnlOptionsPanel.add(cbRot);
        
        ComboBoxOption cbCodeServ = new ComboBoxOption("Service Code: ", con.getCodeService());
        pnlOptionsPanel.add(cbCodeServ);
        
        TextOption topName = new TextOption("Name: ");
        pnlOptionsPanel.add(topName);
        
        TextOption topSurname = new TextOption("Surname: ");
        pnlOptionsPanel.add(topSurname);
        
        TextOption topAddres = new TextOption("Addres: ");
        pnlOptionsPanel.add(topAddres);
        
        TextOption topPhone = new TextOption("Phone: ");
        pnlOptionsPanel.add(topPhone);
        
        TextOption topSa = new TextOption("Salary: ");
        pnlOptionsPanel.add(topSa);
        
        JButton btnSearch = new JButton();
        btnSearch.setText("Search");
        btnSearch.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
                Pair<Object[][], String[]> val = con.selectNurse(cbCodeServ.getText(), cbRot.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                
                pnlReport.setChart("caldas", con.getNurseByTurn(cbCodeServ.getText(), cbRot.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText()));
                
            }
        
        });
        pnlOptionsPanel.add(btnSearch);
        
        JButton btnInsert = new JButton();
        btnInsert.setText("Insert");
        btnInsert.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
                con.insertNurse(cbCodeServ.getText(), cbRot.getText(), topSa.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText());
                Pair<Object[][], String[]> val = con.selectNurse(cbCodeServ.getText(), cbRot.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                pnlReport.setChart("Caldas", con.getNurseByTurn("", "", "", "", "", ""));
                
            }
        
        });
        pnlOptionsPanel.add(btnInsert);
        
        JButton btnUpdate = new JButton();
        btnUpdate.setText("Update");
        btnUpdate.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblModel.getSelectedRow();
                String numero = (String)tblModel.getValueAt(row, 0);
                con.updateNurse(numero, cbCodeServ.getText(), cbRot.getText(), topSa.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText());
                Pair<Object[][], String[]> val = con.selectNurse(cbCodeServ.getText(), cbRot.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                
                pnlReport.setChart("Caldas", con.getNurseByTurn("", "", "", "", "", ""));
            }
        
        });
        pnlOptionsPanel.add(btnUpdate);
        
        JButton btnDelete = new JButton();
        btnDelete.setText("Delete");
        btnDelete.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
                int row = tblModel.getSelectedRow();
                String numero = (String)tblModel.getValueAt(row, 0);
                con.deleteNurse(numero);
                Pair<Object[][], String[]> val = con.selectNurse(cbCodeServ.getText(), cbRot.getText(), topName.getText(), topSurname.getText(), topAddres.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                
                pnlReport.setChart("Caldas", con.getNurseByTurn("", "", "", "", "", ""));
                
            }
            
        });
        pnlOptionsPanel.add(btnDelete);
        
        pnlMainPanel.add(pnlOptionsPanel, "West");
        
        tblModel = new JTable();
        dataModel = (DefaultTableModel) (tblModel.getModel());
        tblModel.setFillsViewportHeight(true);
        JScrollPane jspData = new JScrollPane(tblModel);
        
        pnlMainPanel.add(jspData, "East");
        
        this.add(pnlMainPanel);
        
        
        JScrollPane pnlReportPanel = new JScrollPane();
        pnlReport = new ReportPanel();
        pnlReport.setChart("Amount of Nurses per turn", con.getNurseByTurn("", "", "", "", "", ""));
        pnlReportPanel.setViewportView(pnlReport);
        this.add(pnlReportPanel);
    }
    
}
