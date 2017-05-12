package edu.iscas.expdroid.strategy;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import edu.iscas.expdroid.exception.MessageNotFoundException;
import edu.iscas.expdroid.model.TEdge;
import edu.iscas.expdroid.model.TEdge.EdgeType;
import edu.iscas.expdroid.shelltools.ADB;
import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.model.TEvent.ActionOper;
import edu.iscas.expdroid.model.TEvent.EventType;
import edu.iscas.expdroid.model.TState;
import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.Statistic;

public class Heuristic extends StrategyImpl{
    private enum EventResult {stay,toOld,toNew};
    private EventResult er;
	@Override
	public void startExp(){
		// TODO Auto-generated method stub
		if(Config.g().doManualExp){
			 System.out.println("do manual script");
			 StrategyImpl manual=new Manual();
			 manual.startExp();
			 copyGraphInfo(manual);
			 System.out.println("manual script finished!");
		}
		
		//do heuristic
	    if(null==curState){  //no explore has been done before
	    	//start initial activity
	    	startLauchActivity();
	    	initialGraph();
	    	cmd.takeScreenshot("0");
	    }
	    
	    while(true){
	    	//check in target app or restart
	    	if(!isInPackage(curState)||isFinishedExp(curState)){
	    		System.out.println("out of application, restart");
	    		// try {
					//ADB.ignite("");
					//startLauchActivity();
					if(null==goRandomState()){//!goMaxPrioState//null==goRandomState()
						 System.out.println("no state to explore");
						 Statistic.end();
		    	    	 break;
					}
				/*} catch (IOException | InterruptedException | MessageNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	    		
	    	}
	    	TEvent event=ehelper.selectHeuristicEvent1(curState);
	    	if(event==null){
	    		 System.out.println("not event to executed,expored will finished");
	    		 Statistic.end();
    	    	 break;
	    	}else{
	    		eventSerial.add(event);
	    	}
	    	//check click event and has type event in current state
	    	if((event.action==ActionOper.click)
	    			&&(event.object.getNClass().equals("android.widget.Button"))
	    			&&ehelper.hasTypeEvent(curState)){
	    		ehelper.fillAllText(curState);
	    	}
            System.out.println("before execute a event");
	    	boolean r=ehelper.executeEvent(event);
	    	   if(r){
	   	    	   //check crash  
    	    	   if(crashOccured){
    	    		 restartFromCrashInH();
    	    	   }
    	    	   else
	    		     extendGraph(event);
	    	   }else {
	    		   System.out.println("trigger event fail! event:"+event);
	    		   eventSerial.remove(eventSerial.size()-1); //remove the failed event
	    		   goMaxPrioState();
	    	   }
	    	

	    	if(Statistic.expExit){
	    		System.out.println("accept exit signal,exlpored will finished");
	    		Statistic.end();
	    		break;
	    	}
     	}
	}


	@Override
	protected void extendGraph(TEvent event) {
		// TODO Auto-generated method stub
		TState cState=ehelper.getCurState();
		TState newState;
		 if(isLastState(cState)){
			 er=EventResult.stay;
			 newState=curState;
			 System.out.println("old state");
		 }else{
		   if(isNewState(cState)){
			 er=EventResult.toNew;
			 System.out.println("new  state");
			 cState.No=stateGraph.allState.size();
			 cState.preTrace.addAll(new ArrayList<TEvent>(curState.preTrace));
			 cState.preTrace.addAll(new ArrayList<TEvent>(eventSerial));
			 stateGraph.allState.add(cState);
			 TEdge edge=new TEdge(EdgeType.auto,cState,new ArrayList<TEvent>(eventSerial));
			 curState.edges.add(edge);
			 newState=cState;
			 //collect state of activityInfo later
			 
			 //save screenshot
			 cmd.takeScreenshot(cState.No+"");
			 
		   }else{  //state has been before 
			   er=EventResult.toOld;
			   TState ccState=searchState(stateGraph,cState);
			   if(ccState==null) return ;
			   TEdge edge=new TEdge(EdgeType.auto,ccState,new ArrayList<TEvent>(eventSerial));
			   curState.edges.add(edge);
			   //change to short pretrace to state
			   if(curState.preTrace.size()+eventSerial.size()<ccState.preTrace.size()){
				   List<TEvent> newPreTraces=new ArrayList<TEvent>();
				   newPreTraces.addAll(new ArrayList<TEvent>(curState.preTrace));
				   newPreTraces.addAll(new ArrayList<TEvent>(eventSerial));
				   ccState.preTrace=new ArrayList<TEvent>(newPreTraces);
			   }
			   newState=ccState;
		   }
		   eventSerial.clear();
		   //goTargetState(curState);
		}
		 System.out.println("curState hascode="+curState.hashCode()+ " no="+curState.No);
		 adjustPriority(event,curState);
		 chooseNextState(newState);
	}
	private void adjustPriority(TEvent event,TState state){
		 System.out.println("state hascode="+state.hashCode()+ " no="+state.No);
		System.out.println("begin adjust priority at state "+ state.No+ " state.priority="+state.priority);
		//adjust similar event priority in state
		//System.out.println("even is "+event.action);
		for(int i=0;i<state.events.size();i++){
			 TEvent e=state.events.get(i);
			 System.out.println("e is "+e.action+" e.priority="+e.priority);
			 if(e.priority>1&&e.isSimilar(event)){
				 System.out.println("find similar event");
				 if(er==EventResult.toNew){
					 e.priority+=5;	
				 }else if(er==EventResult.stay){
					 e.priority-=10;
				 }else if(er==EventResult.toOld){
					 e.priority-=10;
				 }
			//	 System.out.println("after adjust e is "+e.action+" e.priority="+e.priority);
				 e.priority=e.priority<0?0:e.priority;
				 e.priority=e.priority>100?100:e.priority;
			 }
		 }
		//adjust state priority based contains events-priority
		int ecount=0; //count of not fired events
		float eprio=0f;
		for(int j=0;j<state.events.size();j++){
			if(state.events.get(j).priority<1) continue;
				ecount++;
				eprio+=state.events.get(j).priority;
		}
		state.priority=ecount==0?0.0f:eprio/ecount;
		System.out.println("after adjust priority at state "+state.No+" state.priority="+state.priority);
	}
/*	private void adjustPriority(TEvent event,TState state){
		System.out.println("beigin adjust priority at state "+ state.No+ " state.priority="+state.priority);
		//adjust similar event priority in state
		for(int i=0;i<state.events.size();i++){
			 TEvent e=state.events.get(i);
			 if(isSimilarEvent(event,e)&&!state.eventExpCount.containsKey(i)){
				// System.out.println("find similar event");
				 if(er==EventResult.toNew){
					 e.priority+=5;
				 }else if(er==EventResult.stay){
					 e.priority-=5;
				 }else if(er==EventResult.toOld){
					 e.priority-=3;
				 }
				 e.priority=e.priority<0?0:e.priority;
				 e.priority=e.priority>100?100:e.priority;
			 }
		 }
		//adjust state priority based contains events-priority
		int ecount=0; //count of not fired events
		float eprio=0f;
		for(int j=0;j<state.events.size();j++){
			if(!state.eventExpCount.containsKey(j)){
				ecount++;
				eprio+=state.events.get(j).priority;
			}
		}
		state.priority=ecount==0?0.0f:eprio/ecount;
		System.out.println("after adjust priority at state "+state.No+" state.priority="+state.priority);
	}*/
	
	
	
	
	private void chooseNextState(TState state){
	    if(isFinishedExp(state)){
	    	System.out.println("finish explored state "+state.No);
	    	cmd.goBack();
	    	TState s=ehelper.getCurState();
	    	 if(isInPackage(s)){
				 TState  ccState=searchState(stateGraph,s);
			     curState=ccState!=null?ccState:state;
			     if(isFinishedExp(curState)){
			    	/*  TState rstate=goRandomState();
			    	  curState=rstate==null?curState:rstate;*/
			    	 goMaxPrioState();
			     }
			 }else
			     curState=state;
	    }else{
	    	if(!isInPackage(state)){ //state is out of app
				 cmd.goBack();
				 TState s=ehelper.getCurState();
				 if(isInPackage(s)){
					 TState  ccState=searchState(stateGraph,s);
					 System.out.println("ccState="+ccState);
				     curState=ccState!=null?ccState:state;
				     if(ccState==null||(ccState!=null&&isFinishedExp(curState))){
				    	 /* TState rstate=goRandomState();
				    	  curState=rstate==null?curState:rstate;*/
				    	 goMaxPrioState();
				     }
				 }else
				     curState=state;
				 return;
			 }
	    	 float [] r=new float[2];
			 TState maxPrio=computeMaxState(r);
			 float max=r[0],avg=r[1];
			 System.out.println("state avg="+avg+" , max="+max);
			 System.out.println("curstate state "+state.No+" state.priority "+ state.priority+"is finished? "+ isFinishedExp(state));
			/* if(//!isFinishedExp(state)&&
					 state.priority+10>avg&&
					 state.priority+30>max){*/
		     if(state.priority>(avg*1/2)){
				 curState=state;
				 System.out.println("stay at current state..state.priopity is "+state.priority);
			
			 }else{
				 System.out.println("choose new state");
				 cmd.goBack();
				 TState s=ehelper.getCurState();
				 if(isInPackage(s)){
					 TState  ccState=searchState(stateGraph,s);
					 System.out.println("ccState="+ccState);
				     curState=ccState!=null?ccState:state;
				     if(ccState==null||(ccState!=null&&isFinishedExp(curState))){
				    	 /* TState rstate=goRandomState();
				    	  curState=rstate==null?curState:rstate;*/
				    	 goMaxPrioState();
				     }
				 }else
				     curState=state;
			 }
			 
		  
	    }
	}
	/*
	private void chooseNextState(TState state){
		 System.out.println("begin choose new state");
		 if(isFinishedExp(state)){
			 //search neigbour state
			 System.out.println("finish explored state "+state.No);
			 List<TEvent> traces=new ArrayList<TEvent>();
			 TState maxChild=searchMaxNeighbor(state,traces);
			 System.out.println("maxChild="+maxChild+" tarces size "+traces.size());
			 if(maxChild!=null&&traces!=null&&!traces.isEmpty()){
				 goMaxChildState(traces);
				 curState=maxChild;
				 System.out.println("switch to a new state,from state("+state.No+","+state.priority+") to max child state("+
						 maxChild.No+","+maxChild.priority+")");
			 }else{ //no reachable child state,so go back
				 /*TState ccState;
				 TState ts=null;
				 do{
					 ccState=null;
					 cmd.goBack();
					 ts=ehelper.getCurState();
					 if(!isInPackage(ts)) break;
					 ccState=searchState(stateGraph,ts);
					 System.out.println("isfinish ccState "+ccState);
				 }while(ccState==null||isFinishedExp(ccState));
				 curState=ccState==null?ts:ccState;*/
				 //goMaxPrioState();
		/*		  System.out.println("random selected a state");
				  TState selected=goRandomState();
				  curState=selected==null?state:selected;
			 }
		 }else{//check if need change state
			 if(!isInPackage(state)){ //state is out of app
				 cmd.goBack();
				 TState s=ehelper.getCurState();
				 if(isInPackage(s)){
					 TState  ccState=searchState(stateGraph,s);
					 System.out.println("ccState="+ccState);
				     curState=ccState!=null?ccState:state;
				     if(ccState!=null&&isFinishedExp(curState)){
				    	  TState rstate=goRandomState();
				    	  curState=rstate==null?curState:rstate;
				     }
				 }else
				     curState=state;
				 return;
			 }
			 float [] r=new float[2];
			 TState maxPrio=computeMaxState(r);
			 float max=r[0],avg=r[1];
			 System.out.println("state avg="+avg+" , max="+max);
			 System.out.println("curstate state "+state.No+" is finished? "+ isFinishedExp(state));
			 if(//!isFinishedExp(state)&&
					 state.priority+10>avg&&
					 state.priority+30>max){
				 curState=state;
				 System.out.println("stay at current state..state.priopity is "+state.priority);
				 return ;
			 }
			 List<TEvent> traces=new ArrayList<TEvent>();
			 TState maxChild=searchMaxNeighbor(state,traces);
			 System.out.println("maxChild="+maxChild);
			 if(maxChild!=null&&traces!=null&&!traces.isEmpty()){
				 if(maxChild.priority>state.priority+10){
					 goMaxChildState(traces);
					 curState=maxChild;
					 System.out.println("switch to a new state,from state("+state.No+","+state.priority+") to state("+
							 maxChild.No+","+maxChild.priority+")");
					 return ;
				 }
			 }
			 
			 if(maxPrio!=null){
				 System.out.println("goto max state ,from state("+state.No+","+state.priority+") to state("+
						 maxPrio.No+","+maxPrio.priority+")");
				 goTargetState(maxPrio);
				 curState=maxPrio;
			 }else{
				 System.out.println("all states has been explored!");
			 }
		  }
			 
		 
	}*/
	
	private boolean isFinishedExp(TState state){
	     if(state==null) return true;
	     boolean result=state.priority<1;//state.events.size()==state.eventExpCount.size();
	     return result;
	}
	/**
	 * 
	 * @param state  start state 
	 * @param target  use to point to the target state which has max priority 
	 * @return if target not null, return traces from state to target
	 */
	private TState searchMaxNeighbor(TState state, List<TEvent> traceToTarget){
		// List<TEvent> traceToTarget=null;
		 TState target=null;
		 //store trace to children State from  param-state
		 Map<TState,List<TEvent>> map=new HashMap<TState,List<TEvent>>(); 
		 Queue<TState> queue=new LinkedList<TState>();
		 queue.offer(state);
		 map.put(state, new ArrayList<TEvent>());
		 int level=0;
		 while(!queue.isEmpty()){
			 TState cur=queue.poll();
			 System.out.println("cur state is "+cur.No+" out edge size="+cur.edges.size());
			 for(TEdge edge:cur.edges){ 
				 TState child=edge.nextState;
				 if(isFinishedExp(child)||!isInPackage(child)) continue; //skip state has explored finished
				 queue.add(child);
				 //record the trace to this child state 
				 List<TEvent> trace=new ArrayList<TEvent>();
				 trace.addAll(new ArrayList<TEvent>(map.get(cur)));  //from start state to parent node traces
				 trace.addAll(new ArrayList<TEvent>(edge.eventTrace)); //add parent to this child trace 
				 map.put(child, trace);
				 
				 if(target==null||target.priority<child.priority){
					 System.out.println("find a child state "+child.No);
					 target=child;
					 traceToTarget.addAll(new ArrayList<TEvent>(map.get(child)));
					 System.out.println("trace to target size "+ traceToTarget.size());
				 }
			 }
			 level++;
			 if(level>3) break;
		 }
		 return target;
	}
	
	private void goMaxChildState(List<TEvent> traces){
		try{
			for(TEvent e:traces){
		    	ehelper.executeEvent(e);
		    }
		}catch(Exception e){
			e.printStackTrace();	
		}
	}
    
	private TState computeMaxState(float[] maxmin){
		float max=0,avg=0;
		 int count=0;
		 TState maxPrio=null;
		 for(TState s:stateGraph.allState){
			 if(!isFinishedExp(s)&&isInPackage(s)){//!isFinishedExp(s)&&
			   if(s.priority>=max){
				   max=s.priority;
				   maxPrio=s;
			   }
			   avg+=s.priority;
			   count++;
			 }
		 }
		 avg=count==0?0:avg/count;
		 maxmin[0]=max;
		 maxmin[1]=avg;
		 return maxPrio;
	}
	
	private boolean goMaxPrioState(){
		float[] minmax=new float[2];
		TState maxState=computeMaxState(minmax);
		System.out.println("max state "+maxState);
		if(maxState!=null){
			System.out.println("maxState!=null");
			goTargetState(maxState);
		    curState=maxState;
		    return true;
		}else{
			System.out.println("maxState==null");
			return false;
		}
	}
	
	
	private TState goRandomState(){
		 List<TState> states=new ArrayList<TState>();
		 for(TState s:stateGraph.allState){
			 if(!isFinishedExp(s)&&isInPackage(s)){
			     states.add(s);
			 }
		 }
		 if(states.isEmpty()) return null;
	     Random random=new Random();
	     random.setSeed(System.currentTimeMillis());
	     //choose event type ui:sys=4:1
	     int sIndex=random.nextInt(states.size());
	     TState target=states.get(sIndex);
	     System.out.println("go random state "+ target.No+ "state.priority="+target.priority);
	     goTargetState(target);
	     curState=target;
	     return target;
	}
	/**
	 * go near state from initial state.
	 * near state is the state which prepath is short 
	 * @return
	 */
	private TState goNearState(){
		 List<TState> states=new ArrayList<TState>();
		 for(TState s:stateGraph.allState){
			 if(!isFinishedExp(s)&&isInPackage(s)){
			     states.add(s);
			 }
		 }
		 if(states.isEmpty()) return null;
		 TState near=null;
		 int minLength=10000;
		 for(TState s:states){
			 if(s.preTrace.size()<minLength){
				 minLength =s.preTrace.size();
				 near=s;
			 }
		 }
		  System.out.println("go near state "+ near.No+ "state.priority="+near.priority);
		  goTargetState(near);
		  curState=near;
		 return near;
	}
	
	
	
	public void restartFromCrashInH(){
		 try{
			 if(eventSerial!=null&&!eventSerial.isEmpty()) eventSerial.clear();
		      if(null!=curState&&!isFinishedExp(curState)){
			     goTargetState(curState);
		      }else{
		    	  TState state=goRandomState();
		    	  curState=state==null?curState:state;
		      } 
		    	  
			  System.out.println("restart from crash!");
			  crashOccured=false;
			 }catch(Exception e){
				  e.printStackTrace();
			 }
	}
	

}
