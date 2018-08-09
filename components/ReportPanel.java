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
    
    CharterManager manager;
    ChartPanel chart;

    public ReportPanel(){
        manager = new CharterManager();
    }
    
    public void setChart(double[] data, String xTitle, String yTitle, String title){
        if(chart == null){ 
            chart = new ChartPanel(manager.generateHistogram(data, xTitle, yTitle, title));
            this.add(chart);
        }
        chart.setChart(manager.generateHistogram(data, xTitle, yTitle, title));
    }
    
    public void setChart(String title, Pair<String, Integer>[] data){
        if(chart == null){ 
            chart = new ChartPanel(manager.generatePieChart(title, data));
            this.add(chart);
        }
        chart.setChart(manager.generatePieChart(title, data));
    }
}
