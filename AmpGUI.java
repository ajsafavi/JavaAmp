import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
/**
 * Write a description of class AmpGUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AmpGUI extends JFrame 
{
    // instance variables - replace the example below with your own
    private GuitarAmp amp;
    private Visualizer vis;
    private JPanel effectsPanel;
    
    /**
     * Main Method
     */
    public static void main(String[] args)
    {
        AmpGUI a = new AmpGUI();
    }
    /**
     * Constructor for objects of class AmpGUI
     */
    public AmpGUI()
    {
        super(); 
        setTitle("JavaAmp");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );
        setSize(500,500);
        amp = new GuitarAmp();
        effectsPanel = new JPanel();
        effectsPanel.setBorder(BorderFactory.createTitledBorder("Effects"));
        addEffects();
        add(effectsPanel);
        vis = new Visualizer(effectsPanel, amp);
        amp.setVis(vis);
        pack();
        amp.start();
        vis.start();
    }
    public void addEffects()
    {
        //SineGen sg = new SineGen(effectsPanel);
        //amp.addEffect(sg);
        
       //low pass at 2.5k hz to remove noise

        //Averager avg = new Averager();
        //amp.addEffect(avg);
        
        Compressor preComp = new Compressor(effectsPanel);
        amp.addEffect(preComp);
        
        TubeDistortion tubeDistortion = new TubeDistortion(effectsPanel);
        amp.addEffect(tubeDistortion);
        
        TubeDistortion tubeDistortion2 = new TubeDistortion(effectsPanel);
        amp.addEffect(tubeDistortion2);
        
        TubeDistortion tubeDistortion3 = new TubeDistortion(effectsPanel);
        amp.addEffect(tubeDistortion3);
        
        Gain gain = new Gain(effectsPanel);
        amp.addEffect(gain);
        
        Equalizer eq = new Equalizer(effectsPanel);
        amp.addEffect(eq);
        
        Reverb reverb = new Reverb(effectsPanel);
        amp.addEffect(reverb);
        
        //Equalizer eq = new Equalizer();
        //amp.addEffect(eq);
        //RCLowPass lpf = new RCLowPass(400,44100);
       // amp.addEffect(lpf);
        
        //RCHighPass hpf = new RCHighPass(300,44100);
        //amp.addEffect(hpf);
        
    }
    
    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */

}
