package alg;

import umontreal.iro.lecuyer.rng.RandomStream;
import umontreal.iro.lecuyer.rng.RandomStreamBase;

//import umontreal.ssj.rng.RandomStream;

/**
 * @author Angel A. Juan - ajuanp(@)gmail.com
 * @version 130112
 */
public class Test
{
	/* INSTANCE FIELDS AND CONSTRUCTOR */
	private static String instanceName;
	private static float percentageDistance; // period between events
	private static float percentageDisruption; //p(x) disruption
	private static float travelSpeed;//travel speed
	private static long seed; // Seed value for the Random Number Generator (RNG)
	private double criterion;// optimization criterion 1011 dist-imp // 1101 imp- dist // 1100 imp  //  1010 dist  // 1001 weighted criterion  
	private static RandomStream rng;



// Test(instanceName,percentageDistance,prob,speed,criterion,seed)

	public Test(String name,  float t, float disrup,float speed,double criterion,long s)
	{
		instanceName = name;   
		percentageDistance = t; // weight for the distance criterion
		percentageDisruption=disrup;
		travelSpeed=speed;
		seed=s;
		this.criterion=criterion;
		
	}




	/* GET METHODS */
	public static String getInstanceName(){return instanceName;}
	public static float getpercentageDistance(){return percentageDistance;} // the time is giving in minutes and here is becoming in hours
	public static float getpercentangeDisruption(){return percentageDisruption;}
	public static float getTravelSpeed(){return travelSpeed;}
	public static long getseed(){return seed;}
	public static RandomStream getRandomStream() {return rng;}

	public static void setRandomStream(RandomStream stream) {rng = stream;}


	public double getOptcriterion() {return criterion;	}


}