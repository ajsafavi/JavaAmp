
/**
 * Write a description of class Averager here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Averager extends AmpEffect
{
    // instance variables - replace the example below with your own
    double wa;
    int samples;
    /**
     * Constructor for objects of class Averager
     */
    public Averager()
    {
        wa  = 0;
        samples = 1;
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
            data[i] = (data[i] + wa * (samples-1))/samples;
            wa = data[i];
        }
        return data;
    }
}
