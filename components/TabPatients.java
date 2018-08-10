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
import static javax.swing.JComponent.TOOL_TIP_TEXT_KEY;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TabPatients extends JPanel {
    
    DBConnection con;
    JTable tblModel;
    DefaultTableModel dataModel;
    ReportPanel pnlReport;
    ComboBoxOption cbDFName;
    ComboBoxOption cbService;
    JPanel self;

    public TabPatients() {
        self = this;
        con = DBConnection.getInstance();
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel pnlMainPanel = new JPanel();
        pnlMainPanel.setLayout(new BorderLayout());
        JPanel pnlOptionsPanel = new JPanel();
        pnlOptionsPanel.setLayout(new BoxLayout(pnlOptionsPanel, BoxLayout.Y_AXIS));
        
        cbDFName = new ComboBoxOption("Doctor: ", con.getDoctorFullName());
        cbDFName.cb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] data = cbDFName.getText().split(" ");
                if(data.length > 1)
                    cbService.setData(con.getDoctorSpecialtyByName(data[0], data[1]));
                else
                    cbService.setData(con.getDoctorSpecialtyByName("", ""));
            }
        });
        pnlOptionsPanel.add(cbDFName);
        
        
        cbService = new ComboBoxOption("Service: ", con.getCodeService());
        pnlOptionsPanel.add(cbService);
        
        TextOption topName = new TextOption("Name: ");
        pnlOptionsPanel.add(topName);
        
        TextOption topSurName = new TextOption("SurName: ");
        pnlOptionsPanel.add(topSurName);
        
        TextOption topAddres = new TextOption("Addres: ");
        pnlOptionsPanel.add(topAddres);
        
        TextOption topPhone = new TextOption("Phone: ");
        pnlOptionsPanel.add(topPhone);
        
        TextOption topM = new TextOption("M: ");
        pnlOptionsPanel.add(topM);
        
        JButton btnSearch = new JButton();
        btnSearch.setText("Search");
        btnSearch.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] dFNames = cbDFName.getText().split(" ");
                Pair<Object[][], String[]> val;
                if(dFNames.length > 1)
                    val = con.selectPatient(dFNames[0], dFNames[1], topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                else    
                    val = con.selectPatient("", "", topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                self.revalidate();
                
                
            }
        
        });
        pnlOptionsPanel.add(btnSearch);
        
        JButton btnInsert = new JButton();
        btnInsert.setText("Insert");
        btnInsert.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
                String[] dFNames = cbDFName.getText().split(" ");
                Pair<Object[][], String[]> val;
                if(dFNames.length > 1){
                    con.insertPatient(dFNames[0], dFNames[1], topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText());
                    val = con.selectPatient(dFNames[0], dFNames[1], topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                }else    {
                    con.insertPatient("", "", topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText());
                    val = con.selectPatient("", "", topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                }
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
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
                String[] dFNames = cbDFName.getText().split(" ");
                Pair<Object[][], String[]> val;
                if(dFNames.length > 1){
                    con.updatePatient(numero, dFNames[0], dFNames[1], topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText());
                    val = con.selectPatient(dFNames[0], dFNames[1], topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                }else    {
                    con.updatePatient(numero, "", "", topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText());
                    val = con.selectPatient("", "", topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                }
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
                self.revalidate();
            }
        
        });
        pnlOptionsPanel.add(btnUpdate);
        
        JButton btnDelete = new JButton();
        btnDelete.setText("Delete");
        btnDelete.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                
                String[] dFNames = cbDFName.getText().split(" ");
                int row = tblModel.getSelectedRow();
                String numero = (String)tblModel.getValueAt(row, 0);
                con.deletePatient(numero);
                Pair<Object[][], String[]> val = con.selectPatient(dFNames[0], dFNames[1], topName.getText(), topSurName.getText(), topAddres.getText(), topPhone.getText(), topM.getText());
                dataModel.setDataVector(val.getKey(), val.getValue());
                dataModel.fireTableDataChanged();
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
        pnlReport.setChart(con.DocByPerson(), "Amount of Doctors", "Patients", "Doctors by Patient");
        pnlReportPanel.setViewportView(pnlReport);
        this.add(pnlReportPanel);
    }
    
}