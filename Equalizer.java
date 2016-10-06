import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Write a description of class Equalizer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Equalizer extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private BandPass low, mid, high;
    private int ll, ml, hl;
    private JPanel panel;
    private JSlider highSlider, lowSlider , midSlider;
    
    
    /**
     * Constructor for objects of class Equalizer
     */
    public Equalizer(JPanel gui)
    {
        ll = 100;
        ml = 100;
        hl = 100;
        low = new BandPass(30.0,100.0);
        mid = new BandPass(100.0,1000.0);
        high = new BandPass(1000.0,20000.0);
        
        panel = new JPanel();
        panel.setSize(100,500);
        panel.setBorder(BorderFactory.createTitledBorder("Equalizer"));
        
        lowSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int)ll);
        lowSlider.setMinorTickSpacing(10);
        lowSlider.setMajorTickSpacing(50);
        lowSlider.setPaintTicks(true);
        lowSlider.setPaintLabels(true);
        lowSlider.setLabelTable(lowSlider.createStandardLabels(10));
        lowSlider.setBorder(BorderFactory.createTitledBorder("Low"));
        lowSlider.addChangeListener(this);
        panel.add(lowSlider);
        
        midSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int)hl);
        midSlider.setMinorTickSpacing(10);
        midSlider.setMajorTickSpacing(50);
        midSlider.setPaintTicks(true);
        midSlider.setPaintLabels(true);
        midSlider.setLabelTable(midSlider.createStandardLabels(10));
        midSlider.setBorder(BorderFactory.createTitledBorder("mid"));
        midSlider.addChangeListener(this);
        panel.add(midSlider);
        
        highSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int)hl);
        highSlider.setMinorTickSpacing(10);
        highSlider.setMajorTickSpacing(50);
        highSlider.setPaintTicks(true);
        highSlider.setPaintLabels(true);
        highSlider.setLabelTable(highSlider.createStandardLabels(10));
        highSlider.setBorder(BorderFactory.createTitledBorder("High"));
        highSlider.addChangeListener(this);
        panel.add(highSlider);
        
        gui.add(panel);
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public double[] process(double[] data)
    {
        double third = 1.0/3.0;
        for(int i = 0; i < data.length; i++)
        {
            double ld = low.process(data[i]) * third * (double)ll/100.0;
            double md = mid.process(data[i]) * third * (double)ml/100.0;
            double hd = high.process(data[i]) * third * (double)hl/100.0;
            data[i] = ld + md + hd;
        }

        return data;
    }
    
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) 
        {
            if(source == lowSlider)
            {
                ll = source.getValue();
            }
            if(source == midSlider)
            {
                ml = source.getValue();
            }
            if(source == highSlider)
            {
                hl = source.getValue();
            }
        }
    }
}
