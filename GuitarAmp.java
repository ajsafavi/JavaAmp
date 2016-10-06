import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import java.util.ArrayList;
/**
 * Write a description of class GuitarAmp here.
 * guitar amp takes in a microphone input and applies effects to it, then plays it back
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GuitarAmp implements Runnable
{
    // instance variables - replace the example below with your own

    private Thread thread;
    private AudioFormat format;
    private TargetDataLine microphone;
    private SourceDataLine speakers;
    private int CHUNK_SIZE;
    private float SAMPLE_RATE;
    private float DT; //change in time per sample
    private ArrayList<AmpEffect> effects;
    private ByteArrayOutputStream out;
    private Visualizer vis;
    /**
     * Default Constructor for objects of class GuitarAmp
     */
    public GuitarAmp()
    {
        CHUNK_SIZE = 16;
        effects = new ArrayList<AmpEffect>();
        SAMPLE_RATE = 44100.0f;
        DT = 1.0f/SAMPLE_RATE;
        format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
        out = new ByteArrayOutputStream();
        try
        {
            microphone = AudioSystem.getTargetDataLine(format);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            out = new ByteArrayOutputStream((int)SAMPLE_RATE * 1);
            microphone.start();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
        }
        catch (LineUnavailableException e) 
        {
            e.printStackTrace();
        } 
    }
    
    public void setVis(Visualizer v)
    {
        vis = v;
    }
    public ByteArrayOutputStream getOutput()
    {
        return out;
    }
    
    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }
    
    public void close()
    {
        thread.interrupt();
        speakers.drain();
        speakers.close();
        microphone.close();
    }
    
    public void addEffect(AmpEffect e)
    {
        effects.add(e);
        System.out.println("Effect added " + e.toString());
    }
    
    public void run() 
    {

        
        byte[] data = new byte[CHUNK_SIZE];
        double[] dataD = new double[16];
        int numBytesRead;
  
        while (true) 
        {
            numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
            //System.out.println(data.length);
            //data
            
            for(int i = 0; i < data.length; i++)
            {
                dataD[i] = (double)data[i];
            }
            for (AmpEffect effect : effects) 
            {
       
                dataD = effect.process(dataD);
            }
            for(int i = 0; i < data.length; i++)
            {
                data[i] = (byte)dataD[i];
            }
            // write mic data to stream for immediate playback
            out.write(data, 0, numBytesRead);
            speakers.write(data, 0, numBytesRead);
            //System.out.println(data[0]);
        }

    }
}
