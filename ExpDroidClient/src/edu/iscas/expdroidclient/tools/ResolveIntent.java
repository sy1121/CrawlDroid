package edu.iscas.expdroidclient.tools;

import android.content.Intent;
import android.util.Log;
import edu.iscas.expdroidclient.Troy;

public class ResolveIntent {
	 private static final String TAG=ResolveIntent.class.getSimpleName();
     public static EventInfo resolve(Intent intent){
    	 EventInfo eInfo=new EventInfo();
    	 try{
    	 eInfo.cmd=intent.getExtras().getString("cmd");
    	 if(eInfo.cmd!=null&&eInfo.cmd.equals("sendUiEventInfo")){
    		 eInfo.inputValue=intent.getExtras().getString("inputValue"); 
    		 return buildUiEventInfo(intent,eInfo);
    	 }
    	 
    	 eInfo.packageName=intent.getExtras().getString("packageName");//被测应用包名
    	 eInfo.act=intent.getExtras().getString("act");
    	 eInfo.picName=intent.getExtras().getString("picName");
    	 }catch(Exception e){
    		 System.out.println(e.getMessage());
    		 return null;
    	 }
    	 return eInfo;
     }
     
     private static EventInfo buildUiEventInfo(Intent intent,EventInfo eInfo){
    	 eInfo.ucmd=intent.getExtras().getString("ucmd");
    	 try{
    		  Log.i(TAG, "resource-id "+intent.getExtras().getString("resource-id"));  
    	 if(null!=intent.getExtras().getString("resource-id")) {
    		 eInfo.cmd="byResourceId";
    		 eInfo.resourceId=intent.getExtras().getString("resource-id"); 
    	 }else if(null!= intent.getExtras().getString("content-desc")){
    		 eInfo.cmd="byContentDes";
    		 eInfo.contentDesc=intent.getExtras().getString("content-desc");
    	 }else if(null!= intent.getExtras().getString("text")) {
    		 eInfo.cmd="byText";
    		 eInfo.text=intent.getExtras().getString("text");
    	 }else if(null!=eInfo.parentNclass){    
    		 eInfo.cmd="fromParentWithText";
    		 eInfo.nclass=intent.getExtras().getString("class");
    		 eInfo.parentNclass=intent.getExtras().getString("parent-class");
    		 eInfo.text=intent.getExtras().getString("text");
    	     eInfo.instance=Integer.parseInt(intent.getExtras().getString("instance")); 
    	 }else{
    		 eInfo.cmd="byXPath";
    		 eInfo.xpath=intent.getExtras().getString("xpath");
    	 }
    	 }catch(Exception e){
    		 System.out.println(e.getMessage());
    	 }
    	 return eInfo;
     }
}
