package edu.iscas.expdroid.strategy;

import java.io.IOException;

import edu.iscas.expdroid.exception.MessageNotFoundException;
import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.shelltools.ADB;
import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.Statistic;

public class RandomE extends StrategyImpl{

	@Override
	public void startExp() {
		// TODO Auto-generated method stub
		if(Config.g().doManualExp){
			 Strategy manual=new Manual();
			 manual.startExp();
		}
		//do Random
	    if(null==curState){  //no explore has been done before
	    	//start initial activity
	    	startLauchActivity();
	    }
	    
	    while(true){  //loop for observe-->select-->execute
	    	curState=ehelper.getCurState();  //observe
	    	//check in target app or restart
	    	if(!isInPackage(curState)) {
	    		 try {
					ADB.ignite("");
					startLauchActivity();
					curState=ehelper.getCurState();
				} catch (IOException | InterruptedException | MessageNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	
	    	TEvent event=ehelper.selectRandomEvent(curState); //select
	    	if(null!=event){
		    	   boolean r=ehelper.executeEvent(event); //execute
		    	   if(!r) {
		    		   System.out.println("trigger event fail! event:"+event);
		    	   }else{
		    		   if(crashOccured)
		    	          restartFromCrash();
		    	   }
		    	}else {
		    		System.out.println("select event fail in state "+curState);
		    	}
		    	if(Statistic.expExit){
		    		Statistic.end();   //monitor exit signer 
		    		break;
		    	}
	    }
	    
	    
	}

	@Override
	protected void extendGraph(TEvent event) {
		// TODO Auto-generated method stub
		
	}

    
}
