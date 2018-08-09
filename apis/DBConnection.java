package apis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

public class DBConnection {

    private static final DBConnection instance = new DBConnection();
    
    Connection conn;
    Statement stmt;
    List<String> tableNames;
    Map<String, String[]> dbMapping;
    
    public DBConnection(){
        try {
            //DB Init
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hospital?autoReconnect=true&useSSL=false", "root", "Altron194");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
            
            //get tableNames
            tableNames = new ArrayList<>();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
            tableNames.remove(tableNames.size()-1);
            
            //get tablesColumns
            dbMapping = new HashMap<>();
            for(String table : tableNames){
                rs = stmt.executeQuery("SELECT * FROM " + table);
                ResultSetMetaData rsmd = rs.getMetaData();
                String[] tableColumns = new String[rsmd.getColumnCount()];
                for (int i = 1; i <= tableColumns.length; i++){
                    tableColumns[i-1] = rsmd.getColumnName(i);
                }
                dbMapping.put(table, tableColumns);
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DBConnection getInstance(){
        return instance;
    }
    
    public Pair<Object[][], String[]> selectPatient(String dName, String dSurname, String name, String surname, String address, String phone, String m){
        try {
            String query = "SELECT mal.numero, mal.prenom AS Name, mal.nom AS Surname, mal.adresse, mal.tel, mal.mu"
                    + "tuelle, docs.docSurname, docs.docName, docs.specialite, h"
                    + "os.no_chambre FROM malade mal JOIN (SELECT so.no_malade, "
                    + "emp.specialite, emp.nom as docSurname, emp.prenom as docN"
                    + "ame FROM soigne so JOIN (SELECT em.numero, d.specialite, "
                    + "em.nom, em.prenom FROM docteur d JOIN employe em ON em.nu"
                    + "mero=d.numero WHERE em.nom like \"" + (dSurname.length() > 0 ? dSurname : "%")
                    + "\" AND em.prenom like \"" + (dName.length() > 0 ? dName : "%") + "\")"
                    + " emp ON so.no_docteur=emp.numero) docs ON mal.numero=docs"
                    + ".no_malade JOIN hospitalisation hos ON mal.numero=hos.no_malade "
                    + "WHERE mal.prenom like \"" + (name.length() > 0 ? name : "%")
                    + "\" AND mal.nom like \"" + (surname.length() > 0 ? surname : "%")
                    + "\" AND mal.adresse like \"" + (address.length() > 0 ? address : "%")
                    + "\" AND mal.tel like \"" + (phone.length() > 0 ? phone : "%")
                    + "\" AND mal.mutuelle like \"" + (m.length() > 0 ? m : "%") + "\"";
            
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount(); //number of column
            String columnName[] = new String[count];
            for (int i = 1; i <= count; i++)
            {
               columnName[i-1] = metaData.getColumnLabel(i);
            }
            
            rs.last();
            Object[][] data = new Object[rs.getRow()][];
            rs.beforeFirst();
            while (rs.next()) {
                Object values[] = new String[columnName.length];
                for(int i = 0; i < columnName.length; i++){
                    values[i] = rs.getObject(columnName[i]).toString();
                }
                data[rs.getRow()-1] = values;
            }
            return new Pair<>(data, columnName);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean insertPatient(String dName, String dSurname, String name, String surname, String address, String phone){
        try {
            String query = "SELECT (SELECT MAX(numero) FROM malade) + 1 as n";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int numero = rs.getInt("n");
            String num = Integer.toString(numero);
            query = "INSERT INTO malade (numero, nom, prenom, adresse, tel, mutuelle) VALUES (" + num + ", \"" + name + "\", \"" + surname + "\", \"" + address + "\", \"" + phone + "\", \"1\")";
            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            String serviceCode = "";
            String doctorCode = rs.getBigDecimal("directeur").toString();
            query = "SELECT (SELECT MAX(no_chambre) FROM chambre) + 1 as n";
            rs = stmt.executeQuery(query);
            rs.next();
            String roomNumber = Integer.toString(rs.getInt("n"));
            query = "INSERT INTO hospitalisation (no_malade, code_service, no_chambre, lit) VALUES (" + numero + ", \"" + serviceCode + "\", " + roomNumber + ", 1)";
            System.out.println(query);
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean updatePatient(String number, String dName, String dSurname, String name, String surname, String address, String phone){
        try {
            String query = "SELECT numero FROM employe WHERE nom like \"" + dSurname + "\" AND prenom like \"" + dName + "\"";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String dNumber = rs.getString("numero");
            query = "update malade set nom=\"" + name + "\", prenom=\"" + surname + "\", adresse=\"" + address + "\", tel=\"" + phone + "\", mutuelle=\"1\"" + " where numero=" + number;
            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deletePatient(String numero){
        try {
            String query = "DELETE FROM malade WHERE numero=" + numero;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            deleteAssignDoctor(numero);
            deleteHospData(numero);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteAssignDoctor(String numero){
        try {
            String query = "DELETE FROM soigne WHERE no_malade=" + numero;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteHospData(String numero){
        try {
            String query = "DELETE FROM hospitalisation WHERE no_malade=" + numero;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
   
    public Pair<Object[][], String[]> selectNurse(String serviceName, String rotation, String name, String surname, String address, String phone){
        try {
            String query = "SELECT * FROM infirmier a JOIN employe b ON a.numero" 
                    + " = b.numero JOIN service c ON a.code_service=c.code"
                    + " WHERE c.nom like \"" + (serviceName.length() > 0 ? serviceName : "%")
                    + "\" AND a.rotation like \"" + (rotation.length() > 0 ? rotation : "%")
                    + "\" AND b.prenom like \"" + (name.length() > 0 ? name : "%")
                    + "\" AND b.nom like \"" + (surname.length() > 0 ? surname : "%")
                    + "\" AND b.adresse like \"" + (address.length() > 0 ? address : "%")
                    + "\" AND b.tel like \"" + (phone.length() > 0 ? phone : "%") + "\"";
            
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount(); //number of column
            String columnName[] = new String[count];
            for (int i = 1; i <= count; i++)
            {
               columnName[i-1] = metaData.getColumnLabel(i);
            }
            
            rs.last();
            Object[][] data = new Object[rs.getRow()][];
            rs.beforeFirst();
            while (rs.next()) {
                Object values[] = new String[columnName.length];
                for(int i = 0; i < columnName.length; i++){
                    values[i] = rs.getObject(columnName[i]).toString();
                }
                data[rs.getRow()-1] = values;
            }
            return new Pair<>(data, columnName);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean insertNurse(String serviceName, String rotation, String salary, String name, String surname, String address, String phone){
        try {
            String query = "SELECT (SELECT MAX(numero) FROM employe) + 1 as n";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int numero = rs.getInt("n");
            String num = Integer.toString(numero);
            insertEmploye(num, name, surname, address, phone);
            query = "SELECT (SELECT MAX(numero) FROM employe) + 1 as n";
            System.out.println(query);
            rs = stmt.executeQuery(query);
            rs.next();
            query = "SELECT code FROM service WHERE nom=\"" + serviceName + "\"";
            System.out.println(query);
            rs = stmt.executeQuery(query);
            rs.next();
            query = "INSERT INTO infirmier (numero, code_service, rotation, salaire) VALUES (" + numero + ", \"" + rs.getString("code") + "\", \"" + rotation + "\", " + salary + ")";
            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean updateNurse(String number, String serviceName, String rotation, String salary, String name, String surname, String address, String phone){
        try {
            
            String query = "SELECT code FROM service WHERE nom=\"" + serviceName + "\"";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String code = rs.getString("code");
            query = "update infirmier set code_service=\"" + code + "\", rotation=\"" + rotation + "\", salaire=" + salary + " where numero=" + number;
            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            query = "update employe set nom=\"" + name + "\", prenom=\"" + surname + "\", adresse=\"" + address + "\", tel=\"" + phone + "\" where numero=" + number;
            System.out.println(query);
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteNurse(String numero){
        try {
            String query = "DELETE FROM infirmier WHERE numero=" + numero;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            deleteEmploye(numero);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Pair<String, Integer>[] getNurseByTurn(String serviceName, String rotation, String name, String surname, String address, String phone){
            
        try {
            String query = "SELECT COUNT(*) as am, d.rotation FROM (SELECT rotation FROM infirmier a JOIN employe b ON a.numero" 
                    + " = b.numero JOIN service c ON a.code_service=c.code"
                    + " WHERE c.nom like \"" + (serviceName.length() > 0 ? serviceName : "%")
                    + "\" AND a.rotation like \"" + (rotation.length() > 0 ? rotation : "%")
                    + "\" AND b.nom like \"" + (name.length() > 0 ? name : "%")
                    + "\" AND b.prenom like \"" + (surname.length() > 0 ? surname : "%")
                    + "\" AND b.adresse like \"" + (address.length() > 0 ? address : "%")
                    + "\" AND b.tel like \"" + (phone.length() > 0 ? phone : "%") + "\") d GROUP BY d.rotation";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            Pair<String, Integer>[] resul = new Pair[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()-1] = new Pair<>(rs.getString("rotation"), rs.getInt("am"));
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean insertHospitalisation(){
        try {
            String query = "DELETE FROM ";
            PreparedStatement ps = conn.prepareStatement(query);/*
            if(code instanceof String)
                ps.setString(1, (String)code);
            if(code instanceof BigDecimal)
                ps.setBigDecimal(1, (BigDecimal)code);
            ps.executeUpdate();*/
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean insertEmploye(String numero, String name, String surname, String address, String phone){
        try {
            String query = "INSERT INTO employe (numero, nom, prenom, adresse, tel) VALUES (" + numero + ", \"" + surname
                    + "\", \"" + name + "\", \"" + address + "\", \"" + phone + "\")";
            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteEmploye(String numero){
        try {
            String query = "DELETE FROM employe WHERE numero=" + numero;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Pair<Object[][], String[]> selectDoctor(String specialty, String name, String surname, String address, String phone){
        try {
            
            String query = "SELECT * FROM docteur a JOIN employe b ON a.numero "
                    +"= b.numero WHERE specialite like \"" + (specialty.length() > 0 ? specialty : "%")
                    +"\" AND prenom like \"" + (name.length() > 0 ? name : "%")
                    +"\" AND nom like \"" + (surname.length() > 0 ? surname : "%")
                    +"\" AND adresse like \"" + (address.length() > 0 ? address : "%")
                    +"\" AND tel like \"" + (phone.length() > 0 ? phone : "%") + "\"";
            /*String query = "SELECT * FROM docteur a JOIN employe b ON a.numero ="
                    + "b.numero WHERE specialite like ? AND nom like ? AND prenom "
                    + "like ? AND adresse like ? AND tel like ?";
            PreparedStatement pstmt = conn.prepareCall(query);
            pstmt.setString(1, (specialty.length() > 0 ? specialty : "\"%\""));
            pstmt.setString(2, (name.length() > 0 ? name : "\"%\""));
            pstmt.setString(3, (surname.length() > 0 ? surname : "\"%\""));
            pstmt.setString(4, (address.length() > 0 ? address : "\"%\""));
            pstmt.setString(5, (phone.length() > 0 ? phone : "\"%\""));*/
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount(); //number of column
            String columnName[] = new String[count];
            for (int i = 1; i <= count; i++)
            {
               columnName[i-1] = metaData.getColumnLabel(i);
            }
            
            rs.last();
            Object[][] data = new Object[rs.getRow()][];
            rs.beforeFirst();
            while (rs.next()) {
                Object values[] = new String[columnName.length];
                for(int i = 0; i < columnName.length; i++){
                    values[i] = rs.getObject(columnName[i]).toString();
                }
                data[rs.getRow()-1] = values;
            }
            return new Pair<>(data, columnName);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean updateDoctor(String number, String specialty, String name, String surname, String address, String phone){
        try {
            String query = "update docteur set specialite=\"" + specialty +"\" where numero=" + number;
            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            query = "update employe set nom=\"" + name + "\", prenom=\"" + surname + "\", adresse=\"" + address + "\", tel=\"" + phone + "\" where numero=" + number;
            System.out.println(query);
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean insertDoctor(String specialty, String name, String surname, String address, String phone){
        try {
            String query = "SELECT (SELECT MAX(numero) FROM employe) + 1 as n";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            int numero = rs.getInt("n");
            insertEmploye(Integer.toString(numero), name, surname, address, phone);
            query = "INSERT INTO docteur (numero, specialite) VALUES (" + numero + ", \"" + specialty + "\")";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteDoctor(String numero){
        try {
            String query = "DELETE FROM docteur WHERE numero=" + numero;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            deleteEmploye(numero);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public String[] getDoctorSpecialty(){
        try {
            String query = "SELECT DISTINCT specialite FROM docteur";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()+1];
            resul[0] = "";
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()] = rs.getString("specialite");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }    
    
    public String[] getDoctorFullName(){
        try {
            String query = "SELECT DISTINCT prenom, nom FROM docteur a JOIN employe b ON a.numero=b.numero";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()+1];
            resul[0] = "";
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()] = rs.getString("prenom") + " " + rs.getString("nom");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public String[] getRotation(){
        try {
            String query = "SELECT DISTINCT rotation FROM infirmier";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()+1];
            resul[0] = "";
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()] = rs.getString("rotation");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public String[] getCodeService(){
        try {
            String query = "SELECT DISTINCT nom FROM service";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()+1];
            resul[0] = "";
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()] = rs.getString("nom");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Pair<String, Integer>[] getDoctorBySpecialty(String specialty, String name, String surname, String address, String phone){
            
        try {
            //String query = x"SELECT COUNT(*) AS am, a.specialite FROM docteur" + " a GROUP BY a.specialite";
            String query = "SELECT COUNT(*) AS am, c.specialite FROM (SELECT a.specialite FROM docteur a JOIN employe b ON a.numero "
                    +"= b.numero WHERE specialite like \"" + (specialty.length() > 0 ? specialty : "%")
                    +"\" AND prenom like \"" + (name.length() > 0 ? name : "%")
                    +"\" AND nom like \"" + (surname.length() > 0 ? surname : "%")
                    +"\" AND adresse like \"" + (address.length() > 0 ? address : "%")
                    +"\" AND tel like \"" + (phone.length() > 0 ? phone : "%") + "\") c GROUP BY c.specialite";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            Pair<String, Integer>[] resul = new Pair[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()-1] = new Pair<>(rs.getString("specialite"), rs.getInt("am"));
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
      
    public String[] getDoctorSpecialtyByName(String name, String surname){
        try {
            String[] resul;
            String query = "SELECT DISTINCT specialite FROM employe a JOIN docteur b ON a.numero=b.numero WHERE prenom like \"" + (name.length() > 0 ? name : "%") + "\" AND nom like \"" + (surname.length() > 0 ? surname : "%") + "\"";
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            resul = new String[rs.getRow()];
            rs.beforeFirst();
            while(rs.next()){
                resul[rs.getRow()-1] = rs.getString("specialite");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public double[] DocByPerson(){
        
        try {
            double[] resul;
            String query = "SELECT COUNT(*) FROM (SELECT COUNT(*) AS DocxE FROM malade a JOIN soigne b ON a.numero=b.no_malade GROUP BY numero) c GROUP BY c.DocxE";
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            resul = new double[rs.getRow()];
            rs.beforeFirst();
            while(rs.next()){
                resul[rs.getRow()-1] = (double)rs.getInt("COUNT(*)");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }   
}
