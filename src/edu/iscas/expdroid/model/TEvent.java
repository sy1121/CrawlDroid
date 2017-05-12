package edu.iscas.expdroid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.android.uiautomator.tree.UiNode;

import edu.iscas.expdroid.utils.TPair;

public class TEvent implements Serializable{
	public enum EventType {UIEvent,SysEvent};
     public enum ActionOper {
    	 click,longClick,setText,
    	 scrollForward,scrollBackward,swipeUp,swipeDown,swipeLeft,swipeRight,clickRamdomPoint,
    	 back,takeScreenshot,rotate,pressContexMenu,
    	 };
    	 
     public EventType eType;
     public ActionOper action;
     public UiNode object;
     public List<TPair> actionParams;
     
     public int priority;  //only use in heuristic 
     
     public TEvent(){
    	 this.eType=EventType.UIEvent;
    	 this.action=ActionOper.click;
    	 this.object=null;
    	 this.actionParams=new ArrayList<TPair>();
    	 this.priority=70;
     }
     
     public TEvent(EventType type,ActionOper action,UiNode object){
    	 this.eType=type;
    	 this.action=action;
    	 this.object=object;
    	 this.actionParams=new ArrayList<TPair>();
    	 this.priority=70;
     }
     
     public TEvent(EventType type,ActionOper action,UiNode object,int priority){
    	 this.eType=type;
    	 this.action=action;
    	 this.object=object;
    	 this.actionParams=new ArrayList<TPair>();
    	 this.priority=priority;
     }
     
     
     
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(object==null) return false;
		TEvent target=(TEvent)obj;
		if(this.eType!= target.eType) return false;
		if(this.eType==EventType.SysEvent){
			return target.action==this.action;
		}else{
			String sxpath1=this.object.getXPath();
			String sxpath2=target.object.getXPath();
			boolean requal=target.object.getResourceId().equals(this.object.getResourceId());
			return (target.action==this.action)&&(sxpath1.equals(sxpath2))&&requal;
		}
		/*TEvent event=(TEvent)obj;
		return this.isSimilar(event);*///(this.object==event.object)&&(this.action==event.action);

	}
	
	public boolean isSimilar(TEvent target){
		if(this.eType!= target.eType) return false;
		if(this.eType==EventType.SysEvent){
			return target.action==this.action;
		}else{
			String sxpath1=this.object.getXPath().replaceAll("[0-9]*", "");
			String sxpath2=target.object.getXPath().replaceAll("[0-9]*", "");
			return (target.action==this.action)&&(sxpath1.equals(sxpath2));
		}
	}
     
     
     
}
