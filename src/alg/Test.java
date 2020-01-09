package alg;

import umontreal.iro.lecuyer.rng.RandomStream;
import umontreal.iro.lecuyer.rng.RandomStreamBase;


public class Test
{
	/* INSTANCE FIELDS AND CONSTRUCTOR */
	private String instanceName;
	private float percentageDistance; // period between events
	private float percentageDisruption; //p(x) disruption
	private static float travelSpeed;//travel speed
	private static long seed; // Seed value for the Random Number Generator (RNG)
	private double criterion;// optimization criterion 1011 dist-imp // 1101 imp- dist // 1100 imp  //  1010 dist  // 1001 weighted criterion  
	private static RandomStream rng;
	private  double drivingRange; // driving Range of the UAV Distance
	private  float victimNodes; // driving Range of the UAV Distance
	



// Test(instanceName,percentageDistance,prob,speed,criterion,seed)

	public Test(String name,  float t, float disrup,float speed,double criterion,long s, double drivingRange, float vicitm)
	{
		instanceName = name;   
		percentageDistance = t; // weight for the distance criterion
		percentageDisruption=disrup;
		travelSpeed=speed;
		seed=s;
		victimNodes=vicitm;
		this.criterion=criterion;
		this.drivingRange=drivingRange;
	}




	/* GET METHODS */
	public String getInstanceName(){return instanceName;}
	public float getpercentageDistance(){return percentageDistance;} // the time is giving in minutes and here is becoming in hours
	public  float getpercentangeDisruption(){return percentageDisruption;}
	public  float getVictimNodesPercentage(){return victimNodes;}
	public static float getTravelSpeed(){return travelSpeed;}
	public long getseed(){return seed;}
	public static RandomStream getRandomStream() {return rng;}
	public double getdrivingRange() {return drivingRange;}
	
	
	public static void setRandomStream(RandomStream stream) {rng = stream;}
	
	public double getOptcriterion() {return criterion;	}


}