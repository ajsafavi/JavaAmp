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
public class TubeDistortion extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private double t1, t;
    private JPanel gainPanel;
    private JSlider tslider, t1slider;

    /**
     * Constructor for objects of class Gain
     */
    public TubeDistortion(JPanel gui)
    {
        t1 = 128;
        t = 128;
        
        gainPanel = new JPanel();
        gainPanel.setSize(100,500);
        gainPanel.setBorder(BorderFactory.createTitledBorder("Tube Distortion"));
        
        tslider = new JSlider(JSlider.VERTICAL, 0, 128, (int)t);
        tslider.setMinorTickSpacing(8);
        tslider.setMajorTickSpacing(32);
        tslider.setPaintTicks(true);
        tslider.setPaintLabels(true);
        tslider.setLabelTable(tslider.createStandardLabels(10));
        tslider.setBorder(BorderFactory.createTitledBorder("hard thresh"));
        tslider.addChangeListener(this);
        gainPanel.add(tslider);
        
        t1slider = new JSlider(JSlider.VERTICAL, 0, 128, (int)t1);
        t1slider.setMinorTickSpacing(8);
        t1slider.setMajorTickSpacing(32);
        t1slider.setPaintTicks(true);
        t1slider.setPaintLabels(true);
        t1slider.setLabelTable(t1slider.createStandardLabels(10));
        t1slider.setBorder(BorderFactory.createTitledBorder("soft thresh"));
        t1slider.addChangeListener(this);
        gainPanel.add(t1slider);
        
        gui.add(gainPanel);
    }
    

   public double[] process(double[] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            double x = data[i];
            double sign = Math.signum(x);
            x = Math.abs(x);
            if(x > t1)
            {
                x = t1 + (x-t1)/(128 - t1) * (t - t1);
            }
            x = x * sign;
            
            data[i] = x;
        }
        return data;
    }
    
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) 
        {
            if(source == tslider)
            {
                t = (double) (source.getValue());
                int tint = (int)t;
                if(tint < t1slider.getValue())
                {
                    t1slider.setValue(tint);
                }
            }
            if(source == t1slider)
            {
                t1 = (double) (source.getValue());
                
            }
            //System.out.println(t1);
        }
    }
}
