import java.util.SortedSet;
import java.util.TreeSet;

// The main class that abstracts a greedy SCP solver
	public abstract class GreedySolver {
	protected String _name;			  // name of algorithm type
	protected double _alpha;          // minimum required coverage level
	protected SCPModel _model;        // the SCP model we're currently operating on
	protected TreeSet<ElementSet> _solnSets; // could use array instead
	SortedSet<Integer> _elementsNotCovered; // set of the elements currently not covered by solution
	protected double _objFn;          // objective function value (*total cost sum* of all sets used)
	protected double _coverage;       // actual coverage percent achieved
	protected long _compTime;         // computation time (ms)
	protected boolean _solved;        // whether or not the model has been solved
	
	public GreedySolver() {
		_name = "NAME NOT SET"; // Subclasses should set this in their constructor
		_model = null;
		reset();
	}
	
	// Basic setters
	public void setMinCoverage(double alpha) { _alpha = alpha; }
	public void setModel(SCPModel model) { _model = model; }
	
	// Basic getters
	public double getMinCoverage() { return _alpha; }
	public int getNSetsSelected()  { return _solnSets.size(); }
	public double getObjFn()       { return _objFn; }
	public double getCoverage()    { return _coverage; }
	public long getCompTime()      { return _compTime; }
	public boolean isSolved()      { return _solved; }
	public String getName()        { return _name; }
	
	// Reset all values (except for SCPModel)
	public void reset() {
		_solnSets = new TreeSet<ElementSet>();
		_elementsNotCovered = null;
		_objFn = 0d;
		_coverage = 0d;
		_compTime = 0;
		_solved = false;
	}
	
	// Run the simple greedy heuristic -- add the next best set until either
	//   (1) The coverage level is reached, or 
	//   (2) There is no set that can increase the coverage.
	// Some initialization and final settings have been written for you, but
	// there are three TODO parts remaining.
	public void solve() {
		
		// Reset the solver
		reset();
		
		// Preliminary initializations
		int num_to_cover = (int)Math.ceil(_alpha * _model.getNumElements());
		int num_can_leave_uncovered = _model.getNumElements() - num_to_cover;
		boolean all_selected = false; // Have all sets been selected?
		

		// TODO: Initialize the following to the correct set of initial Element IDs that 
		//       are not covered (i.e., should be all Element IDs in the SCPModel).
		_elementsNotCovered = new TreeSet<Integer>();
		
		// Copy all elements from model to _elementsNotCovered
		_elementsNotCovered.addAll(this._model.getElements());
		
		// Begin the greedy selection loop
		long start = System.currentTimeMillis();
		System.out.println("Running '" + getName() + "'...");
		while (_elementsNotCovered.size() > 0 && 
			   _elementsNotCovered.size() > num_can_leave_uncovered && 
			   !all_selected) {

			
			// TODO: Get the next best ElementSet to add (if there is one).
			ElementSet nextBestSet = this.nextBestSet();
			
			if (nextBestSet == null){
				all_selected = true;
				break;
			}
			
			// TODO: Update solnSets, objFn, elements_not_covered, and all_selected.
			
			this._solnSets.add(nextBestSet);
			this._objFn += nextBestSet.getCost();
			this._elementsNotCovered.removeAll(nextBestSet.getElements());

			
			System.out.println("- Selected: " + nextBestSet.toString());
			
		}
		
		
		// Set coverage, solved, compTime and print warning if applicable
		_coverage = (_model.getNumElements() - _elementsNotCovered.size())/(double)_model.getNumElements();
		_solved = true;
		_compTime = System.currentTimeMillis() - start;
		if (_coverage < _alpha)
			System.out.format("\nWARNING: Impossible to reach %.2f%% coverage level.\n", 100*_alpha);
		System.out.println("Done.");
	}
	
	// To be implemented in child classes.
	// NOTE: if no set can improve the solution, returns null to allow the greedy algorithm to terminate.
	public abstract ElementSet nextBestSet();
	
	// Print the solution
	public void print() {
		System.out.println("\n'" + getName() + "' results:");
		System.out.format("'" + getName() + "'   Time to solve: %dms\n", _compTime);
		System.out.format("'" + getName() + "'   Objective function value: %.2f\n", _objFn);
		System.out.format("'" + getName() + "'   Coverage level: %.2f%% (%.2f%% minimum)\n", 100*_coverage, 100*_alpha);
		System.out.format("'" + getName() + "'   Number of sets selected: %d\n", _solnSets.size());
		System.out.format("'" + getName() + "'   Sets selected: ");
		for (ElementSet s : _solnSets) {
			System.out.print(s.getId() + " ");
		}
		System.out.println("\n");
	}
	
	// Print the solution performance metrics as a row
	public void printRowMetrics() {
		System.out.format("%-25s%12d%15.4f%17.2f\n", getName(), _compTime, _objFn, 100*_coverage);
	}	
}
