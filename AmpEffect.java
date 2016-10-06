
/**
 * Abstract class AmpEffect - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class AmpEffect
{
    // instance variables - replace the example below with your own
    private byte[] previous;
    

    public double[] process(double[] data)
    {
        return data;
    }
    
    public byte doubleToByte(double input) //converts double to byte and makes sure its within range
    {
        byte newByte = (byte)(input);
        if (input > (double)Byte.MAX_VALUE)
            newByte = Byte.MAX_VALUE;
        if (input < (double)Byte.MIN_VALUE)
            newByte = Byte.MIN_VALUE;
        return newByte;
    }
    public double addSound(double a, double b)
    {
        if (a < 0 && b < 0)
            return (a+b) - (a*b/-128);
        if (a > 0 && b > 0)
            return (a+b) - (a*b/128);
        return a + b;
    }
}
