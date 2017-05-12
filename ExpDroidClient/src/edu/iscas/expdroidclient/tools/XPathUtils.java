package edu.iscas.expdroidclient.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import edu.iscas.expdroidclient.Troy;

public class XPathUtils {
    private static final String tag=XPathUtils.class.getSimpleName();
	public static View getViewByXPath(String path,List<View> views){
	     for(View view:views){
	    	 if(getPathNodesAndIndexMatch(view, path)){
	    		 return view;
	    	 }
	     }
	    return null;
     }
	
	   public  static   boolean getPathNodesAndIndexMatch(View node,String xPath){
	       String sNodeClasses[] = xPath.substring(1).split("/");
	       int index = sNodeClasses.length-1;
	       int numberOfChecksDone = 0;
	       String xp="";
	       View currNode=node;
	       while(currNode!=null){
	           if(index < 0){
	               return false;
	           }
	           String currClass=currNode.getClass().getName();
	           ViewParent viewParent = currNode.getParent();
	           if(viewParent!=null && viewParent instanceof ViewGroup){
	               ViewGroup currNodeParent = (ViewGroup) viewParent;
	               //has parent
	               if(currNodeParent.getChildCount()>1){
	                   List<View> childNodeList = new ArrayList<View>();
	                   for(int i=0;i<currNodeParent.getChildCount();i++){
	                       View childNode = currNodeParent.getChildAt(i);
	                       if(childNode!=null && childNode.getVisibility()==View.VISIBLE
	                               && isInstance(childNode, getClassName(sNodeClasses[index]))){
	                           childNodeList.add(childNode);
	                       }
	                   }

	                   // Sort the list (Issue in Android -- trees appear differently than normal)
	                   Collections.sort(childNodeList, new EspressoViewComparator());

	                   int childIndex=-1;
	                   for(int i=0;i<childNodeList.size();i++){
	                       if(childNodeList.get(i).equals(currNode)){
	                           childIndex=i;
	                           break;
	                       }
	                   }

	                   if(childIndex==-1){
	                       Log.e("Barista","computeXPath: child not found from parent");
	                       return false;
	                   }
	                   int xpIndex=childIndex+1;
	                   xp="/"+currClass+"["+xpIndex+"]"+xp;
	                   if(!checkEquals(sNodeClasses, index--, currNode, childIndex)){
	                       return false;
	                   }
	               }
	               else{
	                   //this node is the only child
	                   xp="/"+currClass+xp;
	                   if(!checkEquals(sNodeClasses, index--, currNode, 0)){
	                       return false;
	                   }
	               }
	               currNode=currNodeParent;
	           }
	           else{
	               //the node should be root because has no parent
	               xp="/"+currClass+xp;
	               if(!checkEquals(sNodeClasses, index--, currNode, 0)){
	                   return false;
	               }
	               currNode=null;
	           }
	           numberOfChecksDone++;
	       }
	       Log.d(tag, "Final XPath"+xp);
	       if(numberOfChecksDone == sNodeClasses.length){
	           return true;
	       }else{
	           return false;
	       }
	   }
	   
	   private static  boolean checkEquals(String[] classes, int index, View node2, int pos2){
	       if(index < 0){
	           return false;
	       }
	       Log.d(tag, classes[index]+", "+node2.getClass().getName()+">"+pos2);
	       int pos1=0;
	       String className= classes[index];
	       if(className.endsWith("]")){
	           pos1 = Integer.parseInt(className.split("[\\[\\]]")[1])-1;
	           className = className.substring(0, className.indexOf('['));
	       }

	       if(pos1 != pos2){
	         Log.d(tag, "CHECK FAILED INDEX >> "+pos1+","+pos2);
	           return false;
	       }
	       if(!isInstance(node2, className)) {
	          Log.d(tag, "CHECK FAILED TYPE");
	           return false;
	       }
	        Log.d(tag, "CHECK PASSED");
	       return true;
	   }

	   private static boolean isInstance(Object o, String className) {
	       try {
	           Class clazz = Class.forName(className);
	           return clazz.isInstance(o);
	       } catch (ClassNotFoundException cnfe) {
	           Log.d(tag, "isInstance: ClassNotFoundException");
	           return false;
	       }
	   }

	private static String getClassName(String className) {
	     if(className.endsWith("]")) {
	       return className.substring(0, className.indexOf('['));
	      }else return className;
	}
	   
	   

	//compute xpath
	private  static String computeXPath(View node){
	    String xp="";
	    View currNode=node;
	    while(currNode!=null){
	        String currClass=currNode.getClass().getName();
	        ViewParent viewParent = currNode.getParent();
	        if(viewParent!=null && viewParent instanceof ViewGroup){
	            ViewGroup currNodeParent = (ViewGroup) viewParent;
	            //has parent
	            if(currNodeParent.getChildCount()>1){
	                List<View> childNodeList = new ArrayList<View>();
	                for(int i=0;i<currNodeParent.getChildCount();i++){
	                    View childNode = currNodeParent.getChildAt(i);
	                    String childClass = childNode.getClass().getName();
	                    if(TextUtils.equals(childClass, currClass)) {
	                        childNodeList.add(childNode);
	                    }
	                }

	                int childIndex=-1;
	                for(int i=0;i<childNodeList.size();i++){
	                    if(childNodeList.get(i).equals(currNode)){
	                        childIndex=i;
	                        break;
	                    }
	                }

	                if(childIndex==-1){
	                    Log.e("Barista","computeXPath: child not found from parent");
	                    return "";
	                }
	                int xpIndex=childIndex+1;
	                xp="/"+currClass+"["+xpIndex+"]"+xp;

	            }
	            else{
	                //this node is the only child
	                xp="/"+currClass+xp;
	            }
	            currNode=currNodeParent;
	        }
	        else{
	            //the node should be root because has no parent
	            xp="/"+currClass+xp;
	            currNode=null;
	        }
	    }
	    return xp;
	}
	   //xpath end

}
