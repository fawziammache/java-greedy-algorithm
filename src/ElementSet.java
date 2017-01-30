import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// This class holds a set ID, it's cost, and the element IDs that the set covers
public class ElementSet implements Comparable {

	private int _id; // Set ID
	private double _cost; //2 Cost of using this Set ID
	private SortedSet<Integer> _elements; // Element IDs that this set covers
	
	// Initialize this ElementSet
	public ElementSet(int id, double cost, Collection<Integer> elements) {
		this._id = id; // Initialize ID
		this._cost = cost; //Initialize cost
		
		this._elements = new TreeSet<>();
		
		// Copies elements 
		for (Integer e: elements)
			this._elements.add(e);
	}
	
	//Constructor, implemented by myself
	public ElementSet() {
		
		this._id = 0;
		this._cost = 0;
		
		this._elements = new TreeSet<Integer>();
	}
	
	// Simple getters
	public int getId()      { return _id;   }
	public double getCost() { return _cost; }
	
	// Setters, myself
	public void setId (int id) { this._id = id; }
	public void setCost (double cost) { this._cost = cost; }
	public void setElements (Collection<Integer> elements) {
		this._elements.clear();	
		for (Integer e: elements)
			this._elements.add(e);
	}
	
	
	//My own function
	public Collection<Integer> getElements() {
		return this._elements;
	}
	
	
	// Returns the number of element IDs in this set that cover element IDs in elements_to_cover.
	// You'll find this very useful during the greedy algorithm implementation.
	public int countElementsCovered(Set<Integer> elements_to_cover) {
		
		int elementsCovered = 0;
		
		for (Integer i: getElementIterable())
			if (elements_to_cover.contains(i)) elementsCovered++;
		
		return elementsCovered; // TODO
	}

	@Override
	public int compareTo(Object o) {
		// TODO: We inherit from the Comparable interface that allows this object to
		//       be stored in a SortedSet so we need to implement this method.  It should
		//       put ElementSet's with lower Set IDs earlier in a comparison ordering.
		//       See supplementary lecture slides or just use Google to understand what
		//       this method returns.
		if (!(o instanceof ElementSet))
			return 1;
		
		ElementSet object = (ElementSet) o;
		
		if (this._id < object._id) 
			return -1;
		
		if (this._id > object._id)
			return 1;
		
		return 0; // if they are equal
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO: Many set-based methods such as add(...) and contains(...) require equals to 
		//       be defined properly, otherwise multiple copies of an equivalent object
		//       may be stored on add(...) and contains(...) would not work as intended.
		//       See supplementary lecture slides or just use Google to understand what
		//       this method returns.
		if (!(o instanceof ElementSet))
			return false;
		
		ElementSet object = (ElementSet) o;
		
		return this._id == object._id && this._cost == object._cost && this._elements.equals(object._elements);		
	}
	
	// With the following method, we can write 
	//   for (Integer i : getElementIteratable()) { 
	//      do something with i;
	//   }
	// so that we can iterate through the Integer elements in 
	// this class *without having any further ability to modify* 
	// the underlying _elements instance.  See SCPModel for a  
	// similar method and how it is used in toString() there.
	public Iterable<Integer> getElementIterable() {
		return (Iterable<Integer>)_elements;
	}
	
	@Override
	public String toString() {
		return String.format("Set ID: %3d   Cost: %6.2f   Element IDs: %s", _id, _cost, _elements);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		SortedSet<Integer> cover = new TreeSet<Integer>();
		cover.add(3);
		cover.add(5);
		cover.add(1);
		cover.add(2);
		cover.add(4);
		
		System.out.println("Cover: " + cover);
		
		SortedSet<Integer> elements = new TreeSet<Integer>();
		elements.add(3);
		elements.add(5);
		elements.add(1);
		//elements.add(4);
		//elements.add(2);
		
		System.out.println("Elements: " + elements);
		

		ElementSet element = new ElementSet(1, 15.2, elements);
		ElementSet obj = new ElementSet(3, 5.2, cover);
		
		System.out.println(element.toString());
		
		System.out.println("Elements covered: " + element.countElementsCovered(cover)); 
		
		int i = element.compareTo(obj);
		boolean b = element.equals(obj);
		
		System.out.println("Compare to: " + i);
		System.out.println("Equals: " + b);
		
		
	}

	
}
