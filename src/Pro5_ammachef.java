import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Pro5_ammachef {
	
	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		SCPModel model = null;
		GreedyCoverageSolver CoverageMethod = new GreedyCoverageSolver();
		GreedyCostSolver CostMethod = new GreedyCostSolver();
		ChvatalSolver ChvatalMethod = new ChvatalSolver();
		String choice = "";
		double alpha = -1;
		boolean modelLoaded = false;
		
		do {
			displayMenu();
			choice = cin.readLine();
			if (choice.equalsIgnoreCase("M")) {
				model = getModelData();
				modelLoaded = true;
				resetMethods(CoverageMethod, CostMethod, ChvatalMethod);
			}
			else if (choice.equalsIgnoreCase("P")) {
				if (!modelLoaded) {
					System.out.println("\nERROR: No problem information has been loaded!\n");
				}
				else {
					System.out.println(model);
					System.out.format("Minimum coverage (alpha): ");
					if (alpha == -1)
						System.out.println("unspecified\n");
					else 
						System.out.format("%.2f%%\n\n", 100*alpha);
				}
			}
			else if (choice.equalsIgnoreCase("A")) {
				CoverageMethod.reset();
				CostMethod.reset();
				ChvatalMethod.reset();
				System.out.println();
				alpha = getDouble("Enter minimum coverage (alpha): ", 0d, 1d);
				setMinCoverage(alpha, CoverageMethod, CostMethod, ChvatalMethod);
				System.out.println();
			}
			else if (choice.equalsIgnoreCase("C") || choice.equalsIgnoreCase("S") || choice.equalsIgnoreCase("V")) {
				if (!modelLoaded) {
					System.out.println("\nERROR: No problem information has been loaded!\n");
				}
				else if (alpha == -1) {
					System.out.println("\nERROR: No minimum coverage percentage has been specified!\n");
				}
				else {
					if (choice.equalsIgnoreCase("C")) {
						CoverageMethod.setModel(model);
						CoverageMethod.solve();
						CoverageMethod.print();
					}
					else if (choice.equalsIgnoreCase("S")) {
						CostMethod.setModel(model);
						CostMethod.solve();
						CostMethod.print();
					}
					else {
						ChvatalMethod.setModel(model);
						ChvatalMethod.solve();
						ChvatalMethod.print();
					}					
				}
			}
			else if (choice.equalsIgnoreCase("X")) {
				// check that they have all been solved with this model instance
				boolean doCompare = true;
				if (CoverageMethod.isSolved() == false) {
					System.out.println("\nERROR: Cannot perform comparison because the greedy coverage method has not been run for this problem instance!\n");
					doCompare = false;
				}
				else if (CostMethod.isSolved() == false) {
					System.out.println("\nERROR: Cannot perform comparison because the greedy cost method has not been run for this problem instance!\n");
					doCompare = false;
				}
				else if (ChvatalMethod.isSolved() == false) {
					System.out.println("\nERROR: Cannot perform comparison because Chvatal's algorithm has not been run for this problem instance!\n");
					doCompare = false;
				}
				
				if (doCompare) {
					printComparison(alpha, CoverageMethod, CostMethod, ChvatalMethod);
				}
			}
			else if (!choice.equalsIgnoreCase("Q")) {
				System.out.println("\nERROR: Invalid menu choice!\n");
			}
			
		} while (!choice.equalsIgnoreCase("Q"));
		
		System.out.println("\r\nCiao!");

	}
	
	// handle entire process of collecting user data
	public static SCPModel getModelData() {
		System.out.println();
		int n = Pro5_ammachef.getInteger("Enter number of elements (n): ", 1, Integer.MAX_VALUE);
		int m = Pro5_ammachef.getInteger("Enter number of sets (m): ", 1, Integer.MAX_VALUE);
		
		SCPModel scpmodel = new SCPModel();
		for (int i = 1; i <= m; i++) {
			System.out.println("\nSet " + i + " details");
			double cost = Pro5_ammachef.getDouble("   Enter cost of set " + i + ": ", -Double.MAX_VALUE, Double.MAX_VALUE);
			
			int j = 1;
			List<Integer> elements = new ArrayList<Integer>();
			while(j != 0) {
				j = Pro5_ammachef.getInteger("   Enter an element covered by set " + i + " (0 to stop): ", 0, n);
				if (j != 0) 
					elements.add(j);
			}
			scpmodel.addElementSet(i, cost, elements);
		}
		//System.out.println("\n" + scpmodel);
		return scpmodel;
	}

	// reset the solution methods
	public static void resetMethods(GreedyCoverageSolver CoverageMethod, GreedyCostSolver CostMethod, ChvatalSolver ChvatalMethod) {
		CoverageMethod.reset();
		CostMethod.reset();
		ChvatalMethod.reset();
	}
	
	
	// set minimum coverage level for solution methods
	public static void printComparison(double alpha, GreedyCoverageSolver CoverageMethod, GreedyCostSolver CostMethod, ChvatalSolver ChvatalMethod) {
		System.out.format("\nAlpha: %.2f%%\n\n", 100*alpha);
		System.out.println("Algorithm                   Time (ms)     Obj Fn Val     Coverage (%)");
		System.out.println("---------------------------------------------------------------------");
		CoverageMethod.printRowMetrics();
		CostMethod.printRowMetrics();
		ChvatalMethod.printRowMetrics();
		System.out.println("---------------------------------------------------------------------");
		
		double tol = 1e-10;	// numerical tolerance
		
		// get time winner
		long minTime = CoverageMethod.getCompTime();
		String timeWinner = "Coverage";
		if (minTime - CostMethod.getCompTime() > tol) {
			minTime = CostMethod.getCompTime();
			timeWinner = "Cost";
		}
		if (minTime - ChvatalMethod.getCompTime() > tol) {
			minTime = ChvatalMethod.getCompTime();
			timeWinner = "Chvatal";
		}
		
		// get obj fn val winner
		double minObj = CoverageMethod.getObjFn();
		String objWinner = "Coverage";
		if (minObj - CostMethod.getObjFn() > tol) {
			minObj = CostMethod.getObjFn();
			objWinner = "Cost";
		}
		if (minObj - ChvatalMethod.getObjFn() > tol) {
			minObj = ChvatalMethod.getObjFn();
			objWinner = "Chvatal";
		}
		
		// get coverage winner
		double maxCov = CoverageMethod.getCoverage();
		String covWinner = "Coverage";
		if (CostMethod.getCoverage() - maxCov > tol) {
			maxCov = CostMethod.getCoverage();
			covWinner = "Cost";
		}
		if (ChvatalMethod.getCoverage() - maxCov > tol) {
			minObj = ChvatalMethod.getCoverage();
			covWinner = "Chvatal";
		}
		
		System.out.format("%-25s%12s%15s%17s\n", "Category winner", timeWinner, objWinner, covWinner);
		System.out.println("---------------------------------------------------------------------\n");
		
		String overall = "Unclear";
		if (timeWinner.equals(objWinner) && objWinner.equals(covWinner))
			overall = timeWinner;
		
		System.out.println("Overall winner: " + overall + "\n");
	}
	
	
	// set minimum coverage level for solution methods
	public static void setMinCoverage(double alpha, GreedyCoverageSolver CoverageMethod, GreedyCostSolver CostMethod, ChvatalSolver ChvatalMethod) {
		CoverageMethod.setMinCoverage(alpha);
		CostMethod.setMinCoverage(alpha);
		ChvatalMethod.setMinCoverage(alpha);
	}
	
	
	// display the menu
	public static void displayMenu() {
		System.out.println("   JAVA SET COVER PROBLEM SOLVER");
		System.out.println("M - Enter SCP model data");
		System.out.println("P - Print SCP instance");
		System.out.println("A - Set minimum coverage percentage");
		System.out.println("C - Solve SCP with the greedy coverage heuristic");
		System.out.println("S - Solve SCP with the greedy cost heuristic");
		System.out.println("V - Solve SCP with Chvatal's algorithm");
		System.out.println("X - Compare algorithm performance");
		System.out.println("Q - Quit\n");
		System.out.print("Enter choice: ");
	}

	
	// get an integer in [LB, UB]
	public static int getInteger(String prompt, int LB, int UB) {
		int x = 0;
		boolean valid;
		do {
			valid = true;
			System.out.print(prompt);
			try {
				x = Integer.parseInt(cin.readLine());
			}
			catch (IOException e) { 
				valid = false;
			}
			catch (NumberFormatException e) {
				valid = false;
			}
			
			if (valid && (x < LB || x > UB)) {
				valid = false;
			}
			
			if (!valid) {
				if (UB == Integer.MAX_VALUE && LB == -Integer.MAX_VALUE)
					System.out.format("ERROR: Input must be an integer in [-infinity, infinity]!\n\n");
				else if (UB == Integer.MAX_VALUE)
					System.out.format("ERROR: Input must be an integer in [%d, infinity]!\n\n", LB);
				else if (LB == -Integer.MAX_VALUE)
					System.out.format("ERROR: Input must be an integer in [-infinity, %d]!\n\n", UB);
				else	
					System.out.format("ERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
			}
		} while (!valid);
		return x;	
	} // end getInteger
	
	
	// get a double in [LB, UB]
	public static double getDouble(String prompt, double LB, double UB) {
		double x = 0;
		boolean valid;
		do {
			valid = true;
			System.out.print(prompt);
			try {
				x = Double.parseDouble(cin.readLine());
			}
			catch (IOException e) { 
				valid = false;
			}
			catch (NumberFormatException e) {
				valid = false;
			}
			
			if (valid && (x < LB || x > UB)) {
				valid = false;
			}
			
			if (!valid) {
				if (UB == Double.MAX_VALUE && LB == -Double.MAX_VALUE)
					System.out.format("ERROR: Input must be a real number in [-infinity, infinity]!\n\n");
				else if (UB == Double.MAX_VALUE)
					System.out.format("ERROR: Input must be a real number in [%.2f, infinity]!\n\n", LB);
				else if (LB == -Double.MAX_VALUE)
					System.out.format("ERROR: Input must be a real number in [-infinity, %.2f]!\n\n", UB);
				else	
					System.out.format("ERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
			}
		} while (!valid);
		return x;	
	} // end getDouble
	
}
