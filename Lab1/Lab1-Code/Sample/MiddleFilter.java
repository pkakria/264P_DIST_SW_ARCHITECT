/******************************************************************************************************************
* File:MiddleFilter.java
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

public class MiddleFilter extends FilterFramework
{
	public void run()
    {
		int bytesread = 0;					// Number of bytes read from the input file.
		int byteswritten = 0;				// Number of bytes written to the stream.
		byte databyte = 0;					// The byte of data read from the file
		long previous_frame2 = 0;			//
		long previous_frame1 = 0;
		int MeasurementLen = 8;
		int lenFrame = 5;
		int id;
		int IdLen = 4;
		long measurement;
		byte marker;
		int i;


		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");
		previous_frame2 = getAltitude();
		previous_frame1 = getAltitude();
		readFrames();

		//reading frames--
		for(int i = 0; i<lenFrame; i++){
			readID();
			if (ID != 2){
				readMesaurements();
			}
			else
				marker = 0X00;
		}
		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/

			try
			{
				databyte = ReadFilterInputPort();
				bytesread++;
				WriteFilterOutputPort(databyte);
				byteswritten++;

			} // try

			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch

		} // while

   } // run

} // MiddleFilter