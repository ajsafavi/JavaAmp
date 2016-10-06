 

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

public class AudioTest {

    public static void main(String[] args) {

        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        TargetDataLine microphone;
        AudioInputStream audioInputStream;
        SourceDataLine sourceDataLine;
        try {
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            int bytesRead = 0;

            try {
                while (bytesRead < 100000) { // Just so I can test if recording
                                                // my mic works...
                    numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                    bytesRead = bytesRead + numBytesRead;
                    System.out.println(bytesRead);
                    out.write(data, 0, numBytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte audioData[] = out.toByteArray();

            // Get an input stream on the byte array
            // containing the data
            InputStream byteArrayInputStream = new ByteArrayInputStream(
                    audioData);
            audioInputStream = new AudioInputStream(byteArrayInputStream,format, audioData.length / format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            int cnt = 0;
            byte tempBuffer[] = new byte[10000];
            
            try {
                while ((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }// end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Block and wait for internal buffer of the
            // data line to empty.
            sourceDataLine.drain();
            sourceDataLine.close();
            microphone.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public static void playback() 
    {
        float SAMPLE_RATE = 44100.0f;
        float dt = 1.0f/SAMPLE_RATE;
        float C = .0005f;
        float C2 = .0005f;
        float C_dt = C/dt;
        float alpha = (dt / (C + dt));
        float beta = (C2 / (C2 + dt));
       
        
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        TargetDataLine microphone;
        SourceDataLine speakers;
        try {
            microphone = AudioSystem.getTargetDataLine(format);
    
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
    
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 8;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();
    
            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            double vinold = 0.0;
            double vold = 0.0;
            while (bytesRead < 10000000) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                out.write(data, 0, numBytesRead); 
                

                //gain circuit
                
                double gain = 5.0;
                for(int i = 0; i < data.length; i++)
                {
                    double newData = (double)data[i] * gain;
                    byte newByte = (byte)(newData);
                    if (newData > (double)Byte.MAX_VALUE)
                        newByte = Byte.MAX_VALUE;
                    if (newData < (double)Byte.MIN_VALUE)
                        newByte = Byte.MIN_VALUE;
                    data[i] = newByte;
                }
                
                //low pass filter
//                 for(int i = 0; i < data.length; i++)
//                 {
//                     double doubleData = (double)data[i];
//                     double newData = (double) vold + alpha*(doubleData - (double)vold);
//                     byte newByte = (byte)(newData);
//                     if (newData > (double)Byte.MAX_VALUE)
//                         newByte = Byte.MAX_VALUE;
//                     if (newData < (double)Byte.MIN_VALUE)
//                         newByte = Byte.MIN_VALUE;
//                     data[i] = newByte;
//                     vold = newData;
//                 }
                //high pass filter
//                 for(int i = 0; i < data.length; i++)
//                 {
//                     vinold = (double) data[i];
//                     double doubleData = (double)data[i];
//                     double newData = beta * (doubleData +  (vold - vinold));
//                     byte newByte = (byte)(newData);
//                     if (newData > (double)Byte.MAX_VALUE)
//                         newByte = Byte.MAX_VALUE;
//                     if (newData < (double)Byte.MIN_VALUE)
//                         newByte = Byte.MIN_VALUE;
//                     data[i] = newByte;
//                     vold = newData;
//                 }
                System.out.println(data[0]);
                // write mic data to stream for immediate playback
                speakers.write(data, 0, numBytesRead);
            }
            speakers.drain();
            speakers.close();
            microphone.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } 
    }
    
}