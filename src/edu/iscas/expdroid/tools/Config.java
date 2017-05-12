package edu.iscas.expdroid.tools;

import java.util.Properties;

import edu.iscas.expdroid.exception.ConfigParamInvalidException;
import edu.iscas.expdroid.utils.UtilE;

public class Config {
        public static final int  BFS=1;
        public static final int  RandomE=2;
        public static final int Heuristic=3;
        public String appdir;
        public String appid;
        public String elladir;
        public String manualScriptPath;//存放人工测试脚本的文件
        public int  exploreType;
        public boolean doManualExp;
        public boolean doExtendExp;
        public float similarRadio;
        public int  exploreMaxTime;  //单位分钟
        public long  intevalPeriod; //统计时间间隔
        public String avdName;
        public Properties preDefineInput; 
        private static Config g;
        //non-ui event
        public boolean swipe;
        public boolean rotate;
        public boolean goBack;
        public  static Config g(){
        	if(g==null){
        		g=new Config();
        	}
              return g;
        }
        
        private Config(){
        	   String  autoTestSettings="./config/autorun.settings";  
        	   Properties props=UtilE.loadConfigFile(autoTestSettings);
        	   load(props);
        	   //load pre-defined EditText value
        	   String predefinedEditText="./config/EditText.properties";
        	   preDefineInput=UtilE.loadConfigFile(predefinedEditText);
        }
        
        void load(Properties props){
        	    appdir=props.getProperty("autotest.appdir");
        	    appid=props.getProperty("autotest.appid");
        	    elladir=props.getProperty("autotest.ellaDir");
        	    manualScriptPath=props.getProperty("autotest.manualScript");
        	    avdName=props.getProperty("autotest.avdName","");
        	    System.out.println("avdName="+avdName);
        	   try{
        		     String exporeType=props.getProperty("autotest.exploreType");
        		     exploreType=getExploreType(exporeType);
        		     String maxTime=props.getProperty("autotest.exploreMaxTime");
        		     exploreMaxTime=Integer.parseInt(maxTime);
        		     String inteval=props.getProperty("autotest.intevalPeriod","10");
        		     intevalPeriod=Long.parseLong(inteval);
        		     String doManualTest=props.getProperty("autotest.doManualExp","false");
        		     doManualExp=Boolean.parseBoolean(doManualTest);
        		     String doExtendTest=props.getProperty("autotest.extendExp","false");
        		     doExtendExp=Boolean.parseBoolean(doExtendTest);
        		     String similar=props.getProperty("autotest.similarRatio","1.0");
        		     similarRadio=Float.parseFloat(similar);
        		     //non-ui events
        		     swipe=Boolean.parseBoolean(props.getProperty("nonui.swipe","false"));
        		     rotate=Boolean.parseBoolean(props.getProperty("nonui.rotate","true"));
        		     goBack=Boolean.parseBoolean(props.getProperty("nonui.goBack","false"));
        		    
        	   }catch(Exception e){
        		   System.out.println("config param not illegal,please check config file");   
        		   try {
					throw new ConfigParamInvalidException("config param invalid,please checked config file!");
				} catch (ConfigParamInvalidException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	   }
        	   
        }
        
        
        private int getExploreType(String type){
        	int result=BFS;          
        	switch(type){
        	          case "1": result=BFS;break;
        	          case "2": result=RandomE;break;
        	          case "3": result=Heuristic;break;
        	          default:break;
        	          }
        	return result;
        }
                
}
