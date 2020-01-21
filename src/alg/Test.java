package alg;

public class Test {
	/* INSTANCE FIELDS AND CONSTRUCTOR */
	private String instanceName;
	private String movement;
	private float percentageDistance; // period between events
	private float percentageDisruption; // p(x) disruption
	private long seed; // Seed value for the Random Number Generator (RNG)
	private double criterion;// 1100 connectivity // 1010 dist // 1001 weighted criterion
	private double drivingRange; // driving Range of the UAV Distance
	private float victimNodes; // driving Range of the UAV Distance

	public Test(String name, String m, float t, float disrup, double criterion, long s, double drivingRange,
			float vicitm) {
		instanceName = name;
		movement = m;
		percentageDistance = t; // weight for the distance criterion
		percentageDisruption = disrup;
		seed = s;
		victimNodes = vicitm;
		this.criterion = criterion;
		this.drivingRange = drivingRange;
	}

	/* GET METHODS */

	public String getMovementStrategy() {
		return movement;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public float getpercentageDistance() {
		return percentageDistance;
	} // the time is giving in minutes and here is becoming in hours

	public float getpercentangeDisruption() {
		return percentageDisruption;
	}

	public float getVictimNodesPercentage() {
		return victimNodes;
	}

	public long getseed() {
		return seed;
	}

	public double getdrivingRange() {
		return drivingRange;
	}

	public double getOptcriterion() {
		return criterion;
	}

}