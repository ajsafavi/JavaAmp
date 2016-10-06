import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;
/**
 * Write a description of class Visualizer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Visualizer implements Runnable
{
    // instance variables - replace the example below with your own
    private volatile ArrayList<Byte> cache;
    private int CACHE_SIZE;
    private JPanel visPanel;
    private JPanel soundPanel;
    private Canvas soundCanvas;
    private JPanel freqPanel;
    private Canvas freqCanvas;
    private Thread thread;
    private GuitarAmp amp;
    private ByteArrayOutputStream in;
    
    private FastFourierTransformer fft;
    private int freqToShow;

    /**
     * Constructor for objects of class Visualizer
     */
    public Visualizer(JPanel gui, GuitarAmp amplifier)
    {
        fft = new FastFourierTransformer(DftNormalization.STANDARD);
        
        
        CACHE_SIZE = 300;
        cache = new ArrayList<Byte>();
        
        visPanel = new JPanel();
        visPanel.setSize(600,250);
        visPanel.setBorder(BorderFactory.createTitledBorder("Visualizer"));
        
        soundPanel = new JPanel();
        soundPanel.setSize(300,250);
        soundPanel.setBorder(BorderFactory.createTitledBorder("Sound Wave"));
        soundCanvas = new Canvas();
        soundCanvas.setSize(300,250);
        soundPanel.add(soundCanvas);
        visPanel.add(soundPanel);
        
        freqPanel = new JPanel();
        freqPanel.setSize(310,260);
        freqPanel.setBorder(BorderFactory.createTitledBorder("Frequencies"));
        freqCanvas = new Canvas();
        freqCanvas.setSize(300,256);
        freqPanel.add(freqCanvas);
        visPanel.add(freqPanel);
        
        in = amplifier.getOutput();
        gui.add(visPanel);
        
    }

    
    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }
    public void run() 
    {
        int sampleSize = 4096;
        int width = 300;
        int height = 256;
        Image img = soundCanvas.createImage(width, height);
        Graphics g =  img.getGraphics();
        
        Image img2 = freqCanvas.createImage(width, height);
        Graphics g2 = img2.getGraphics();
        try
        {
            Thread.sleep(200);
        }
        catch(Exception e)
        {
        }
        while(true)
        {
            //draw sound wave
            g.setColor(Color.white);
            g.fillRect(0,0,width,height);
            g.setColor(Color.blue);
            int prevX = 0;
            int prevY = 0;
            int range = 2048;
            byte audioData[] = in.toByteArray();
            int step = 10; //draw ever step 
            //System.out.println(audioData.length);
            if(audioData.length < range)
            {
                continue;
                //range = audioData.length;
            }
            int[] data = new int[range];
            for(int i = 0; i < range; i+= 1)
            {
                data[i] = (int)audioData[i];
            }
            for(int i = 0; i < range; i+= 8)
            {
                int x = (int)(((double)i/(double)range) * (double)width);
                int y = 128 + data[i];
                g.fillRect(x,y,1,1);
                //System.out.println(x + "," + y);
               
                if( i > 0)
                {
                    g.drawLine(prevX, prevY, x, y);
                }
                prevX = x;
                prevY = y;
            }
            
            //show frequency spectra
            g2.setColor(Color.white);
            g2.fillRect(0,0,width,height);
            g2.setColor(Color.blue);
            double[] newData = new double[range];
            for(int i = 0; i < data.length; i++)
            {
                newData[i] = (double)data[i];
            }
            Complex[] tData = fft.transform(newData, TransformType.FORWARD);
            
            prevX = 0;
            prevY = height;
            double maxX = Math.log((double)(range/4 -1) )/Math.log(2);
            for(int i = 1; i < range/4 -1 ; i+= 1) //about 20k hz max
            {
                int x = (int)(((Math.log((double)i)/Math.log(2))/maxX) * (double)width);
                //System.out.println(x);
                int y = (int)(-(tData[i].abs())/100.0) + 250;
                //System.out.println(y);
                g2.fillRect(x,y,1,1);
                //System.out.println(x + "," + y);
               
                if( i > 0)
                {
                    g2.drawLine(prevX, prevY, x, y);
                }
                prevX = x;
                prevY = y;
            }
            
            in.reset();
            soundCanvas.getGraphics().drawImage(img,0,0,null);
            freqCanvas.getGraphics().drawImage(img2,0,0,null);
            //soundCanvas.update(g);
            try
            {
                Thread.sleep(75);
            }
            catch(Exception e)
            {
            }
        }
    }
}
