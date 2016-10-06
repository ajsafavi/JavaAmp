
/**
 * Write a description of class RCHighPass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RCHighPass extends AmpEffect
{
    // instance variables - replace the example below with your own
    private double alpha;
    private double dt;
    private double vinprev;
    private double voutprev;

    /**
     * Constructor for objects of class RCHighPass
     */
    public RCHighPass(double freq, double sr)
    {
        dt = 1/sr;
        double a = 2*Math.PI * freq * dt;
        alpha = 1/(a + 1);
        vinprev = 0;
        voutprev = 0;
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
        
        double vout =  alpha * (voutprev + vin - vinprev);
        
        vinprev = vin;
        voutprev = vout;
        data = vout;
        return data;
    }
    
}
