package edu.iscas.expdroid.shelltools;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iscas.expdroid.exception.MessageNotFoundException;
import edu.iscas.expdroid.strategy.StrategyImpl;
import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.Statistic;
import edu.iscas.expdroid.utils.UtilE;

public class ADB {
    public static String acmd="adb "+(Config.g().avdName.equals("")?"":"-s "+Config.g().avdName);
    public static String lcat=acmd+" logcat";
    public static final String ACTM="ActivityManager";
    public static  String altr=lcat+" -d "+ACTM+":D *:S";
    public static final String PKG="edu.iscas.expdroidclient";
    public static  String fltr=lcat+" -d "+PKG+":D *:S";
    public static final String RUN=" broadcast -a android.intent.action.RUN";
    public static  String am=acmd+" shell am";
    public static  String run=am+RUN;
    public static int TO=1000;
    public static int maxWaitTime=5*TO;
	public void restart() throws IOException{
    	 String killcmd="adb kill-server";
         String startcmd="adb start-server";
    	 StringBuffer sb=new StringBuffer();
    	 ExceCmd.getInstance().getInstance().execCommand(killcmd, sb);
    	 ExceCmd.getInstance().getInstance().execCommand(startcmd, sb);
     }
	
	
    public static  void device(String serial){
        acmd="adb -s "+ serial;
        lcat=acmd+" logcat";
   //   altr=lcat+" -d "+ACTM+":D *:S";
   //   fltr=lcat+" -d "+PKG+":D *:S";
        am=acmd+" shell am";
        run=am+RUN;
    }
    
    public static int[] devices() throws IOException{
    	 int dv_cnt=0,em_cnt=0;
    	String  dvs="adb devices";
        StringBuffer sb=new StringBuffer();
    	int r=ExceCmd.getInstance().execCommand(dvs,sb);
    	System.out.println(sb.toString().trim());
    	String res=sb.toString().trim();
        if(r==0){
            Pattern dvp=Pattern.compile(".+device\\s*");
            Pattern emp=Pattern.compile("emulator-\\d+\\s+device\\s*");
            Matcher dvm=dvp.matcher(res);
            Matcher emm=emp.matcher(res);
            boolean finddv=dvm.find();
            while(finddv){
            	dv_cnt++;
            	finddv=dvm.find();
            }
            boolean findem=emm.find();
            while(findem){
                em_cnt++;
                findem=emm.find();
            }
        }
       int[] result={dv_cnt,em_cnt};
       return result;
    }
    
    public static boolean online() throws IOException, InterruptedException{
        int[] cnt=devices();
        if(cnt[0]==0){
        	return false;
        }else return true;
       
    }
    
    static String  SUCC="Success";
    static String  FAIL="Failure";
    
    public static String  uninstall(String pkg){
    	 if(null!=pkg&&!pkg.isEmpty())
         return  sync_msg(acmd+" uninstall "+pkg,new String[]{SUCC,FAIL});
    	 else return  sync_msg(acmd+" uninstall "+PKG,new String[]{SUCC,FAIL});
    }
    
    public static  String install(String apk){
    	 return  sync_msg(acmd+" install  "+apk,new String[]{SUCC});
    }
    
    public void instAll(String dir) throws IOException, InterruptedException{
    	 File files=new File(dir);
    	 for(File f:files.listFiles()){
    		 if(f.getName().toLowerCase().endsWith(".apk")){
    			 uninstall(AAPT.pkg(f.getAbsolutePath().toString()));
    			 install(AAPT.pkg(f.getAbsolutePath().toString()));
    		 }
    	 }
    }
    
    public static void ignite(String act) throws IOException, InterruptedException, MessageNotFoundException{
    	//sync_logcat(am+" startservice -n "+PKG+"/.Ignite -e AUT "+act,fltr);
      //  sync_logcat(am+" instrument -w umd.troyd/.Troy",fltr);
    	 runcmd(lcat+" -c");
         String cmd=am+" instrument -w edu.iscas.expdroidclient/.Troy";
   	     ExceCmd.getInstance().execCommandExitVlaue(cmd);
	   	 String out="";
		 int count=0;
	   	 while(out.isEmpty()){
	   		 out=runcmd(lcat+" -d Troy:I *:S");
	   		 if(out.contains("Finished"))
	   			 return ;
	   		 else{
	   			 out="";
	   			 Thread.sleep(TO);
	   			 count++;
	   		 }
	   		 if(count>=30){
	   			 throw new MessageNotFoundException("can't found target message,Timeout!");
	   		 }
	   	 }
    }
    
    public static String cmd_notSync(String cmd,Map<String,String> map) throws IOException, InterruptedException, MessageNotFoundException{
    	String ext="";
    	if(map!=null&&!map.isEmpty()){
    	for (String key : map.keySet()) {
    		  ext+=" -e "+ key + " \"" + map.get(key)+"\"";
        }
    	}
    	return runcmd(run+" -e cmd "+cmd+ext);
    }
    
    public static String cmd(String cmd,Map<String,String> map) throws IOException, InterruptedException, MessageNotFoundException{
    	String ext="";
    	if(map!=null&&!map.isEmpty()){
    	for (String key : map.keySet()) {
    		  ext+=" -e "+ key + " \"" + map.get(key)+"\"";
       }
    	}
    	return sync_logcat(run+" -e cmd "+cmd+ext);
    }
    
    public static String sync_logcat(String cmd) throws IOException, InterruptedException, MessageNotFoundException{

		runcmd(lcat+" -c");
		 runcmd(cmd);
		 Thread.sleep(1000); //wait logcat print
		 String out="";
		 int count=0;
		  while(out.isEmpty()){
		     out=runcmd(lcat+" -d Troy:I *:S");
		     if(out.contains("Action Finished"))
			   return "SUCCESS";
		     else{
			   out="";
			   Thread.sleep(TO);
			   count++;
		     }
		    if(count>=10){
		      out="FAIL";
			  throw new MessageNotFoundException("can't found target message,Timeout!");
			 }
		 }
    	/* ExceCmd.getInstance().execCommandExitVlaue(cmd);
    	 String out="";
    	 if(UtilE.waitForSocketMsg("")){
    		 out="SUCCESS";
    	 }else{
    		 out="FAIL";
    		 throw new MessageNotFoundException("can't found target message,Timeout!");
    	 }*/
       	 return out;
        
    }
    
    
    private static String sync_msg(String cmd,String[] msgs){
         String out="";
         int waitTime=0;
         try{
         while(out.isEmpty()){
        	 out=runcmd(cmd);
        	 for(int i=0;i<msgs.length;i++){
        		 if(out.contains(msgs[i]))
        			 return msgs[i];
        	 }
        	 out="";
        	 Thread.sleep(TO);
        	 waitTime+=TO;
        	 if(waitTime>=maxWaitTime){
        		out="Failure";
        		 break;
        	 }
         }
         }catch(Exception e){
        	e.printStackTrace();
         }
		return out;
    }
    
    public static String  runcmd(String cmd){
       StringBuffer sb=new StringBuffer();
       if(cmd!=null&&!cmd.isEmpty()){
    	   ExceCmd.getInstance().execCommand(cmd, sb);
       }
       return sb.toString();
    }
}
