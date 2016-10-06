import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Write a description of class Gain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Gain extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private double gain;
    private double maxGain, minGain;
    private JPanel gainPanel;
    private JSlider slider;
    private double sliderMultiplier;

    /**
     * Constructor for objects of class Gain
     */
    public Gain(JPanel gui)
    {
        sliderMultiplier = 10.0;
        minGain = 1.0;
        maxGain = 10.0;
        gain = 1.0;
        int sliderMin = (int) (minGain * sliderMultiplier);
        int sliderMax = (int) (maxGain * sliderMultiplier);
        int sliderVal = (int) (gain * sliderMultiplier);
        
        gainPanel = new JPanel();
        gainPanel.setSize(100,500);
        gainPanel.setBorder(BorderFactory.createTitledBorder("Gain"));
        
        slider = new JSlider(JSlider.VERTICAL, sliderMin, sliderMax, sliderVal);
        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(10));
        slider.setBorder(BorderFactory.createTitledBorder("Gain Selection"));
        slider.addChangeListener(this);
        gainPanel.add(slider);
        
        gui.add(gainPanel);
    }
    

   public double[] process(double[] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            double newData = data[i] * gain;
            data[i] = newData;
        }
        return data;
    }
    
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) 
        {
            gain = (double) (source.getValue()/sliderMultiplier);
        }
    }
}
