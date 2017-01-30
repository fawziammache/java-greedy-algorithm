import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class GreedyCostSolver extends GreedySolver {
	
	public GreedyCostSolver() {
		this._name = "Greedy cost heuristic";
		this.reset();
	}
	
	@Override
	public ElementSet nextBestSet() {
		
		double currentSetCost = 0;
		double bestSetCost = Double.MAX_VALUE;
		int elementsCovered = 0;
		ElementSet nextBestSet = null;
		
		for (ElementSet e: this._model.getElementSetIterable()){
			
			elementsCovered = e.countElementsCovered(_elementsNotCovered);
			
			if(elementsCovered < 1)
				continue;
			
			currentSetCost = e.getCost();
			
			if (currentSetCost < bestSetCost){
				nextBestSet = e;	
				bestSetCost = currentSetCost;
			}
		}
		
		
		return nextBestSet; 
	}
	
}
