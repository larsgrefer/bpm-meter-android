package com.larsgrefer.bpmMeter;

public class BPM
{
	
	public static double FromDelay( double delay )
	{
		if( delay == 0 )
			return Double.POSITIVE_INFINITY;
		else
			return (double)(60d / delay);
	}

	public static double delay( double BPM )
	{
		if( BPM == 0 )
			return Double.POSITIVE_INFINITY;
		else
			return (double)(60d / BPM);
	}
}
