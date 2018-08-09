/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apis;

import java.awt.Color;
import javafx.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;



/**
 *
 * @author altron01
 */
public class CharterManager {
    
    public CharterManager(){}
    
    public JFreeChart generatePieChart(String title, Pair<String, Integer>[] data){
        
      DefaultPieDataset dataset = new DefaultPieDataset( );
      for(int i = 0; i < data.length; i++){
          dataset.setValue(data[i].getKey(), data[i].getValue());
      }
      JFreeChart chart = ChartFactory.createPieChart(      
         title,   // chart title 
         dataset,          // data    
         true,             // include legend   
         true, 
         false);

      return chart;
    }
    
    public JFreeChart generateHistogram(double[] data, String xTitle, String yTitle, String title){

        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i = 0; i < data.length; i++){
            dataset.addValue(data[i], Integer.toString(i), Integer.toString(i));
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            title,         // chart title
            xTitle,               // domain axis label
            yTitle,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );
        /*
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("Hist",data,20);
        String plotTitle = title;
        String xAxis = xTitle;
        String yAxis = yTitle;
        PlotOrientation orientation = PlotOrientation.VERTICAL;

        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xAxis, yAxis,
                dataset, orientation, show, toolTips, urls);
        */
        chart.setBackgroundPaint(Color.white);

        return chart;
    }
    
}
