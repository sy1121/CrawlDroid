package edu.iscas.expdroid.strategy;


import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.Statistic;

public class Schedule {
	 private Strategy strategy=null;
     public void execute(){
    	 //install client & tested-app and start instrumentation
    	  boolean r=Rec.initial();
    	  if(!r) return ;
    	  switch(Config.g().exploreType){
    	  case Config.BFS:
    		  strategy=new BFS();
    		  break;
    	  case Config.RandomE:
    		  strategy=new RandomE();
    		  break;
    	  case Config.Heuristic:
    		  strategy=new Heuristic(); 
    		  break;
    	  }
    	  //begin Ｅxplore
    	  System.out.println("Explore start ...");
    	  new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				strategy.startExp();
			}
    		  
    	  },"exploreThread").start();
    	  
    	  //start statistics
    	  new Thread(new Runnable(){

  			@Override
  			public void run() {
  				// TODO Auto-generated method stub
  				Statistic.strategy=strategy;
  				Statistic.start();
  			}
      		  
      	  },"statisticThread").start();
    	  //start monitor crash in target app
    	  new Thread(new Runnable(){

    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				strategy.monitorCrash();
    			}
        		  
        	  },"nonitorCrashThread").start();
     }
}
