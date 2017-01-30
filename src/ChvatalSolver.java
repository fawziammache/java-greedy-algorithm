
public class ChvatalSolver extends GreedySolver {
	
	public ChvatalSolver() {
		this._name = "Chvatal's algorithm";
		this.reset();
	}
	
	@Override
	public ElementSet nextBestSet() {
		
		
		ElementSet nextBestSet = null;
		
		double currentCostCovRatio = 0;
		double bestCostCovRatio = Double.POSITIVE_INFINITY;
		int elementsCovered = 0;
		
		for (ElementSet e: this._model.getElementSetIterable()){
		
			elementsCovered = e.countElementsCovered(_elementsNotCovered);
			
			if (elementsCovered < 1)
				continue;
			
			currentCostCovRatio = e.getCost() / elementsCovered;
			
			
			if (currentCostCovRatio < bestCostCovRatio){
				nextBestSet = e;
				bestCostCovRatio = currentCostCovRatio;
			}

		}
		
		return nextBestSet; 
	}
	
}
