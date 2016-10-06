import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
/**
 * Write a description of class Reverb here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Reverb extends AmpEffect implements ChangeListener
{
    // instance variables - replace the example below with your own
    private int delay1, delay2, delay3;
    private double value1, value2, value3;
    private double SAMPLE_RATE;
    private int  CACHE_SIZE;
    
    private double inValue1, inValue2, inValue3;
    private double reverbLevel;
    //private LowPassFilter lpf;
    
    private JSlider reverbSlider;
    private JPanel reverbFrame;
    private byte cache[];

    /**
     * Constructor for objects of class Reverb
     */
    public Reverb(JPanel gui)
    {
        SAMPLE_RATE = 44100.0;
        //lpf = new LowPassFilter(1000, SAMPLE_RATE);
        delay1 = 16 * 370;//37 ms delay
        delay2 = 16 * 1130;//113 ms delay
        delay3 = 16 * 2337;//337 ms delay
        CACHE_SIZE = 40000;
        inValue1 = 1.0;
        inValue2 = .7;
        inValue3 = .6;
        reverbLevel = 0.0;
        value1 = inValue1 * reverbLevel;
        value2 = inValue2 * reverbLevel;
        value3 = inValue3 * reverbLevel;
        cache = new byte[CACHE_SIZE];
        
        reverbFrame = new JPanel();
        reverbFrame.setSize(100,500);
        reverbFrame.setBorder(BorderFactory.createTitledBorder("Reverb"));
        
        reverbSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (reverbLevel));
        reverbSlider.setMinorTickSpacing(5);
        reverbSlider.setMajorTickSpacing(10);
        reverbSlider.setPaintTicks(true);
        reverbSlider.setPaintLabels(true);
        reverbSlider.setLabelTable(reverbSlider.createStandardLabels(10));
        reverbSlider.setBorder(BorderFactory.createTitledBorder("Reverb Level"));
        reverbSlider.addChangeListener(this);
        reverbFrame.add(reverbSlider);
        gui.add(reverbFrame);
    }

    public double[] process(double[] dataD)
    {
        byte[] data = new byte[16];
        for(int i = 0; i < data.length; i++)
        {
            data[i] = (byte)dataD[i];
        }
        int len = data.length;
        //System.out.println(data.length);
        for(int t = cache.length-1; t >= len; t--)
        {
            
            //System.out.println(t);
            byte x = cache[t-len];
            cache[t] = x;
        }
        
        for(int i = 0; i < len; i=i+1)
        {
            byte y =  data[i];
            cache[i] = y;
            //cache[i] = lpf.process(y);
        }
        for(int i = 0; i < len; i=i+1)
        {
            byte x =  data[i];
            double original = (double)data[i];
            double add = 0;
            double add1 = (double)cache[delay1 + i] *  value1;
            double add2 = (double)cache[delay2 +  i] *  value2;
            double add3 = (double)cache[delay3 + i] *  value3;
            double added = addSound(original, add1);
            added = addSound(added, add2 * 1);
            added = addSound(added, add3 * 1);
            data[i] = doubleToByte(added);
           
        }
        for(int i = 0; i < data.length; i++)
        {
            dataD[i] = (double)data[i];
        }
        return dataD;
    }
    
    public void stateChanged(ChangeEvent e) 
    {
        JSlider source = (JSlider)e.getSource();
        
        if (!source.getValueIsAdjusting()) 
        {
            reverbLevel = ((double) source.getValue())/(100.0);
            value1 = inValue1 * reverbLevel;
            value2 = inValue2 * reverbLevel;
            value3 = inValue3 * reverbLevel;
        }
    }
}
