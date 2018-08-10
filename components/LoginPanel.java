/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import apis.DBConnection;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author altron01
 */
public class LoginPanel extends JPanel {
    
    public JButton btnLog;
    TextOption user;
    TextOption password;
    DBConnection con;
    
    public LoginPanel(){
        con = DBConnection.getInstance();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        user = new TextOption("User: ");
        this.add(user);
        password = new TextOption("Password: ");
        this.add(password);
        JPanel low = new JPanel();
        low.setLayout(new BorderLayout());
        btnLog = new JButton("Login");
        low.add(btnLog, "Center");
        this.add(low);
    }
    
    public boolean testData(){
        return con.setLoginData(user.getText(), password.getText());
    }
    
}
