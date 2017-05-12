package edu.iscas.expdroid.strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.model.TState;
import edu.iscas.expdroid.model.TStateGraph;
import edu.iscas.expdroid.shelltools.ADB;
import edu.iscas.expdroid.shelltools.Cmd;
import edu.iscas.expdroid.shelltools.ExceCmd;
import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.tools.EventHelper;
import edu.iscas.expdroid.tools.ScriptHelper;
import edu.iscas.expdroid.tools.Statistic;
import edu.iscas.expdroid.utils.UtilE;

public abstract class StrategyImpl implements Strategy{
   
	public TStateGraph stateGraph;
	public static TState curState;
	public List<TEvent> eventSerial;
	public Cmd cmd=new Cmd(); 
	public EventHelper ehelper=new EventHelper();
    public boolean crashOccured=false;
	
	protected void startLauchActivity(){
		try{ 
		  if(Rec.startActivity!=null&&!Rec.startActivity.equals(""))
		    cmd.initial(Rec.testedPackage,Rec.startActivity);
		  else {
			  System.out.println("error occuer:cannot get laucher activity!");
			  
		  }
		}catch(Exception e){
			
		}
	}
	
	
	
	protected void initialGraph(){
		stateGraph=new TStateGraph();
    	eventSerial=new ArrayList<TEvent>();
    	TState state=ehelper.getCurState();
    	state.No=0;
    	stateGraph.initState.add(state);
    	stateGraph.allState.add(state);
    	curState=state;
	}

	
	protected boolean isNewState(TState state){
		 if(stateGraph==null||stateGraph.allState.isEmpty()) return true;
		 boolean result=true;
		 for(TState s:stateGraph.allState){
			 if(s.equals(state)){
				 result=false;
				 break;
			 }
		 }
		 return result;
	}
	
	protected boolean isLastState(TState state){
		if(curState==null) return false;
		return curState.equals(state);
	}
	
	protected boolean isInPackage(TState state){
		if(state==null) return false;
		return (!state.uiSet.isEmpty())&&(state.uiSet.get(0).getPackage().equals(Rec.testedPackage));
	}
	
	protected void goTargetState(TState state){
		try{
			String targetPackage=Rec.testedPackage;
			//reinstall and save coverageInfo to file
			//ExceCmd.getInstance().execCommand("adb shell am startservice -n "+targetPackage+"/com.example.codecoverage.CollectService");
		    ExceCmd.getInstance().execCommand(ADB.acmd+" shell am force-stop "+targetPackage);
		    Rec.reinstallTargetApp();
		    //start target app
		    cmd.initial(Rec.testedPackage,Rec.startActivity);
		    for(TEvent e:state.preTrace){
		    	ehelper.executeEvent(e);
		    }
		    if(eventSerial!=null&&!eventSerial.isEmpty()) eventSerial.clear();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	/**
	 * search state from StateGraph
	 * @param graph
	 * @param target 
	 * @return
	 */
	protected TState searchState(TStateGraph graph,TState target){
		 for(TState state:graph.allState){
			 if(state.equals(target)) return state;
		 }
		 return null;
	}
	
	
	@Override
	public TStateGraph obtainGraph() {
		// TODO Auto-generated method stub
		return stateGraph;
	}
	
	@Override
	public void monitorCrash() {
		// TODO Auto-generated method stub
		try{
	    int crashIndex=1;
	    while(!Statistic.expExit){
	    System.out.println("restart monitor crash at time "+ UtilE.getCurTime());
		ADB.runcmd(ADB.lcat+" -c");
      	Thread.sleep(1000); //wait logcat print
   	    String out="";
        int count=0;
        String crashMessage="";
      	while(out.isEmpty()){
      		 out=ExceCmd.getInstance().execCommandForResult(ADB.lcat+" -d *:E|grep EXCEPTION");
      		// out=ADB.runcmd(); //-d dump log and no block
      		// System.out.println("out ="+out);
      		 if(out.contains("FATAL EXCEPTION")){
      			  System.out.println("crash occured");
      			  crashOccured=true;
      			  crashMessage=out;
      			  break;
      		 }else{
      			 out="";
      			 Thread.sleep(1000);
      			 count++;
      		 }
      		 
      		 if(Statistic.expExit) break;
      		
      	 }
      	 if(crashOccured){
		      saveCrashPath(crashIndex++,crashMessage);
		     // UtilE.sendMessageBySocket("crash occur", "localhost", 5051);
		  }
	    }
		
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	private void saveCrashPath(int index,String message){
		List<TEvent> eventList=new ArrayList<TEvent>();
		String pathSizeStr="";
		if(curState!=null){
			eventList.addAll(new ArrayList<TEvent>(curState.preTrace));
		    pathSizeStr+="state size :"+curState.preTrace.size();
			System.out.println("pretrace size is "+curState.preTrace.size());
		}else{
			System.out.println("pretrace size is 0");
			pathSizeStr+="state size :0";
		}
		pathSizeStr+="pretrace size :"+ (null==eventSerial?"0":eventSerial.size()+"");
		if(eventSerial!=null)
			eventList.addAll(new ArrayList<TEvent>(eventSerial));
		String common="Crash No."+index+"\n \\\\"+message+" "+pathSizeStr+"\n";
		ScriptHelper.buildEspressoScript(eventList,common,ScriptHelper.Crash_SCRIPT_PATH);
	}
	
	public  void restartFromCrash(){
		 try{
		 if(eventSerial!=null&&!eventSerial.isEmpty()) eventSerial.clear();
	      if(null!=curState){
		     goTargetState(curState);
	      }else {
	    	  Rec.reinstallTargetApp();
	    	  //wait for app load finished,execute preTrace to arrive the target state
			  Thread.sleep(2000);
	    	  cmd.initial(Rec.testedPackage,Rec.startActivity);
	      }  
		  System.out.println("restart from crash!");
		  crashOccured=false;
		 }catch(Exception e){
			  e.printStackTrace();
		 }
	}
	
	
	protected void copyGraphInfo(StrategyImpl strategy){
		 System.out.println("begin copy info");
		 System.out.println("eventSerial count"+strategy.eventSerial.size());
		 for(TState state:strategy.stateGraph.allState){
			 System.out.println("no:"+state.No+"  preTracesize="+state.preTrace.size());
		 }
		 
		 this.stateGraph=strategy.stateGraph;
		 this.eventSerial=strategy.eventSerial;
	}
	
	protected abstract void extendGraph(TEvent event);

}
