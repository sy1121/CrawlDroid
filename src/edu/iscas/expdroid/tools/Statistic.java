package edu.iscas.expdroid.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.iscas.expdroid.shelltools.ADB;
import edu.iscas.expdroid.shelltools.Cmd;
import edu.iscas.expdroid.shelltools.ExceCmd;
import edu.iscas.expdroid.shelltools.Rec;
import edu.iscas.expdroid.strategy.Extend;
import edu.iscas.expdroid.strategy.Strategy;
import edu.iscas.expdroid.utils.FileOper;
import edu.iscas.expdroid.utils.UtilE;

public class Statistic {
	 public static Strategy strategy;
     public static long startExpTime;
     public static volatile boolean expExit=false;
     private static ScheduledExecutorService statService;
     private static ScheduledExecutorService timeoutService;
     private static Cmd cmd=new Cmd();
     public static String outDirPath=new File(".").getAbsolutePath()+File.separator+"output"+File.separator+Rec.testedPackage+File.separator;
     private static String allActs="";
     
     //emma coverage
     private static final String sourcecode="/home/sheyi/emmacoverage/FDroid-source"; 
	  private static final String coverageService="/home/sheyi/emmacoverage/edu";
	  private static final String resultdir="/home/sheyi/emmacoverage/coverager";
	  private static final String emmajar="/home/sheyi/android-sdk-linux/sdk/tools/lib/emma.jar";

     public static void start(){
    	 startExpTime=System.currentTimeMillis();
    	// deleteDeviceFiles(); //delete device files remain by last explored
    	 FileOper.createFile(new File(outDirPath), false);
    	 scheduleStatistic();
    	 waitTimeOut(); //wait the explore time expired
    	 
     }
     
     private static void setExitFlag(){
    	 expExit=true;
     }
     
     public static void end(){
    	 try{
    	// expExit=true;
    	 //the order is important ,do not modify
    	 if(Config.g().doExtendExp){
         Extend extend=new Extend();
         extend.startExp();
    	 }
    	 //1.close statService
    	 statService.shutdown();
    	 //2.output state-graph and statistic infos
    	 statisticAndOutput();
    	 //3
    	 timeoutService.shutdown();
    	//4.then stop test app 
    	 cmd.exit();
    	 ExceCmd.getInstance().execCommand(ADB.acmd+" shell am force-stop "+Rec.testedPackage); 
    	 ADB.uninstall(Rec.testedPackage);
    	 deleteDeviceFiles(); 
    	 //5.close socket for crash monitor 5050
    	 closeSocket();
    	  if(!statService.isTerminated()) statService.shutdownNow();
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }finally{
    		 System.exit(0);
    	 }
     }
     
     /**
      * 
      * @return the time from start explore to now  (TimeUnit.SECONDS)
      */
     public static long getExpiredTime(){
    	 long now=System.currentTimeMillis();
    	 return (now-startExpTime)/1000;
     }
     public static String getExpiredTimeStr(){
    	 long cost=getExpiredTime();
    	 return "expored Expired Time: "+(cost/3600==0?"":(cost/3600)+"h")+(cost%3600)/60+"m";
     }
     
     private static void scheduleStatistic(){
    	 StatTask stask=new StatTask();
    	 long delay=Config.g().intevalPeriod;
    	 statService=Executors.newSingleThreadScheduledExecutor();
    	 statService.scheduleWithFixedDelay(stask,delay,delay,TimeUnit.MINUTES);
     }
     
     private static void waitTimeOut(){
    	 TimeOutTask ttask=new TimeOutTask();
    	 int delay=Config.g().exploreMaxTime;
    	 timeoutService=Executors.newSingleThreadScheduledExecutor();
    	 timeoutService.schedule(ttask, delay, TimeUnit.MINUTES);
     }
     
     
     private static void statisticAndOutput() throws IOException{
    	 //build dot-file script  and output graph pic  
    	 System.out.println("strategy is "+Config.g().exploreType);
    	 if(Config.g().exploreType==Config.BFS||Config.g().exploreType==Config.Heuristic){
    	  ScriptHelper.produceGraphScript(strategy.obtainGraph());
    	  String picpath=outDirPath+"test.jpg";
    	  ExceCmd.getInstance().execCommand("dot -Tjpg "+ScriptHelper.DOT_FILE_PATH+" -o "+picpath);
    	 }
    	 //pull file from Device : crashlog screenshot ... 
    	 pullFileFromDevice();
    	 
    	 System.out.println("explore finished at "+ UtilE.getCurTime()+" \n"+getExpiredTimeStr());
    	 
     }
     
