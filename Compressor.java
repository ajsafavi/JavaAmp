import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Write a description of class Compressor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Compressor extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private double threshold;
    private double ratio;
    private JPanel compFrame;
    private JSlider thresholdSlider;
    private JSlider ratioSlider;
    private double sliderMultiplier;

    /**
     * Constructor for objects of class Compressor
     */
    public Compressor(JPanel gui)
    {
        threshold = 50.0;
        ratio = .5;
        
        compFrame = new JPanel();
        compFrame.setSize(100,500);
        compFrame.setBorder(BorderFactory.createTitledBorder("Compressor"));
        
        thresholdSlider = new JSlider(JSlider.VERTICAL, 0, 128, (int) threshold);
        thresholdSlider.setMinorTickSpacing(1);
        thresholdSlider.setMajorTickSpacing(5);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);
        thresholdSlider.setLabelTable(thresholdSlider.createStandardLabels(16));
        thresholdSlider.setBorder(BorderFactory.createTitledBorder("Threshold"));
        thresholdSlider.addChangeListener(this);
        compFrame.add(thresholdSlider);
        
        ratioSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (ratio*100));
        ratioSlider.setMinorTickSpacing(1);
        ratioSlider.setMajorTickSpacing(5);
        ratioSlider.setPaintTicks(true);
        ratioSlider.setPaintLabels(true);
        ratioSlider.setLabelTable(ratioSlider.createStandardLabels(10));
        ratioSlider.setBorder(BorderFactory.createTitledBorder("Ratio"));
        ratioSlider.addChangeListener(this);
        compFrame.add(ratioSlider);
        
        gui.add(compFrame);
    }
    public Compressor(double thresh,double rat)
    {
        threshold = thresh;
        ratio = rat;
    }

   public double[] process(double[] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            
            double newData = data[i];
            double sign = Math.signum(newData);
            newData = Math.abs(newData);
            if(newData > threshold)
            {
                newData = (newData - threshold) * (ratio) + threshold;
            }
            newData = newData * sign;
            
            data[i] = newData;
        }
        return data;
    }
    public double process(double data)
    {
            
        double newData = data;
        double sign = Math.signum(newData);
        newData = Math.abs(newData);
        if(newData > threshold)
        {
            newData = (newData - threshold) * (ratio) + threshold;
        }
        newData = newData * sign;
        data = newData;
        return data;
    }
    
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        
        if (!source.getValueIsAdjusting()) 
        {
            if(source == thresholdSlider)
            {
                threshold = (double) (source.getValue());
            }
            if(source == ratioSlider)
            {
                ratio = ((double) (source.getValue())) / 100.0;
            }
        }
    }
}
