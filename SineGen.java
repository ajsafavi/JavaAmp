import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Write a description of class SineGen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SineGen extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private int t;
    private double amp;
    private double freq;
    private JPanel sinePanel;
    private JSlider slider;
    /**
     * Constructor for objects of class SineGen
     */
    public SineGen(JPanel gui)
    {
        t = 0;
        amp = 100;
        sinePanel = new JPanel();
        sinePanel.setSize(100,500);
        sinePanel.setBorder(BorderFactory.createTitledBorder("Sine Waver"));
        
        freq = 500;
        slider = new JSlider(JSlider.VERTICAL, 20, 4000, 500);
        slider.setMinorTickSpacing(100);
        slider.setMajorTickSpacing(500);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(500));
        slider.setBorder(BorderFactory.createTitledBorder("Frequency"));
        slider.addChangeListener(this);
        sinePanel.add(slider);
        
        gui.add(sinePanel);
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public double[] process(double[] data)
    {
        for(int i = 0; i < data.length; i++)
        {
            double newData = data[i];
            double add = amp * Math.sin(1.0/44100.0 * (double)t * freq * 2 * Math.PI);
            newData = add;// addSound(newData, amp * Math.sin(1.0/44100.0 * (double)t * freq * 2 * Math.PI));
           
            data[i] = newData;
            t++;
        }
        return data;
    }
    
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) 
        {
            freq = (double) (source.getValue());
        }
    }
}
