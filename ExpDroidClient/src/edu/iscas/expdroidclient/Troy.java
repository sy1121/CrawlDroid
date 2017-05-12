package edu.iscas.expdroidclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.robotium.solo.Solo;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import edu.iscas.expdroidclient.tools.CrashHandler;
import edu.iscas.expdroidclient.tools.EventInfo;
import edu.iscas.expdroidclient.tools.ResolveIntent;
import edu.iscas.expdroidclient.tools.UtilEC;

public class Troy extends Instrumentation{
    private static final String TAG=Troy.class.getSimpleName();
    private static volatile boolean exit;
    private static final String sdcard_path=Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String UIDUMP_DEVICE_PATH=sdcard_path+"/expDroid/uidump.xml";
    private static final String ACTIVTY_COVERAGE_PATH=sdcard_path+"/expDroid/act.txt";
    private static final String CurrentActvityName_PATH=sdcard_path+"/expDroid/an.txt";
    private static final String SCREEN_SHOT_PATH="sdcard/expDroid/screenshot";
    private static Set<String> actSet=new HashSet<String>();
    private static ExecutorService singleThreadExecutor=Executors.newSingleThreadExecutor(); 
    private UiDevice mDevice;
    private Solo solo;
    private static String pName="";
    
	@Override
	public void onCreate(Bundle arguments) {
		// TODO Auto-generated method stub
		super.onCreate(arguments);
		Log.i(TAG,"troy oncreate");
		CrashHandler crashHandler=CrashHandler.getInstance();
	    crashHandler.init(getTargetContext());
		exit=false;
		start(); //call onStart
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG,"troy onStart");
		// initial mDevice
		mDevice=UiDevice.getInstance(this);
		//register broadcast receiver
		Log.i(TAG,"before receive");
		/*final Intent intent=getContext().getPackageManager()
    			.getLaunchIntentForPackage("org.liberty.android.fantastischmemo");
    	//clear out any previous instances
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    	getContext().startActivity(intent);*/
		getTargetContext().registerReceiver(receiver, new IntentFilter(Intent.ACTION_RUN));
		Log.i(TAG,"troy start Finished");
		//createDir();
	    //looping
		while(true){
			try{
				if(exit) break;
				Thread.sleep(20742);
				Log.e(TAG,"troy is running!");
			}catch(InterruptedException e){
				Log.e(TAG, e.toString());
			}
		}
    //	 if this finished, broadcast receiver will start on a new process
	//	 that is, static var. solo, the ultimate controller, would be lost
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		getTargetContext().unregisterReceiver(receiver);
		
	}
	
	private String inputValue="AutoTest"; //set the default input-value for EditText
    enum Command {
		initial,dump,exit,
		byResourceId,byContentDes,byText,fromParentWithText,byXPath,
		goBack,takeScreenshot,rotate,swipeLeft,swipeRight,swipeUp,swipeDown,
		clickRamdomPoint,pressContexMenu,
	}
    enum Perform {
    	click,longClick,setText,scrollForward
    }
	
	// to control instrumentation, send an intent to this broadcast receiver
	// `adb shell am broadcast -a android.intent.action.RUN -e cmd <cmd>`
	private BroadcastReceiver  receiver=new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, final Intent intent) {
			// TODO Auto-generated method stub
		    Thread thread=new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					EventInfo eInfo=ResolveIntent.resolve(intent);
					Log.i(TAG, "onReceive cmd: "+eInfo.cmd);
					if(eInfo.inputValue!=null&&!eInfo.inputValue.isEmpty()) 
						inputValue=eInfo.inputValue;
					
				    switch(Command.valueOf(eInfo.cmd)){
				    case initial:
				    	 initial(eInfo.packageName,eInfo.act);
				    	 break;
				    case dump:
				    	 dump();
				    	 break;
				    case exit:
				         exit();
				         break;
				    case byResourceId:
				    	byResourceId(eInfo.ucmd,eInfo.resourceId);
				    	break;
				    case byContentDes:
				    	byContentDes(eInfo.ucmd,eInfo.contentDesc);
				    	break;
				    case byText:
				    	byText(eInfo.ucmd,eInfo.text);
				    	break;
				    case fromParentWithText:
				    	fromParentWithText(eInfo.ucmd,eInfo.parentNclass,eInfo.instance,eInfo.nclass,eInfo.text);
				    	break;
				    case byXPath:
				    	byXPath(eInfo.ucmd,eInfo.xpath);
				    	break;
				    case goBack:
				    	goBack();
				    	break;
				    case takeScreenshot:
				    	takeScreenshot(eInfo.picName);
				    	return;
				    case rotate:
				    	rotate();
				    	break;
				    case swipeLeft:
				    	swipeLeft();
				    	break;
				    case swipeRight:
				    	swipeRight();
				    	break;
				    case swipeUp:
				    	swipeUp();
				    	break;
				    case swipeDown:
				    	swipeDown();
				    	break;
				    case clickRamdomPoint:
				    	clickRamdomPoint();
				    	break;
				    case pressContexMenu:
				    	pressContexMenu();
				    	break;
				    }
				 
				    // add current Activity
				    Log.i(TAG, "sdcard path="+sdcard_path);
				 /*   if(solo!=null&&solo.getCurrentActivity()!=null)
				    	putActNameToFile(solo.getCurrentActivity().getClass().getSimpleName());*/
				    String currentActName=Troy.this.getRunningActivityName();
				    putActNameToFile(currentActName);
				    saveCurrentActName(currentActName);
				    
				    mDevice.waitForWindowUpdate(null, 1000);
				    mDevice.waitForIdle();
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    //dump current windows
				    dump();
				    Log.i(TAG, "send message");
				    Log.i(TAG, "Action Finished");
				  //  UtilEC.sendMessageBySocket("windowUpdate", UtilEC.serverIP, UtilEC.portForSocketMsg);
				}
		    	
	    });
		    singleThreadExecutor.execute(thread);
		
		}
		
	};
	
	private String getRunningActivityName(){ 
		/*String cur=mDevice.getCurrentActivityName();
		System.out.println("current activity name= "+ cur);*/
        ActivityManager activityManager=(ActivityManager) this.getContext().getSystemService(Context.ACTIVITY_SERVICE); 
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName(); 
        return runningActivity;                
     }
	
