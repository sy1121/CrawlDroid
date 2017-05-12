package edu.iscas.expdroid.shelltools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iscas.expdroid.exception.MessageNotFoundException;
import edu.iscas.expdroid.utils.TPair;


public class Cmd {
	//start uiautomator oper
	public void initial(String packageName,String actName) {
	   Map<String,String> map=new HashMap<String,String>();
	    map.put("packageName", packageName);
      	map.put("act", actName);
        try {
			ADB.cmd("initial", map);
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//dumpWindowHierarchy
	public boolean dump() {
		Map<String,String> map=new HashMap<String,String>();
        try {
			String msg=ADB.cmd("dump", map);
			return !msg.isEmpty();
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	
	//exit test-app
	public void exit(){
		Map<String,String> map=new HashMap<String,String>();
        try {
			ADB.cmd("exit", map);
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ui event Info: action and select-params
	public boolean sendUiEventInfo(List<TPair> infos){
	  	Map<String,String> map=new HashMap<String,String>();
	    for(TPair pair:infos){
	       map.put(pair.key, pair.value);
	    }
	    try {
			String rstr=ADB.cmd("sendUiEventInfo", map);
			if(rstr.contains("SUCCESS")) return true;
			else return false;
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//press back
	public boolean goBack(){
		Map<String,String> map=new HashMap<String,String>();
        try {
			String eres=ADB.cmd("goBack", map);
			if(eres.contains("SUCCESS")) return true;
			else return false;
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//takeScreenshot
	public void takeScreenshot(String picName){
		Map<String,String> map=new HashMap<String,String>();
        try {
         	map.put("picName", picName);
			ADB.cmd_notSync("takeScreenshot", map);
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//rotate
	public boolean  rotate(){
		Map<String,String> map=new HashMap<String,String>();
        try {
			String eres=ADB.cmd("rotate", map);
			if(eres.contains("SUCCESS")) return true;
			else return false;
		} catch (IOException | InterruptedException | MessageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//swipeLeft
		public boolean  swipeLeft(){
			Map<String,String> map=new HashMap<String,String>();
	        try {
				String eres=ADB.cmd("swipeLeft", map);
				if(eres.contains("SUCCESS")) return true;
				else return false;
			} catch (IOException | InterruptedException | MessageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public boolean  swipeRight(){
			Map<String,String> map=new HashMap<String,String>();
	        try {
				String eres=ADB.cmd("swipeRight", map);
				if(eres.contains("SUCCESS")) return true;
				else return false;
			} catch (IOException | InterruptedException | MessageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		public boolean  swipeUp(){
			Map<String,String> map=new HashMap<String,String>();
	        try {
				String eres=ADB.cmd("swipeUp", map);
				if(eres.contains("SUCCESS")) return true;
				else return false;
			} catch (IOException | InterruptedException | MessageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		public boolean  swipeDown(){
			Map<String,String> map=new HashMap<String,String>();
	        try {
				String eres=ADB.cmd("swipeDown", map);
				if(eres.contains("SUCCESS")) return true;
				else return false;
			} catch (IOException | InterruptedException | MessageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public boolean  clickRamdomPoint(){
			Map<String,String> map=new HashMap<String,String>();
	        try {
				String eres=ADB.cmd("clickRamdomPoint", map);
				if(eres.contains("SUCCESS")) return true;
				else return false;
			} catch (IOException | InterruptedException | MessageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public boolean  pressContexMenu(){
			Map<String,String> map=new HashMap<String,String>();
	        try {
				String eres=ADB.cmd("pressContexMenu", map);
				if(eres.contains("SUCCESS")) return true;
				else return false;
			} catch (IOException | InterruptedException | MessageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
}
