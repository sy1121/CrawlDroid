package edu.iscas.expdroid.shelltools;

import java.io.IOException;

public class AVD {
    String avd;
    String opt;
     public AVD(String avd,String opt){
    	 this.avd=avd;
    	 this.opt=opt;
     }
     
    static String ADR="android";
    static String AL=ADR+" list";
    
    public  boolean isExists() throws IOException{
    	String s=ExceCmd.getInstance().execCommandForResult(AL+" avd");
    	if(s.contains(avd)) return true;
    	else return false;
    }
    
     public void delete() throws IOException{
    	 ExceCmd.getInstance().execCommand(ADR+" delete avd -n "+avd);
     }

     static String A10=ADR+"-10";
     static String AOPT="-s WQVGA432";
     
     public String create() throws IOException{
    	 delete();
    	 return ExceCmd.getInstance().execCommandForResult(ADR+" create avd -n "+avd+"-t "+A10+" "+AOPT);
     }
     
     static String EM="emulator";
     static String OPT="-cpu-delay 0 -netfast -no-snapshot-save";
     
     public void start() throws IOException{
    	 final String  startcmd=EM+" -avd "+avd+" "+OPT+" "+opt+" ";
    	// System.out.println(startcmd);
	     ExceCmd.getInstance().execCommand(startcmd);
     }
     
     static String ARM=EM+"-arm";
     
     public void stop() throws IOException{
    	 ExceCmd.getInstance().execCommand("pkill "+ARM);  //linux
    	 //ExceCmd.getInstance().execCommand("killall "+ARM); //darwin
    	 //ExceCmd.getInstance().execCommand("taskkill IM  "+ARM+".exe"); //mswin
     }
}
