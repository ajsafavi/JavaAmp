
/**
 * Write a description of class LowPassFilter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LowPassFilter extends AmpEffect
{
    // instance variables - replace the example below with your own
    private double R,L,C; //(dt/(RC+dt))
    private double dt;
    private double iprev, vprev;
    private Compressor comp;

    /**
     * Constructor for objects of class LowPassFilter
     */
    public LowPassFilter(double r, double l, double c, double sr)
    {
        // initialise instance variables
        dt = 1/sr;
        R = r;
        L = l;
        C = c;
        iprev = 0;
        vprev = 0;
        comp = new Compressor(40, .55);
    }

    public byte[] process(byte[] data)
    {
        int len = data.length;
        //prev = (double) data[0];
        for(int i =  0; i < len; i++)
        {
            data[i] = process(data[i]);
        }
        return data;
    }
    public byte process(byte data)
    {
        double vin = (double)data;
        vin = (double)comp.process((byte)vin);
        double i = ((vin/R+L*iprev/(dt*R))+C/dt * (vin+L*iprev/dt-vprev))/(1+L/(dt*R)+C*L/(dt*dt));
        double vout = vin - L*(i-iprev)/dt;
        //vout = (double)comp.process((byte)vout);
     
        //System.out.println(i);
        
        iprev = i;
        vprev = vout;
        
//         if(Math.abs(vout) > Math.abs(vin))
//         {
//             vout = vin;
//         }
        
        vout = vout * .5;
        vout = vout +  data*.5;
        data = doubleToByte(vout);
        return data;
    }
}
