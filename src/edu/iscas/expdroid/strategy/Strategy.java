package edu.iscas.expdroid.strategy;

import edu.iscas.expdroid.model.TStateGraph;

public interface Strategy {
	/**
	 * to start concrete an Explore Strategy 
	 */
    public void startExp();
    
    public TStateGraph obtainGraph();
    
    public void monitorCrash();
}
