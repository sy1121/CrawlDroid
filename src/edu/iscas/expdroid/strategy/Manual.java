package edu.iscas.expdroid.strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;

import edu.iscas.expdroid.model.TEdge;
import edu.iscas.expdroid.model.TEdge.EdgeType;
import edu.iscas.expdroid.shelltools.ExceCmd;
import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.model.TState;
import edu.iscas.expdroid.model.TStatement;
import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.ManualScript;

public class Manual extends StrategyImpl{
	@Override
	public void startExp() {
		// TODO Auto-generated method stub
		//load and resolve manual-script
		try{
	      String manualScriptDir=Config.g().manualScriptPath;
	      System.out.println("manualScriptDir="+manualScriptDir);
	      if(null==manualScriptDir||"".equals(manualScriptDir)) return ;
	      File file=new File(manualScriptDir);
	      String[] fileNames;
	      fileNames=file.list(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".java");
			}
	    	  
	      });
	      System.out.println("fileNames="+fileNames[0]+" length="+fileNames.length);
	      for(int i=0;i<fileNames.length;i++){
	      List<TStatement> statements=paserScript(manualScriptDir+File.separator+fileNames[i]); 
	      System.out.println("name="+fileNames[i]+" statements size="+ statements.size());
          if(statements==null||statements.isEmpty()) return ;
  	      //start initial activity
  	      startLauchActivity();
  	      if(stateGraph==null) //graph not exist
  	         initialGraph();
  	      else{
  	    	 curState=ehelper.getCurState();
  	    	 eventSerial.clear();
  	      }
  	      int index=0; //execute statement index 
  	      while(index<statements.size()){
  	    	TEvent event=new TEvent();
  	    	boolean isSuccess=ManualScript.executeTest(statements.get(index++), curState, event);
  	    	System.out.println("isSuccess="+ isSuccess);
  	    	if(isSuccess){
  	    		eventSerial.add(event);
  	    		extendGraph(event);
  	    	}else{
  	    		System.out.println("execute manual-script failed! fail statement is "+statements.get(index-1));
  	    		break;
  	    	}
  	      }
  	        if(i<(fileNames.length-1)){ //no the last script
  	        	//forcestop-->uninstall-restall
  	        	ExceCmd.getInstance().execCommand("adb shell am force-stop "+Rec.testedPackage);
  			    Rec.reinstallTargetApp();
  	        }
	      }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private List<TStatement> paserScript(String scriptFilePath){
		try{
		      FileInputStream file=new FileInputStream(scriptFilePath);
		      CompilationUnit cu = ManualScript.getCompilationUnit(file);
	          List<TStatement> statements=ManualScript.PaserTestMethod(cu,"test");
	          return statements;
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;
	}

	@Override
	public void monitorCrash() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void extendGraph(TEvent event) {
		// TODO Auto-generated method stub
		 TState cState=ehelper.getCurState();
		 if(isLastState(cState)){
			 System.out.println("old state");
		 }else{
		   if(isNewState(cState)){
			 System.out.println("new  state");
			 cState.No=stateGraph.allState.size();
			 cState.preTrace.addAll(new ArrayList<TEvent>(curState.preTrace));
			 cState.preTrace.addAll(new ArrayList<TEvent>(eventSerial));
			 stateGraph.allState.add(cState);
			 TEdge edge=new TEdge(EdgeType.auto,cState,new ArrayList<TEvent>(eventSerial));
			 edge.edgeType=EdgeType.manual;
			 curState.edges.add(edge);
			 curState=cState;
			 //collect state of activityInfo later
			 
		   }else{  //state have been explored before 
			   //got it from graph
			 TState ccState=searchState(stateGraph,cState);
			 if(ccState==null) return ;
			 TEdge edge=new TEdge(EdgeType.auto,ccState,new ArrayList<TEvent>(eventSerial));
			 edge.edgeType=EdgeType.manual;
			 curState.edges.add(edge);
			 curState=ccState;
		   }
		    eventSerial.clear();
		} 
	}
	
	
}
