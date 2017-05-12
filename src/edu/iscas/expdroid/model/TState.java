package edu.iscas.expdroid.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.UiNode;

import edu.iscas.expdroid.tools.Config;

public class TState {
	public int No;
	public BasicTreeNode rootNode;
    public List<UiNode> uiSet;
    public List<TEvent> events;
    public List<TEvent> preTrace; //the trace from initial state to current state
    public Set<TEdge> edges;
    public Map<Integer,Integer> eventExpCount;  //map record event trigger time
    public int cursor;  //only use in BFS ,indicate the index of next trigger event in the event list 
    
    public float priority; //only use in heuristic 
    
    public String actName; //the name of activity that this state belong 
    public TState(){
    	uiSet=new ArrayList<UiNode>();
    	events=new ArrayList<TEvent>();
    	preTrace=new ArrayList<TEvent>();
    	edges=new HashSet<TEdge>();
    	eventExpCount=new HashMap<Integer,Integer>();
    	cursor=0;
    	priority=70.0f;
    	actName="";
    }
    
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 1;
	}
/*	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		//add later
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		TState state=(TState)obj;
		//ui-number match paterrn 
		int uiSum=this.uiSet.size(); 
		if(uiSum==0) return state.uiSet.isEmpty();
		int sameNodeCount=0;
		for(UiNode node:state.uiSet){
			if(this.uiSet.contains(node)) sameNodeCount++;
		}
        float similar=1-((float)(uiSum-sameNodeCount)/(float)uiSum);
        System.out.println("state"+this.No+" and state"+state.No+" similar is "+similar);
        //ui-structure match pattern add later
		return similar>=Config.g().similarRadio;
	}*/
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		//add later
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		TState state=(TState)obj;
	    if(!state.actName.equals(this.actName)) return false;
	    List<UiNode> s1=new ArrayList<UiNode>();
	    List<UiNode> s2=new ArrayList<UiNode>();
		for(UiNode node:this.uiSet){
			if(!node.hasChild()) s1.add(node);
		}
		for(UiNode node:state.uiSet){
			if(!node.hasChild()) s2.add(node);
		}
	    int sameCount=0;
		for(int i=0;i<s1.size();i++){
			for(int j=0;j<s2.size();j++){
				String xpath1=s1.get(i).getXPath();
				String xpath2=s2.get(j).getXPath();
				if(xpath1.equals(xpath2)){
			           sameCount++;
			           break;
				}
			}
		}
		//System.out.println("samecount="+sameCount+" s1.size="+s1.size()+" s2.size="+s2.size());
		float similar=((float)sameCount*2)/(s1.size()+s2.size());
        //System.out.println("state"+this.No+" and state"+state.No+" similar is "+similar);
        //ui-structure match pattern add later
		return similar>=Config.g().similarRadio;
	}
	
	

}
