
/**
 * Write a description of class RCLowPass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RCLowPass extends AmpEffect
{
    // instance variables - replace the example below with your own
    private double alpha;;
    private double dt;
    private double vprev; 
   
    /**
     * Constructor for objects of class RCLowPass
     */
    public RCLowPass(double freq, double sr)  
    {
        // initialise instance variables
        dt = 1/sr;
        double a = 2*Math.PI * freq * dt;
        alpha = a/(a + 1);
        vprev = 0;
    }
    public double[] process(double[] data)
    {
        int len = data.length;
        //prev = (double) data[0];
        for(int i =  0; i < len; i++)
        {
            data[i] = process(data[i]);
        }
        return data;
    }
    public double process(double data)
    {
        double vin = data;
        double vout =  (vprev + alpha * (vin - vprev));
        vprev = vout;
        data = vout;
        return data;
    }
}
