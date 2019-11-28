package alg;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Generates a list of tests to be run.
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130807
 */
public class TestsManager
{
	public static ArrayList<Test> getTestsList(String testsFilePath)
	{   ArrayList<Test> list = new ArrayList<Test>();

	try
	{   FileReader reader = new FileReader(testsFilePath);
	Scanner in = new Scanner(reader);
	// The two first lines (lines 0 and 1) of this file are like this:
	//# instance | maxTime(sec) | ...
	// A-n32-k5       30      ...
	in.useLocale(Locale.US);
	while( in.hasNextLine() )
	{  
		if(in.hasNext()){
			String s = in.next();
			if (s.charAt(0) == '#') // this is a comment line
				in.nextLine(); // skip comment lines
			else
			{   String instanceName = s; // e.g.: A-n32-k5
			//float maxTime = in.nextFloat(); // max computational time (in sec)
			float percentageDistance= in.nextFloat(); // to compute the weight- weight for the distance criterion 
			float prob= in.nextFloat();// disruption p(x)
			float speed= in.nextFloat();// travel speed p(x)
			double criterion=in.nextDouble();// importance is the optimization criterion
			Long seed= in.nextLong();// seed
			//boolean Euclidean=in.nextBoolean();// Euclidean distance
			//int MaxIter=in.nextInt();// maximum number of interations
			Test aTest = new Test(instanceName,percentageDistance,prob,speed,criterion,seed);
			list.add(aTest);
			}
		}
		else in.nextLine();

	}
	in.close();
	}
	catch (IOException exception)
	{   System.out.println("Error processing tests file: " + exception);
	}
	return list;
	}
}