/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import apis.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javafx.util.Pair;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author altron01
 */
public class QueryTab extends JPanel {
    
    DBConnection con;
    DefaultTableModel tblModel;
    
    public QueryTab() {
        con = DBConnection.getInstance();
        
        this.setLayout(new BorderLayout());
        
        JTable tblData = new JTable();
        tblModel = (DefaultTableModel) tblData.getModel();
        JScrollPane scPane = new JScrollPane(tblData);
        this.add("East", scPane);
        
        JPanel pnlQueryData = new JPanel();
        pnlQueryData.setLayout(new BoxLayout(pnlQueryData, BoxLayout.Y_AXIS));
        JComboBox cbTables = new JComboBox();
        cbTables.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pnlQueryData.add(cbTables);
        JButton btnQuery = new JButton();
        btnQuery.setText("Search");
        btnQuery.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Pair<Object[][], String[]> val = con.select((String) cbTables.getSelectedItem());
                //tblModel.setDataVector(val.getKey(), val.getValue());
                //tblModel.fireTableDataChanged();
            }
            
        });
        pnlQueryData.add(btnQuery);
        this.add("West", pnlQueryData);
    }
}
