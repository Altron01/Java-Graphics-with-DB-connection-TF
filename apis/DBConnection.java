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
    
    public Pair<Object[][], String[]> selectDocteur(){
        try {
            String query = "SELECT * FROM docteur a JOIN employe b ON a.numero = b.numero";
            
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
    public <T> boolean update(String table, Pair<String, T>[] updateValues, String param, String code){
        try {
            String query = "UPDATE " + table + " SET ";
            for(int i = 0; i < updateValues.length - 1; i++){
                query += updateValues[i].getKey() + "= ?, ";
            }
            query += updateValues[updateValues.length - 1].getKey() + "= ? WHERE " + param + "=" + code;
            PreparedStatement ps = conn.prepareStatement(query);
            for(int i = 0; i < updateValues.length; i++){
                if(updateValues[i].getValue() instanceof String)
                    ps.setString(i + 1, (String)updateValues[i].getValue());
                if(updateValues[i].getValue() instanceof BigDecimal)
                    ps.setBigDecimal(i + 1, (BigDecimal)updateValues[i].getValue());
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public Pair<Object[][], String[]> selectMalade(){
        try {
            String query = "SELECT a.numero, a.nom, a.prenom, a.adresse, a.tel, a.mutuelle, "
                    + "b.DocName, b.DocSurName, b.DocSpecialty, f.lit, f.ServiceNom, f.ServiceBatiment, "
                    + "f.NoChambre, f.nb_lits, f.NurseName, f.NurseSurName FROM malade a JOIN (SELECT e.nom "
                    + "AS \"DocName\", e.prenom AS \"DocSurName\", d.specialite AS \"DocSpecialty\", c.no_malade "
                    + "AS \"no_malade\" FROM soigne c JOIN docteur d ON c.no_docteur = d.numero JOIN employe e ON d.numero = "
                    + "e.numero) b ON a.numero = b.no_malade JOIN (SELECT g.no_malade, g.lit, h.nom AS \"ServiceNom\", h.batiment "
                    + "AS \"ServiceBatiment\", i.no_chambre AS \"NoChambre\", i.nb_lits, k.nom AS \"NurseName\", k.prenom " 
                    + "AS \"NurseSurName\" FROM hospitalisation g JOIN service h ON g.code_service = h.code JOIN chambre i ON "
                    + "g.no_chambre = i.no_chambre JOIN infirmier j ON i.surveillant = j.numero JOIN employe k ON j.numero = k.numero) f ON a.numero = f.no_malade";
            
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
    
    public Pair<Object[][], String[]> selectInfirmier(){
        try {
            String query = "SELECT * FROM infirmier a JOIN employe b ON a.numero = b.numero JOIN service c ON b.code_service = c.code";
            
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
    
    public Pair<Object[][], String[]> select(String tableName, String[] columns, String[] params){
        try {
            
            String query = "SELECT ";
            String[] columnsNames = dbMapping.get(tableName);
            for(int i = 0; i < columnsNames.length - 1; i++){
                query += columnsNames[i] + ", ";
            }
            query += columnsNames[columnsNames.length - 1] + " FROM " + tableName + " WHERE";
            for(int i = 0; i < params.length; i++){
                query += " " + columns[i] + "=" + params[i];
            }
            query += ";";
            
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            
            rs.last();
            Object[][] data = new Object[rs.getRow()][];
            rs.beforeFirst();
            while (rs.next()) {
                Object values[] = new String[columnsNames.length];
                for(int i = 0; i < columnsNames.length; i++){
                    values[i] = rs.getObject(columnsNames[i]).toString();
                }
                data[rs.getRow()-1] = values;
            }
            return new Pair<>(data, columnsNames);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Pair<Object[][], String[]> customeSelect(String query){
    
        try {
            
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount(); //number of column
            String[] columnName = new String[count];
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
    
    public <T> boolean delete(String table, String param, T code){
        try {
            String query = "DELETE FROM " + table + " WHERE " + param + "=" + code;
            PreparedStatement ps = conn.prepareStatement(query);
            if(code instanceof String)
                ps.setString(1, (String)code);
            if(code instanceof BigDecimal)
                ps.setBigDecimal(1, (BigDecimal)code);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    
    
    
    
    public boolean insertEmploye(){
        try {
            String query = "DELETE FROM ";
            PreparedStatement ps = conn.prepareStatement(query);/*
            //if(code instanceof String)
            //    ps.setString(1, (String)code);
            //if(code instanceof BigDecimal)
            //    ps.setBigDecimal(1, (BigDecimal)code);
            //ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean insertDocteur(){
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
    
    public boolean insertInfimier(){
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
    
    public boolean insertMalade(){
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
    
    
    
    public String[] getDocteurEspetialty(){
        try {
            String query = "SELECT DISTINCT specialite FROM docteur";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()-1] = rs.getString("specialite");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String[] getNurseCode() {
        
        try {
            String query = "SELECT DISTINCT code_service FROM infirmier";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()-1] = rs.getString("code_service");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Pair<Object[][], String[]> selectNurse() {
        
        try {
            String query = "SELECT * FROM infirmier a JOIN employe b ON a.numero = b.numero";
            
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

    public String[] getNurseRotation() {
        
        try {
            String query = "SELECT DISTINCT rotation FROM infirmier";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()-1] = rs.getString("rotation");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String[] getPatientDisease() {
        
        try {
            String query = "SELECT DISTINCT mutuelle FROM malade";
            
            ResultSet rs = stmt.executeQuery(query);
            
            rs.last();
            String[] resul = new String[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                resul[rs.getRow()-1] = rs.getString("mutuelle");
            }
            return resul;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Pair<Object[][], String[]> selectPatient() {
        
        
        try {
            String query = "SELECT * FROM malade";
            
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
}