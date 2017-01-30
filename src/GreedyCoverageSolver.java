import java.util.*;


public class GreedyCoverageSolver extends GreedySolver {
	
	public GreedyCoverageSolver() {
		this._name = "Greedy coverage heuristic";
		this.reset();
	}
	
	@Override
	public ElementSet nextBestSet() {

		int currentSetCoverage = 0;
		int bestSetCoverage = 0;
		
		ElementSet nextBestSet = null;
		
		for (ElementSet e: this._model.getElementSetIterable()){
			
			currentSetCoverage = e.countElementsCovered(this._elementsNotCovered);
			
			if (currentSetCoverage > bestSetCoverage){
				bestSetCoverage = currentSetCoverage;
				nextBestSet = e;
			}

		}
		
		return nextBestSet;
	}
	
}
