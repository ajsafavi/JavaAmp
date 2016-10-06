import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Write a description of class BandPass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BandPass extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private double high, low;
    private RCHighPass hp;
    private RCLowPass lp;
    private JPanel panel;
    private JSlider highSlider, lowSlider;
    /**
     * Constructor for objects of class BandPass
     */
    public BandPass(double fl, double fh)
    {
        high = fh;
        low = fl;
        
        hp = new RCHighPass(low, 44100.0);
        lp = new RCLowPass(high, 44100.0);
    }
    
    public BandPass(JPanel gui)
    {
        high = 4000;
        low = 1000;
        
        hp = new RCHighPass(low, 44100.0);
        lp = new RCLowPass(high, 44100.0);
        
        panel = new JPanel();
        panel.setSize(100,500);
        panel.setBorder(BorderFactory.createTitledBorder("Band Pass"));
        
        lowSlider = new JSlider(JSlider.VERTICAL, 0, 2000, (int)low);
        lowSlider.setMinorTickSpacing(500);
        lowSlider.setMajorTickSpacing(1000);
        lowSlider.setPaintTicks(true);
        lowSlider.setPaintLabels(true);
        lowSlider.setLabelTable(lowSlider.createStandardLabels(1000));
        lowSlider.setBorder(BorderFactory.createTitledBorder("Low"));
        lowSlider.addChangeListener(this);
        panel.add(lowSlider);
        
        highSlider = new JSlider(JSlider.VERTICAL, 0, 15000, (int)high);
        highSlider.setMinorTickSpacing(500);
        highSlider.setMajorTickSpacing(1000);
        highSlider.setPaintTicks(true);
        highSlider.setPaintLabels(true);
        highSlider.setLabelTable(highSlider.createStandardLabels(1000));
        highSlider.setBorder(BorderFactory.createTitledBorder("High"));
        highSlider.addChangeListener(this);
        panel.add(highSlider);
        
        gui.add(panel);
    }

    public double[] process(double[] data)
    {
        int len = data.length;
        //prev = (double) data[0];
        for(int i =  0; i < len; i++)
        {
            double lowSound = lp.process(data[i]);
            double highSound = hp.process(data[i]);
            data[i] = highSound*.5 + lowSound*.5;
        }
        return data;
    }
    public double process(double data)
    {
        double lowSound = lp.process(data);
        double highSound = hp.process(data);
        data = highSound*.5 + lowSound*.5;
        return data;
    }
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) 
        {
            if(source == highSlider)
            {
                high = (double) (source.getValue());
                lp = new RCLowPass(high, 44000.0);
            }
            if(source == lowSlider)
            {
                low = (double) (source.getValue());
                hp = new RCHighPass(low, 44000.0);
                
            }
            //System.out.println(t1);
        }
    }
}
