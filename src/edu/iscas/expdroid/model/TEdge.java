package edu.iscas.expdroid.model;

import java.util.ArrayList;
import java.util.List;

public class TEdge {
     public enum EdgeType {auto,manual};
     public EdgeType edgeType;
     public TState nextState;
     public List<TEvent> eventTrace;
    
     public TEdge(){
    	  edgeType=EdgeType.auto;
    	  nextState=null;
    	  eventTrace=new ArrayList<TEvent>();
     }
     
     public TEdge(EdgeType etype,TState state,List<TEvent> etrace){
    	  edgeType=etype;
   	      nextState=state;
   	      eventTrace=etrace;
     }

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null) return false;
		TEdge edge=(TEdge)obj;
		if(eventTrace.size()!=edge.eventTrace.size()) return false;
		if(nextState.No!=edge.nextState.No) return false;
	    for(TEvent event:eventTrace){
	        if(!edge.eventTrace.contains(event)) return false;
	    }
		return true;
	}
     
     
     
}
