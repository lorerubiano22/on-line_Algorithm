package alg;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class TestsManager {
	public static ArrayList<Test> getTestsList(String testsFilePath) {
		ArrayList<Test> list = new ArrayList<Test>();

		try {
			FileReader reader = new FileReader(testsFilePath);
			Scanner in = new Scanner(reader);

			in.useLocale(Locale.US);
			while (in.hasNextLine()) {
				if (in.hasNext()) {
					String s = in.next();
					if (s.charAt(0) == '#') // this is a comment line
						in.nextLine(); // skip comment lines
					else {
						String instanceName = s;
						String movementStrategy = in.next().toUpperCase();
						float percentageDistance = in.nextFloat(); // to compute the weight- weight for the distance
																	// criterion
						float prob = in.nextFloat();// disruption p(x)
						double criterion = in.nextDouble();// importance is the optimization criterion
						Long seed = in.nextLong();// seed
						double drivingRange = in.nextDouble();// driving range
						float victimNodes = in.nextFloat();// victims
						Test aTest = new Test(instanceName, movementStrategy, percentageDistance, prob, criterion, seed,
								drivingRange, victimNodes);
						list.add(aTest);
					}
				} else
					in.nextLine();

			}
			in.close();
		} catch (IOException exception) {
			System.out.println("Error processing tests file: " + exception);
		}
		return list;
	}
}