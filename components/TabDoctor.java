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
import java.awt.event.MouseListener;
import javafx.util.Pair;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author altron01
 */
public class TabDoctor extends JPanel {
    
    DBConnection con;
    JTable tblModel;
    DefaultTableModel dataModel;
    ReportPanel pnlReport;
    JPanel self;
    
    public TabDoctor(){
        
        self = this;
        con = DBConnection.getInstance();
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel pnlMainPanel = new JPanel();
        pnlMainPanel.setLayout(new BorderLayout());
        JPanel pnlOptionsPanel = new JPanel();
        pnlOptionsPanel.setLayout(new BoxLayout(pnlOptionsPanel, BoxLayout.Y_AXIS));
        
        ComboBoxOption cbEsp = new ComboBoxOption("Especialty: ", con.getDoctorSpecialty());
        pnlOptionsPanel.add(cbEsp);
        
        TextOption topName = new TextOption("Name: ");
        pnlOptionsPanel.add(topName);
        
        TextOption topSurname = new TextOption("Surname: ");
        pnlOptionsPanel.add(topSurname);
        
        TextOption topAddress = new TextOption("Address: ");
        pnlOptionsPanel.add(topAddress);
        
        TextOption topPhone = new TextOption("Phone: ");
        pnlOptionsPanel.add(topPhone);
        
        JButton btnSearch = new JButton();
        btnSearch.setText("Search");
        btnSearch.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                Pair<Object[][], String[]> val = con.selectDoctor(cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                pnlReport.setChart("Doctors by Specialty", con.getDoctorBySpecialty(cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText()));
                self.revalidate();
            }
        
        });
        pnlOptionsPanel.add(btnSearch);
        JButton btnInsert = new JButton();
        btnInsert.setText("Insert");
        btnInsert.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                con.insertDoctor(cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText());
                Pair<Object[][], String[]> val = con.selectDoctor(cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                pnlReport.setChart("Doctors by Specialty", con.getDoctorBySpecialty("", "", "", "", ""));
                self.revalidate();
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
                con.updateDoctor(numero, cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText());
                Pair<Object[][], String[]> val = con.selectDoctor(cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                pnlReport.setChart("Doctors by Specialty", con.getDoctorBySpecialty("", "", "", "", ""));
                self.revalidate();
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
                con.deleteDoctor(numero);
                Pair<Object[][], String[]> val = con.selectDoctor(cbEsp.getText(), topName.getText(), topSurname.getText(), topAddress.getText(), topPhone.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                pnlReport.setChart("Doctors by Specialty", con.getDoctorBySpecialty("", "", "", "", ""));
                self.revalidate();
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
        pnlReport.setChart("Doctors by Specialty", con.getDoctorBySpecialty("", "", "", "", ""));
        pnlReportPanel.setViewportView(pnlReport);
        this.add(pnlReportPanel);
    }
}
