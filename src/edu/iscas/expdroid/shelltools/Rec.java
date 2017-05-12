package edu.iscas.expdroid.shelltools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.iscas.expdroid.exception.MessageNotFoundException;
import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.Statistic;
import edu.iscas.expdroid.utils.FileOper;
import edu.iscas.expdroid.utils.xmlParse.ActivityHandler;
import edu.iscas.expdroid.utils.xmlParse.ParseXML;


public class Rec {
       public  static String testedPackage;//被测应用包名
       public  static String startActivity;   
       public  static String apkPath=""; //apk文件路径
       public  static String prjectPath=""; //工程路径
       
	 public  static boolean initial(){
	    try{
			System.out.println("initializing...");
			Config config=Config.g();
			prjectPath=new File(".").getAbsolutePath();
			apkPath=config.appdir+"/"+config.appid;
			//rebuild and install  troyd
			System.out.println("rebuild and install client...");
			//get package name  of Tested-app ,then reinstall
			testedPackage=AAPT.pkg(apkPath);
			Troyd.reInstallApp(testedPackage,apkPath);
			//start 
			System.out.println("start client ...");
		    String actName=AAPT.launcher(apkPath);
		    System.out.println("start activtiy is "+actName);
		    ADB.ignite(actName);
		    startActivity=actName; // save the first Activity name
		    storeManifest(apkPath);
		   } catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			 System.out.println("initialize failed!");
			 e.printStackTrace();
			 return false;
		   }
		     System.out.println("initialize succeed!");
		     return true;
        }
	   
	   public static void reinstallTargetApp(){
			 Troyd.reInstallApp(testedPackage,apkPath);
	  }
	   
	   
	   private static void storeManifest(String apkPath){
		   try{
		     String AT=new File(".").getAbsolutePath()+"/tools/apktool.jar";
	    	 String dir=System.currentTimeMillis()+"";
	    	 ExceCmd.getInstance().execCommand("java -Djava.awt.headless=true -jar "+AT+" d -f --no-src --keep-broken-res "+apkPath+" -o "+dir);
	    	 String meta=dir+"/AndroidManifest.xml";
	    	 //save file for statistic after explored
	    	 FileOper.createFile(new File(Statistic.outDirPath),false);
	    	 String cp="cp ./"+meta+" "+Statistic.outDirPath;
	    	 ExceCmd.getInstance().execCommand(cp);
	    	 String rm="rm -rf "+dir;
	    	 ExceCmd.getInstance().execCommand(rm);
	    	 getActInfo();
		   }catch(Exception e){
			   e.printStackTrace();
		   }
	   }
	   
	   private static void getActInfo(){
		 	 //caculater all activity by paser androidManifest.xml
	    	 ParseXML paser=new ParseXML();
	    	 paser.init(new ActivityHandler());
	    	 String outDirPath=new File(".").getAbsolutePath()+"/output/"+Rec.testedPackage+"/";
	    	 String xmlFilePath=outDirPath+"/AndroidManifest.xml";
	    	 String targetFile=outDirPath+"acts.txt";
	    	 paser.storeToFile(xmlFilePath, targetFile);
	   }
	    
}
