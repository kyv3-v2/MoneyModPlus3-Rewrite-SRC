package wtf.moneymod.client.impl.utility.impl.math;

public enum MathUtil
{
    INSTANCE;
    
    public double clamp(final double value, final double min, final double max) {
        return Math.max(Math.min(value, max), min);
    }
    
    public double square(final double input) {
        return input * input;
    }
    
    public float square(final float input) {
        return input * input;
    }
    
    public double interpolate(final double from, final double to, final double difference) {
        return from + (to - from) * difference;
    }
}
