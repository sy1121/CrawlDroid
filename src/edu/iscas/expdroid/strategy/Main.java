package edu.iscas.expdroid.strategy;

import java.util.Properties;

import edu.iscas.expdroid.tools.Config;
import edu.iscas.expdroid.tools.Statistic;

public class Main {
    public static void main(String[] args){
    	//get config
    	Config config=Config.g();
        //modify config by cmd params
    	setup(args,config);
    	System.out.println("appid="+config.appid);
    	//Schedule
    	Schedule sche=new Schedule();
    	sche.execute();
    }
    
    private static void setup(String[] args,Config config){
    	Properties commandLineProp=new Properties();
    	String command=null;
    	int i=0;
    	while(i<args.length){
    		String key=args[i];
    		if(key.startsWith("-")){
    			String value=args[i+1];
    			commandLineProp.put(key.substring(1), value);
    			i++;
    		}else if(command==null){
    			command=key;
    		}else {
    			System.out.println("WARN: ignoring commandline argument"+key);
    		}
    		i++;
    	}
    	if(commandLineProp.getProperty("appid")!=null) 
    		config.appid=commandLineProp.getProperty("appid");
    	if(commandLineProp.getProperty("appdir")!=null) 
    		config.appdir=commandLineProp.getProperty("appdir");
    	if(command!=null&&command.equals("e")){
    		if(!Statistic.expExit){
    			Statistic.expExit=true;
    		}else{
    			System.out.println("expDroid not start!");
    		}
    	}
    		
    }
}
