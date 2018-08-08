/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import apis.CharterManager;
import javax.swing.*;
import java.awt.*;
import javafx.util.Pair;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author altron01
 */
public class ReportPanel extends JPanel {
    
    
    public ReportPanel(double[] data){
        CharterManager manager = new CharterManager();
        this.add(new ChartPanel(manager.generateHistogram(data)));
    }
    
    public ReportPanel(String title, Pair<String, Integer>[] data){
        CharterManager manager = new CharterManager();
        this.add(new ChartPanel(manager.generatePieChart(title, data)));
    }
}
