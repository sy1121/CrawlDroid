package edu.iscas.expdroid.strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.shelltools.ExceCmd;
import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.tools.Statistic;
import edu.iscas.expdroid.utils.FileOper;
import edu.iscas.expdroid.utils.xmlParse.ActivityHandler;
import edu.iscas.expdroid.utils.xmlParse.ParseXML;

public class Extend extends StrategyImpl{
	static String curpath=new File(".").getAbsolutePath();
	static String AT= curpath+"/tools/apktool.jar";
	static boolean timoutFlag=false;
	static final long exploreTime=1*60*1000;
	@Override
	public void startExp() {
		// TODO Auto-generated method stub
		saveManifestFile();
		List<String> can=getCandidates();
		System.out.println("candidate size is "+can.size());
		System.out.println("begin explored out activity");
		for(String act:can){
			timoutFlag=false;
			/*Runnable runnable=new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true){
					    try{
					    	Thread.sleep(1000);
					    }catch(InterruptedException e){
					       e.printStackTrace();
					    }
					}
				}
			};
			Thread thread=new Thread(runnable);
			thread.start();*/
			waitForTimeout timeout=new waitForTimeout();
			timeout.start();
			System.out.println("current activity is "+act);
			exploredFromOut(act);
			System.out.println("finish explored act "+ act);
			System.out.println();
			System.out.println();
		}
	}

	@Override
	protected void extendGraph(TEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	private void saveManifestFile(){
		try{
		 String dir=System.currentTimeMillis()+"";
    	 ExceCmd.getInstance().execCommand("java -Djava.awt.headless=true -jar "+AT+" d -f --no-src --keep-broken-res "+ Rec.apkPath+" -o "+dir);
    	 String meta=dir+"/AndroidManifest.xml";
    	 //save file for statistic after explored
    	 FileOper.createFile(new File(Statistic.outDirPath),false);
    	 String cp="cp ./"+meta+" "+Statistic.outDirPath;
    	 ExceCmd.getInstance().execCommand(cp);
   	     ExceCmd.getInstance().execCommand("rm -rf "+dir);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private List<String> getCandidates(){
		 List<String> rlist=new ArrayList<String>();
		 ParseXML paser=new ParseXML();
    	 paser.init(new ActivityHandler());
    	 String xmlFilePath=Statistic.outDirPath+"/AndroidManifest.xml";
    	 List<String> exploreds=paser.getExploredActivities(xmlFilePath);
    	 System.out.println("explored size is"+exploreds.size());
    	 File file=new File(Statistic.outDirPath+"/ac.txt");
    	 if(!file.exists()) return rlist;
         String allActs=FileOper.readFromFile(file);
         System.out.println("allActs="+ allActs);
         for(String act:exploreds){
        	 System.out.println("current explored act "+act);
        	if(!allActs.contains(act)) rlist.add(act);
         }
         return rlist;
	}
	
	protected void startExploredActivity(String actName){
		try{ 
		  if(actName!=null&&!actName.equals(""))
			  cmd.initial(Rec.testedPackage,Rec.testedPackage+"."+actName);
		  else {
			  System.out.println("error occuer:cannot get laucher activity!");
			  
		  }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void exploredFromOut(String actName){
		startExploredActivity(actName);
		 while(true){  //loop for observe-->select-->execute
		    	curState=ehelper.getCurState();  //observe
		    	//check in target app or restart
		    	if(!isInPackage(curState)) {
		    		 try {
						startExploredActivity(actName);
						curState=ehelper.getCurState();
					} catch (Exception e) {
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
			    		   if(crashOccured){
							/*startExploredActivity(actName);
							curState=ehelper.getCurState();*/
			    			   timoutFlag=true;
			    		   }
			    		}
			    	}else {
			    		System.out.println("select event fail in state "+curState);
			    	}
			    	if(timoutFlag){
			    		break;
			    	}
		    }
		    
	}
	
	private static class waitForTimeout extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
			    try{
			    	Thread.sleep(exploreTime);
			    	timoutFlag=true;
			    }catch(InterruptedException e){
			       e.printStackTrace();
			    }
			}
		}
		
	}
	

}
