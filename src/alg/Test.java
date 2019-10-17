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
	private String instanceName;
	private float maxTime; // period between events
	private float probDisruption; //p(x) disruption
	private static float travelSpeed;//travel speed
	private long seed; // Seed value for the Random Number Generator (RNG)
	private boolean typeNetwork;
	private int StopCriterion;// Max num iterations
	private double criterion;// optimization criterion 1011 dist-imp // 1101 imp- dist // 1100 imp  //  1010 dist  // 1001 weighted criterion  
	private RandomStream rng;





	public Test(String name,  float t, float disrup,float speed,double criterion,long s, boolean euclidean, int MaxIter)
	{
		instanceName = name;   
		maxTime = t; // interval of times it represents how the stop criteria is splited
		probDisruption=disrup;
		travelSpeed=speed;
		seed=s;
		this.criterion=criterion;
		typeNetwork=euclidean;
		StopCriterion=MaxIter; // number of events or time horizont
	}


	

	/* GET METHODS */
	public String getInstanceName(){return instanceName;}
	public float getMaxTime(){return maxTime;} // the time is giving in minutes and here is becoming in hours
	public float getprobDisruption(){return probDisruption;}
	public static float getTravelSpeed(){return travelSpeed;}
	public long getseed(){return seed;}
	public boolean gettypeNetwork(){return typeNetwork;}
	public int getStopCriterion() {return StopCriterion;}
	public RandomStream getRandomStream() {return rng;}

	public void setRandomStream(RandomStream stream) {rng = stream;}


	public double getOptcriterion() {return criterion;	}


}