     private static void pullFileFromDevice(){
	     try {
    	 //screenshot   this path may trigger a bug , for the path in various api of devices may different
	     String shotpath="/sdcard/expDroid/screenshot";
	     if(!isEmptyInDevice(shotpath)){
    	 String screenshot=outDirPath;//+"screenshot";
    	 FileOper.createFile(new File(screenshot), false);
    	 String spull=ADB.acmd+" pull /sdcard/expDroid/screenshot/ "+screenshot+"/";
	     ExceCmd.getInstance().execCommand(spull);
	     }
		//crash-log
	     String crashpath="/sdcard/crash/";
	     if(!isEmptyInDevice(crashpath)){
	     String crashlog=outDirPath+"crashLog";
	     String cpull=ADB.acmd+"pull /sdcard/crash/"+Rec.testedPackage+"/ "+crashlog;
	     ExceCmd.getInstance().execCommand(cpull);
	     }
	     } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     private static void closeSocket(){
    	 try {
			ExceCmd.getInstance().execCommand(new File(".").getAbsolutePath()+"/tools/kp.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     private static void deleteDeviceFiles(){
    	 try {
    		System.out.println("delete file in devices begin");
    		String crash="/sdcard/crash";
    		String expDriod="/sdcard/expDroid";
    		if(!isEmptyInDevice(crash))
			ExceCmd.getInstance().execCommand(ADB.acmd+" shell rm -r /sdcard/crash");
			if(!isEmptyInDevice(expDriod))
    		ExceCmd.getInstance().execCommand(ADB.acmd+" shell rm -r /sdcard/expDroid/*");
    		System.out.println("delete file in devices end");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     private static boolean isEmptyInDevice(String filePath){
    		String check=ADB.acmd+" shell ls "+filePath;
			String r=	ExceCmd.getInstance().execCommandForResult(check);
			if(r.isEmpty()||r.contains("No such file or directory"))
				return true;
    	    return false;
    	 
     }
     
     private static void collectActCovInfo(){
    	 try{
    	    //activity covergae Infos
     	    String pull=ADB.acmd+" pull /sdcard/expDroid/act.txt "+outDirPath;
			ExceCmd.getInstance().execCommand(pull);
			String acts=FileOper.readFromFile(new File(outDirPath+"act.txt"));
			StringBuffer sb=new StringBuffer();
			String[] names=acts.split("-");
			sb.append(System.getProperty("line.separator", "/n"));
			sb.append(UtilE.getCurTime()).append(System.getProperty("line.separator", "/n"));
			if(allActs==null||"".equals(allActs))
				allActs=getActsInfo();
			int actCount=0;
			StringBuilder actStr=new StringBuilder("");
			for(String name:names){
				if(!name.trim().equals("")&&(allActs.contains(name)||allActs.contains(name.substring(name.lastIndexOf('.')+1)))){
			      actStr.append(name+" ");
				  actCount++;
				}
			}
			sb.append("explored activities count:"+actCount+System.getProperty("line.separator", "/n"));
			sb.append(actStr.toString());
			sb.append(System.getProperty("line.separator", "/n"));
			String statFilePath=outDirPath+"actr.txt";
			FileOper.createFile(new File(statFilePath),true);
			FileOper.appendStringToFile(statFilePath, sb.toString());
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }
     }
    
     
     private static void collectMethCovInfo(){
    	 //method coverage Info 
    	 try{
    		 String mcpath="/sdcard/expDroid/mc.txt";
    		 if(!isEmptyInDevice(mcpath)){
    		 String pull=ADB.acmd+" pull "+mcpath+" "+outDirPath;
    		 int r=ExceCmd.getInstance().execCommand(pull,new StringBuffer());
    		 if(r==0){ //cp success
    			 Set<Integer> mset=new HashSet<Integer>();
    			 File cfile=new File(outDirPath+"mc.txt");
    			 if(cfile.exists()){
    				BufferedReader reader = new BufferedReader(new FileReader(cfile));
		            String methid = null;
		            while ((methid = reader.readLine()) != null) {
		            	if(!methid.isEmpty()){
		            		mset.add(Integer.parseInt(methid));
		            	}
		            }
		            reader.close();
		        	StringBuffer sb=new StringBuffer();
		            sb.append(System.getProperty("line.separator", "/n"));
					sb.append(UtilE.getCurTime()).append(System.getProperty("line.separator", "/n"));
					sb.append("explored methods count:"+(mset.size())+System.getProperty("line.separator", "/n"));
					sb.append(System.getProperty("line.separator", "/n"));
					String statFilePath=outDirPath+"methr.txt";
					FileOper.createFile(new File(statFilePath),true);
					FileOper.appendStringToFile(statFilePath, sb.toString());
    			 }else{
    				 System.out.println("method coverage files not find!");
    			 }
    		 }
    		 }
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }
     }
     
     private static void collectStatementsCovInfo(){
    	  BufferedReader reader = null;  
			try{
		    System.out.println("统计时间点："+ UtilE.getCurTime());
			//收集数据
			String collect="adb shell am startservice -n "+Rec.testedPackage+"/edu.iscas.codecoverage.CollectService";
		    ExceCmd.getInstance().execCommand(collect);
		    //从设备上拉取覆盖率信息
		    String ecName=UtilE.getCurTime()+"-"+System.currentTimeMillis()+"-coverage.ec";
		    String targetName=resultdir+File.separator+Rec.testedPackage+File.separator;
		    String pull="adb pull /mnt/sdcard/coverage.ec "+targetName;
		    ExceCmd.getInstance().execCommand(pull);
		    String delete="adb shell rm  /mnt/sdcard/coverage.ec";
		    ExceCmd.getInstance().execCommand(pull);
		    String oldFile=resultdir+File.separator+Rec.testedPackage+File.separator+"coverage-old.ec";
		    System.out.println("filepath="+oldFile);
		    File file=new File(oldFile);
		    if(file.exists()){
		    	String merge="java -cp "+emmajar+" emma merge -input coverage-old.ec,coverage.ec -out coverage-old.ec";
		    	ExceCmd.getInstance().execCommand(new String[]{"/bin/sh","-c","cd "+targetName+"; "+merge});
		        System.out.println("exist");
		    }else{
		    	String merge="java -cp "+emmajar+" emma merge -input coverage.ec -out coverage-old.ec";
		    	ExceCmd.getInstance().execCommand(new String[]{"/bin/sh","-c","cd "+targetName+"; "+merge});
		    	System.out.println("first time");
		    }
			String out="java -cp "+emmajar+" emma report -r txt -in coverage.em,coverage.ec";
	    	ExceCmd.getInstance().execCommand(new String[]{"/bin/sh","-c","cd "+targetName+"; "+out});
	        Thread.sleep(2000); //等待生成coverage.txt文件
	    	//把结果写入文件
	    	String resultFilePath=resultdir+File.separator+Rec.testedPackage+File.separator+"record.txt";
	        File rfile=new File(resultFilePath);
	    	if(!rfile.exists()) FileOper.createFile(rfile,true);
	    	//读取coverage.txt中的第6行
	    	  File cfile = new File(resultdir+File.separator+Rec.testedPackage+File.separator+"coverage.txt");  
	          FileOper.createFile(cfile, true);
	    	  reader = new BufferedReader(new FileReader(cfile));  
	          String tempString = null;  
	          StringBuilder sb=new StringBuilder("");
	          sb.append(UtilE.getCurTime()+" ");
	            int line = 0;  
	            // 一次读入一行，直到读入null为文件结束  
	            while ((tempString = reader.readLine()) != null) {  
	                line++;  
	                if(line==6) {
	                	sb.append(tempString+"\n");
	                	break;
	                } 
	            }  
	            reader.close();
	            FileOper.appendStringToFile(resultFilePath, sb.toString());
		    //清除内存中的覆盖率信息
		    
			}catch(Exception e){
				e.printStackTrace();
			}finally {  
	            if (reader != null) {  
	                try {  
	                    reader.close();  
	                } catch (IOException e1) {  
	                }  
	            }  
	        }  
		}
     
     
     private static String getActsInfo(){
    	 String path=outDirPath+"acts.txt";
    	 File actsFile=new File(path);
    	 if(!actsFile.exists()) return "no";
    	 return FileOper.readFromFile(actsFile);
     }
     
     
     private static class StatTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			collectActCovInfo();
			collectMethCovInfo();
			collectStatementsCovInfo();
		}
    	 
     }
     
     private static class TimeOutTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			setExitFlag();
		}
    	 
     }
}
