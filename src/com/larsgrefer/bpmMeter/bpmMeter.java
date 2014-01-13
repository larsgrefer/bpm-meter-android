package com.larsgrefer.bpmMeter;

public class bpmMeter 
{
	public byte taktart;
	public boolean takte;
	public byte traegheit;
	private double delay;
	private long lastTap;
	private double taps = 0;
	
	public double bpm, tpm, bd, td;
	public bpmMeter()
	{
		taktart = 4;
		takte = true;
		traegheit = 10;
		
	}
	
	public void tap( )
	{
		if( ( (double)( System.currentTimeMillis() - lastTap ) / 1000d ) >= 5 )
			reset( );

		taps++;
		if( taps > 2 )
			delay = ( ( (double)( System.currentTimeMillis() - lastTap ) / 1000d ) + ( delay * traegheit ) ) / ( traegheit + 1 );
		else if( taps > 1 )
			delay = (double)( System.currentTimeMillis() - lastTap ) / 1000d;
		lastTap = System.currentTimeMillis();
		update( );
	}
	
	public void reset( )
	{
		lastTap = 0;
		taps = 0;
		delay = 0;
		update( );
	}
	
	void update( )
	{

		if( taps == 1 )
		{
			bpm = 0;
			tpm = 0;
			bd = Double.POSITIVE_INFINITY;
			td = Double.POSITIVE_INFINITY;
		}
		else if( taps > 1 || ( taps == 0 && delay != 0 ) )
		{
			if( !takte )
			{
				bd = delay;
				td = delay * taktart;
				double bpm = BPM.FromDelay( delay );
				this.bpm = bpm;
				tpm = bpm / taktart;
			}
			else
			{
				td = delay;
				bd = delay / taktart;
				double tpm = BPM.FromDelay( delay );
				bpm = tpm * taktart;
				this.tpm = tpm;
			}
		}
		else
		{
			bpm = Double.POSITIVE_INFINITY;
			tpm = Double.POSITIVE_INFINITY;
			td = 0;
			bd = 0;
		}
	}
	
	public void setBPM( double BpM )
	{
		if( !takte )
			delay = BPM.delay( BpM );
		else
			delay = BPM.delay( BpM / taktart );
		update( );
	}
	
	public void setTPM( double TpM )
	{
		if( !takte )
			delay = BPM.delay( TpM * taktart );
		else
			delay = BPM.delay( TpM );
		update( );
	}
	
	public void setBD( double DB )
	{
		takte = false;
		delay = DB;
		update();
	}
	
	public void setTD( double TD )
	{
		takte = true;
		delay = TD;
		update();
	}
}
