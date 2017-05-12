package edu.iscas.expdroid.shelltools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AAPT {
     public static String pkg(String apk) throws IOException{
    	  List<String> rlist=badg(apk,"package","name='\\S+'");
    	  if(rlist!=null&&!rlist.isEmpty()){
    		  String pair=rlist.get(0);
    		  return  pair.substring(pair.indexOf('\'')+1, pair.lastIndexOf('\''));
    	  }
    	  return null;
     }
     
     public static String  launcher(String apk) throws IOException{
    	 List<String> rlist=badg(apk,"launchable","name='\\S+'\\s*label=");
    		  if(rlist!=null&&!rlist.isEmpty()){
    			  String pair=rlist.get(0);
        		  return  pair.substring(pair.indexOf('\'')+1, pair.lastIndexOf('\''));
        	  }
        	  return null;
    	   
     }
     
     public static List<String>  perm(String apk) throws IOException{
    	 List<String> rlist=badg(apk,"permission","permission:'\\S+'");
    	 return rlist;
     }
     
     private  static List<String> badg(String apk,String what,String pattern) throws IOException{
    	 List<String> rlist=new ArrayList<String>();
    	 String cmd="aapt dump badging "+ apk + " | grep "+what;
    	 StringBuffer rstring=new StringBuffer();
    	 int r=ExceCmd.getInstance().execCommand(cmd,rstring);
    	 if(r==0){
    		 Pattern p=Pattern.compile(pattern); 
    		 Matcher m=p.matcher(rstring.toString());
    		 while(m.find()){ 
    			 rlist.add(m.group());
    		 } 
    	 }else{
    		 System.out.println("error occur in exce cmd "+ cmd);
    	 }
    	 return rlist;
    	 
     }
}
