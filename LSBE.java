import java.io.*;
import java.awt.image.*;
//import java.awt.Font;
import javax.imageio.*;
//import java.awt.Color;
//import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.*;
//import java.awt.image.BufferedImage;

class LSBE
{
	public static void main(String[] argv)
	{
		//reader to read from standard input
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Type 'exit' to quit");
		//file name to open, read from standard input
		while(true)
		{
			String FileName;
			//Bufferedimage to operate on, opened using the file name
			BufferedImage input;
		
			try
			{
				//read a file name from standard input
				FileName = reader.readLine();
				
				//System.out.println(FileName);
				//if the filename is the keyword exit, then exit the program.
				if(FileName.trim().toLowerCase().equals("exit"))return;
				
				System.out.println("Working on file " + FileName);
				//read the file, using an ImageIO reader
				input = ImageIO.read(new File(FileName));
			}
			catch(IOException e)
			{
				System.out.println("Error reading file, Terminating");
				return;
			}
		
			//convert the image to an array of bytes which can easily be operated on
			Raster inputRaster = input.getRaster();
			byte[] data = ((DataBufferByte) inputRaster.getDataBuffer()).getData();
		
			//testing how java handles bit shift operators
			//this will move the LSB to the MSB, and zero out all other digits
			//((byte)((((byte)0x1)<<7)&0x80))
			//same thing, but printed
			//System.out.println(((byte)((((byte)0x1)<<7)&0x80)));
		
			//for each byte in the image (all channels) amplify the MSB
			for(int i = 0; i<data.length;i++)
			{
				//data[i] = ((byte)(((byte)data[i])<<7));
				if((data[i]&0x01) == 1)
					data[i] = (byte)255;
				else
					data[i] = 0;
			}

		
			//convert back to a bufferedimage, to allow saving
				//get the SampleModel from the origional image (input and output should have identical SampleModels)
			SampleModel inputSampleModel = inputRaster.getSampleModel();
				//create a databuffer, which contains the modified data
			DataBuffer outputData = new DataBufferByte(data, data.length);
				//create a point which has the value (0,0)
			Point origin = new Point();		
				//assemble the previous three objects into a Raster, which can be used to construct a bufferedimage
			Raster outputRaster = Raster.createRaster(inputSampleModel, outputData, origin);
				//construct the buffered image
			input.setData(outputRaster);
		
			//save the image to the output file
			try
			{
				FileName = FileName.split("[.]")[0];
				//String[] tempName = FileName.split("/");
				//FileName = tempName[tempName.length - 1] + "-LSBE.png";
				//FileName = FileName.replace('/','?');
				File outputfile = new File("outputs/" +FileName);
				ImageIO.write(input, "png", outputfile);
				//System.out.println("File " + FileName + "-LSBE.png written");
			}
			catch(IOException e)
			{
				System.out.println("Error while saving image");
			}
		}
  }
}
