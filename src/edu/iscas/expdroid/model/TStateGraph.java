package edu.iscas.expdroid.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TStateGraph {
     public List<TState> initState; //suppose has mul-initstate
     public List<TState> allState;   
     public TStateGraph(){
    	 initState=new ArrayList<TState>();
    	 allState=new ArrayList<TState>();
     }
     
     public Set<TEvent> getAllEvents(){
    	 Set<TEvent> rlist=new HashSet<TEvent>();
    	 for(TState s:allState){
    		 rlist.addAll(s.events);
    	 }
    	 return rlist;
     }
}
