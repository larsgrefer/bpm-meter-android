package de.larsgrefer.bpm_meter.base;

public class BPM
{
	
	public static double xpmFromDuration(double delay)
	{
		if( delay == 0 )
			return Double.POSITIVE_INFINITY;
		else
			return 60d / delay;
	}

	public static double durationFromXpm(double xpm)
	{
		if( xpm == 0 )
			return Double.POSITIVE_INFINITY;
		else
			return 60d / xpm;
	}
}