/**
 * 
 * @param actName start activity Name
 */
    private void initial(String packageName,String actName){
        try{
	    	Log.i(TAG, "initial actName: "+actName);
	    	pName=packageName;
	    	//start activity
	        Intent start=new Intent();
	        start.setComponent(new ComponentName(packageName, actName));//"com.eleybourn.bookcatalogue"
	       // start.setClassName("com.eleybourn.bookcatalogue", actName);
	        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        getTargetContext().startActivity(start);
	        Thread.sleep(5000); //wait for loading 
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	//Log.i(TAG, "initial Finished");
        }
	      
    }


	//dump
    private void dump(){
    	  try{
		    	 // File file = new File(Environment.getExternalStorageDirectory()+File.separator+"dump.xml");
		    	  File file=new File(UIDUMP_DEVICE_PATH);
		    	  file.mkdirs();
		    	  if(file.exists()){
		              file.delete();
		          }
		          file.createNewFile();
		          Log.i(TAG, "filePath "+file.getAbsolutePath());  
		          OutputStream outputStream = new FileOutputStream(file);
		        //  mDevice.setCompressedLayoutHeirarchy(true);
		          mDevice.dumpWindowHierarchy(outputStream);
		        //  Log.i(TAG, "dumpFinished");
		      }catch(Exception e){
		    	  e.printStackTrace();
		      }finally{
		    	//  Log.i(TAG, "dump Finished");  	  
		      }
    }
    
    
    //exit Instrumentation set flag=true
    private void exit(){
    	try{
    	 exit=true;
    	 singleThreadExecutor.shutdown();
        // deleteDir();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
	    	//  Log.i(TAG, "exit Finished");  	  
	      }
    }
    
    
    //click
    private void byResourceId(String ucmd,String resId){
    	try{
    	  Log.i(TAG, "resId "+resId);  
    	  UiObject obj=mDevice.findObject(new UiSelector().resourceId(resId));
    	  obj.waitForExists(3000);
    	  if(obj.exists()){
             switch(Perform.valueOf(ucmd)){
             case click:
            	 obj.click();break;
             case longClick:
            	 obj.longClick();break;
             case setText:
            	 obj.clearTextField();
            	 obj.legacySetText(inputValue);
            	// mDevice.pressEnter();
            	 break;
             case scrollForward:
            	 UiScrollable scroll=new UiScrollable(new UiSelector().resourceId(resId));
            	 if(scroll.exists())
            		 scroll.scrollForward(100);
                	 //scroll.scrollToEnd(1);
            	 break;
             default:break;
             }
    	  }
    	}catch(UiObjectNotFoundException e){
    	    Log.e(TAG, e.getMessage());	
    	}finally{
    		//  Log.i(TAG, "byResourceId Finished");  	  
    	}
    }
    
    
    private void byContentDes(String ucmd,String contentDesc){
    	try{
      	  UiObject obj=mDevice.findObject(new UiSelector().description(contentDesc));
      	 obj.waitForExists(3000);
      	if(obj.exists()){
            switch(Perform.valueOf(ucmd)){
            case click:
           	 obj.click();break;
            case longClick:
           	 obj.longClick();break;
            case setText:
             obj.clearTextField();
           	 obj.legacySetText(inputValue);
           	// mDevice.pressEnter();
           	 break;
            case scrollForward:
            	 UiScrollable scroll=new UiScrollable(new UiSelector().description(contentDesc));
            	 if(scroll.exists())
            		 scroll.scrollForward(100);
                	// scroll.scrollToEnd(1);
           	 break;
            default:break;
            }
      	}
      	}catch(UiObjectNotFoundException e){
      	    Log.e(TAG, e.getMessage());	
      	}finally{
  		//  Log.i(TAG, "byContentDes Finished");  	  
  	    }
    }
    
    private void byText(String ucmd,String text){
    	try{
      	  UiObject obj=mDevice.findObject(new UiSelector().text(text));
      	 obj.waitForExists(3000);
      	if(obj.exists()){
            switch(Perform.valueOf(ucmd)){
            case click:
           	 obj.click();break;
            case longClick:
           	 obj.longClick();break;
            case setText:
             obj.clearTextField();
           	 obj.legacySetText(inputValue);
           	 break;
            case scrollForward:
            	 UiScrollable scroll=new UiScrollable(new UiSelector().text(text));
            	
            	 if(scroll.exists())
            	 scroll.scrollForward(100);
            	 //scroll.scrollToEnd(1);
           	 break;
            default:break;
            }
      	}
      	}catch(UiObjectNotFoundException e){
      	    Log.e(TAG, e.getMessage());	
      	}finally{
  		//  Log.i(TAG, "byText Finished");  	  
  	    }
    }
    
    private void fromParentWithText(String ucmd,String parentClass,int instance,String nclass,String text){
    	try{
        	  UiObject obj=mDevice.findObject(new UiSelector()
        			  .className(parentClass)
        			  .instance(instance)
        			  .childSelector(new UiSelector()
        					  .className(nclass)
        					  .text(text)));
        	  obj.waitForExists(3000);
        	  if(obj.exists()){
                  switch(Perform.valueOf(ucmd)){
                  case click:
                 	 obj.click();break;
                  case longClick:
                 	 obj.longClick();break;
                  case setText:
                	 obj.clearTextField();
                 	 obj.legacySetText(inputValue);
                 	// mDevice.pressEnter();
                 	 break;
                  case scrollForward:
                	  UiScrollable scroll=new UiScrollable(new UiSelector()
                			  .className(parentClass)
                			  .instance(instance)
                			  .childSelector(new UiSelector()
                					  .className(nclass)
                					  .text(text)));
                 	 if(scroll.exists())
                 		 scroll.scrollForward(100);
                    	 //scroll.scrollToEnd(1);
                 	 break;
                  default:break;
                  }
            	}
        	}catch(UiObjectNotFoundException e){
        	    Log.e(TAG, e.getMessage());	
        	}finally{
        		// Log.i(TAG, "fromParentWithText Finished");  	  
      	    }
    }
    

    
    private void byXPath(String ucmd,String xpath){
    	//int exception=10/0; //throw exception to check exception handler work well?
    	 UiObject2 obj=null; 
    	 try{
         obj=mDevice.findObject(By.xpath(xpath));	
    	 if(obj==null) Log.e(TAG, "can't find the target node by xpath:"+xpath);
    	 else {
    		 Log.i(TAG, "found the target node! obj="+obj+"  xpath = "+xpath + "resource-id = "+ obj.getResourceName());
    		 switch(Perform.valueOf(ucmd)){
             case click:
            	 obj.click();break;
             case longClick:
            	 obj.longClick();break;
             case setText:
            	 obj.legacySetText(inputValue);
            	 break;
             case scrollForward:
            	 obj.scroll(Direction.UP, 0.5f); //Direction not consider,modify latter 
            	 break;
             default:break;
             }
    	 }
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 }finally{
    		 if(null!=obj) obj.recycle();
    		// Log.i(TAG, "byXPath Finished");  	  
  	    }
    }
    
    
    
    private void putActNameToFile(String actName){
	     try{
	    	UtilEC.createFile(ACTIVTY_COVERAGE_PATH);
	    	String actStr=UtilEC.readFromFile(ACTIVTY_COVERAGE_PATH);
	    	if(!actStr.trim().contains(actName)){
	    		String append="-"+actName;
	    		UtilEC.appendToFile(ACTIVTY_COVERAGE_PATH,append);
	    	}
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }
	    	return ;
    }
    
    private void saveCurrentActName(String actName){
    	try{
    		UtilEC.createFile(CurrentActvityName_PATH);
    		String actStr=UtilEC.readFromFile(CurrentActvityName_PATH);
    		if(!actStr.trim().equals(actName)){
    			UtilEC.saveToFile(CurrentActvityName_PATH,actName);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    //system event
    private void goBack(){
    	try{
    		System.out.println("before go back()");
    		mDevice.pressBack();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		Log.i(TAG, "goBack Finished");  	 
    	}
    }
    
    private void takeScreenshot(String fileName){
    	try{
    		File dir=new File(SCREEN_SHOT_PATH);
    		if(!dir.exists()){
			    dir.mkdirs();
			}
    		String picName=fileName+".png";
    		System.out.println("before takeshoot Path="+SCREEN_SHOT_PATH);
    		File file=new File(String.format("%s/%s",SCREEN_SHOT_PATH,picName));
    		mDevice.takeScreenshot(file);
    		System.out.println("after takeshoot file="+file.getAbsolutePath());
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		//Log.i(TAG, "takeScreenshot Finished");  
    	}
    }
    
    private void rotate(){
    		try {
				mDevice.setOrientationLeft();
				mDevice.setOrientationNatural();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
	    		//Log.i(TAG, "rotate Finished");  
	    	}
    }
    //swipe  
    private void swipeLeft(){
    	 try{
    		 int height=mDevice.getDisplayHeight();
    		 int width=mDevice.getDisplayWidth();
    		 int startX=width-100;
    		 int endX=100;
    		 int startY=height/2;
    		 int endY=height/2;
    		 mDevice.swipe(startX, startY, endX, endY, 8);
    	 }catch(Exception e){
           e.printStackTrace();
    	 }finally{
	    		//Log.i(TAG, "swipeLeft Finished");  
	     }
    }
    
    private void swipeRight(){
   	 try{
   		 int height=mDevice.getDisplayHeight();
   		 int width=mDevice.getDisplayWidth();
   		 int startX=10;
   		 int endX=width-10;
   		 int startY=height/2;
   		 int endY=height/2;
   		 mDevice.swipe(startX, startY, endX, endY, 8);
   	 }catch(Exception e){
         e.printStackTrace();
   	 }finally{
	     //Log.i(TAG, "swipeRight Finished");  
	 }
   }
    
    private void swipeUp(){
      	 try{
      		 int height=mDevice.getDisplayHeight();
      		 int width=mDevice.getDisplayWidth();
      		 int startX=width/2;
      		 int endX=width/2;
      		 int startY=height-100;
      		 int endY=100;
      		 mDevice.swipe(startX, startY, endX, endY, 80);
      	 }catch(Exception e){
            e.printStackTrace();
      	 }finally{
   	    // Log.i(TAG, "swipeUp Finished");  
   	 }
      }
    
    private void swipeDown(){
     	 try{
     		 int height=mDevice.getDisplayHeight();
     		 int width=mDevice.getDisplayWidth();
     		 int startX=width/2;
     		 int endX=width/2;
     		 int startY=100;
     		 int endY=height-100;
     		 mDevice.swipe(startX, startY, endX, endY, 80);
     	 }catch(Exception e){
           e.printStackTrace();
     	 }finally{
  	    // Log.i(TAG, "swipeDown Finished");  
  	 }
     }
   
    //click random point
    private void clickRamdomPoint(){
    	try{
    	 int height=mDevice.getDisplayHeight();
 		 int width=mDevice.getDisplayWidth();
 		 Random random=new Random();
    	 int x=random.nextInt(width);
    	 int y=random.nextInt(height);
    	 mDevice.click(x, y);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
     	 //    Log.i(TAG, "clickRamdomPoint Finished");  
     	 }
    }
    
    //press context-menu
    private void pressContexMenu(){
        try{
    	  mDevice.pressMenu();
        }catch(Exception e){
           e.printStackTrace();
        }finally{
        	 //    Log.i(TAG, "pressMenu Finished");  	
        }
        
    }
    
}
