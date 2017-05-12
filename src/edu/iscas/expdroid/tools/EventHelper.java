package edu.iscas.expdroid.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.android.uiautomator.TreeViewModel;
import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.UiNode;

import edu.iscas.expdroid.exception.NotFindParamException;
import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.model.TEvent.ActionOper;
import edu.iscas.expdroid.model.TEvent.EventType;
import edu.iscas.expdroid.model.TState;
import edu.iscas.expdroid.shelltools.Cmd;
import edu.iscas.expdroid.strategy.BFS;
import edu.iscas.expdroid.strategy.StrategyImpl;
import edu.iscas.expdroid.utils.TPair;
import edu.iscas.expdroid.utils.UtilE;

public class EventHelper {
    private Cmd cmd=new Cmd();
    private List<TEvent> preSysEvent;
    public TState getCurState(){
    	TState state=new TState();
    	/*try {
    	    Thread.sleep(100);   //wait for activity load finished
            boolean r=cmd.dump();//dump windows 
            if(!r) return state; //dump fail
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	TreeViewModel treeNodeInfo=new TreeViewModel(Config.g().avdName);
    	state.uiSet.addAll(treeNodeInfo.getAllNode());
    	state.rootNode=treeNodeInfo.getRootNode();
    //	state.events.addAll(filterEvent(computeEvents(state)));
    	state.events.addAll(computeEvents(state));
    	state.events.addAll(obtainSysEvent());
    	//state.priority=getStatePriority(state);
    	state.actName=UtilE.getCurrentActivityName();
    	return state;
    }
    
    public float getStatePriority(TState state){
    	 float def=70.0f,sum =0.0f;
    	 int c=0;
    	 for(TEvent e:state.events){
    		 if(e.priority>1){
    			 c++;
    			 sum+=e.priority;
    		 }
    	 }
    	 return c==0?def:sum/c;
    }
    
    public List<TEvent> computeEvents(TState state){
         List<TEvent> events=new ArrayList<TEvent>();
         if(state==null) return events;
         for(UiNode node:state.uiSet){
        	 //all node must be enable,or create event for this UiNode
        	 if(!node.isEnabled()) continue; 
        	 //clickable--> click
        	 if(node.isClickable()||node.isCheckable()){
        		 //not click ListView direactly but its items
        		 if(node.getNClass().endsWith("ListView")){
        			 for(BasicTreeNode item:node.getChildrenList()){
        				 UiNode itemui=(UiNode)item;
        				 TEvent event=new TEvent(EventType.UIEvent,ActionOper.click,itemui);
        				 if(!events.contains(event))events.add(event);
        			 }
        		 }else {
        			 if(!node.getNClass().endsWith("EditText")){
        		         TEvent event=new TEvent(EventType.UIEvent,ActionOper.click,node);
    				     if(!events.contains(event))events.add(event);
        			 }
        		 }
        	 }
        	 
        	 //longClickable--> longClick
        	 if(node.isLongClickable()){
        		 //not click ListView direactly but its items
        		 if(node.getNClass().endsWith("ListView")){
        			 for(BasicTreeNode item:node.getChildrenList()){
        				 UiNode itemui=(UiNode)item;
        				 TEvent event=new TEvent(EventType.UIEvent,ActionOper.longClick,itemui);
        				 if(!events.contains(event))events.add(event);
        			 }
        		 }else {
        		     TEvent event=new TEvent(EventType.UIEvent,ActionOper.longClick,node);
        		     event.priority=30; //初始优先级
    				 if(!events.contains(event))events.add(event);
        		 }
        	 }
        	 
        	 //class:EditText --> type/SetText 
        	 if(node.getNClass().endsWith("EditText")){
        		 TEvent event=new TEvent(EventType.UIEvent,ActionOper.setText,node);
        		 String inputValue="AutoTest";
        		 //check predefine value in config file
        		 /*if(Config.g().preDefineInput.containsKey(node.getResourceId()))
        			 inputValue=Config.g().preDefineInput.get(node.getResourceId())+"";*/
        		 inputValue=inputHelper.generateTextInput(node);
        		 event.actionParams.add(new TPair("inputValue",inputValue));
                 event.priority=0; //初始优先级
				 if(!events.contains(event))events.add(event);
        	 }
        	 
        	 //scrollable--> swipe up/down/left/right UiScrollable--> fling backward/forward/toBegining/toEnd  scroll Backward/Forward
        	 if(node.isScrollable()){
        		 TEvent event=new TEvent(EventType.UIEvent,ActionOper.scrollForward,node);
				 if(!events.contains(event))events.add(event);
        	 }
         }
         return events;
    }
    
    public TEvent selectBFSEvent(TState state){
    	   //all events has been trigger
    	   if(state.cursor>=state.events.size()) return null;
    	   //get next event and mark count
    	   TEvent event=state.events.get(state.cursor);
    	   if(state.eventExpCount.containsKey(state.cursor)){
    		  state.eventExpCount.put(state.cursor, state.eventExpCount.get(state.cursor)+1);
    	   }else{
    		  state.eventExpCount.put(state.cursor, 1);
    	   }
    	   state.cursor++;
    	   return event;
    }
    
    public TEvent selectRandomEvent(TState state){
    	   List<TEvent> candidates=state.events;
    	   candidates.addAll(obtainSysEvent());
    	   Random random=new Random();
    	   return candidates.get(random.nextInt(candidates.size()));
    }
    public TEvent selectHeuristicEvent1(TState state){
  	  try{
  	   List<Integer> list=new ArrayList<Integer>();
 	   for(int i=0;i<state.events.size();i++){
 		   for(int j=0;j<state.events.get(i).priority;j++) list.add(i);
 	   } 
 	   if(list.size()==0) return null;
 	   Collections.shuffle(list); //not necessary 
  	   Random random=new Random();
	   random.setSeed(System.currentTimeMillis()/1000);
	   int eventIndex=list.get(random.nextInt(list.size()));
	   if(state.eventExpCount.containsKey(eventIndex)){
 		  state.eventExpCount.put(eventIndex, state.eventExpCount.get(eventIndex)+1);
 	   }else{
 		  state.eventExpCount.put(eventIndex, 1);
 	   }
	   if(state.events.get(eventIndex).eType==EventType.UIEvent||state.events.get(eventIndex).action==ActionOper.back)
	   state.events.get(eventIndex).priority=0; //has been choosed
       return state.events.get(eventIndex);

 	  }catch(Exception e){
 		  e.printStackTrace();
 	  }
 	  return null;
    }
    public TEvent selectHeuristicEvent(TState state){
    	  try{
    	  /* boolean finished=state.events.size()<=state.eventExpCount.size();
    	   if(finished) return null;*/
    	   List<Integer> list=new ArrayList<Integer>();
    	   for(int i=0;i<state.events.size();i++){
    		   if(state.eventExpCount.containsKey(i)) continue;
    		   for(int j=0;j<state.events.get(i).priority;j++) list.add(i);
    	   } 
    	   Collections.shuffle(list); //not necessary 
    	   //add system event 
    	   List<TEvent> sysEvents=getSysEvent();
    	   Random random=new Random();
    	   random.setSeed(System.currentTimeMillis());
    	   int seed=(state.events.size()-state.eventExpCount.size())*2/sysEvents.size();
    	   seed=Math.max(2, seed);
    	   if(state.events.size()-state.eventExpCount.size()==0) seed=1;
    	   //choose event type ui:sys=4:1
    	   int typeIndex=random.nextInt(seed);
    	   if(typeIndex!=0){ //UiEvent
    		   int eventIndex=list.get(random.nextInt(list.size()));
	    	   if(state.eventExpCount.containsKey(eventIndex)){
	     		  state.eventExpCount.put(eventIndex, state.eventExpCount.get(eventIndex)+1);
	     	   }else{
	     		  state.eventExpCount.put(eventIndex, 1);
	     	   }
	    	   return state.events.get(eventIndex);
    	   }else{
    		   return sysEvents.get((random.nextInt(sysEvents.size())));
    	   }
    	  }catch(Exception e){
    		  e.printStackTrace();
    	  }
    	  return null;
    	   
    }
    
    
    public boolean executeEvent(TEvent event){
         if(event==null) {
        	 System.out.println("event execute failed,event is nul,please check");
        	 return false;
         }
         boolean result=true;
         switch(event.eType){
         case UIEvent:
        	 result=doUIEvent(event.object,event.action,event.actionParams);
        	 break;
         case SysEvent:
        	 result=doSysEvent(event.action,event.actionParams);
        	 break;
		 default:
			 System.out.println("event execute failed! an invalid event type!");
			 result=false;
			 break;
         }
         return result;
    }
    
    private boolean doUIEvent(UiNode node,ActionOper action,List<TPair> aparamPair){
        List<TPair> paramList=new ArrayList<TPair>();
        //deal with cmd
        paramList.add(new TPair("ucmd",action.toString()));
        //deal with location-params
        List<TPair> lparamsPair=null;
        try{
        	lparamsPair=generateLParams(node);
        }catch(NotFindParamException e){
        	System.out.println(e.getMessage());
        	return false;
        }
        paramList.addAll(lparamsPair);
        //deal with action-params
        paramList.addAll(aparamPair);
   
        try{
        	return cmd.sendUiEventInfo(paramList);
        }catch(Exception e){
        	System.out.println(e.getMessage());
        	return false;
        }
    }
    
    private boolean doSysEvent(ActionOper action,List<TPair> aparamPair){
        boolean res=true;
    	try{
    	switch(action){
    	case back: res=cmd.goBack(); break;
    	case rotate:res=cmd.rotate();break;
    	case swipeLeft: res=cmd.swipeLeft();break;
    	case swipeRight: res=cmd.swipeRight();break;
    	case swipeUp: res=cmd.swipeUp();break;
    	case swipeDown: res=cmd.swipeDown();break;
    	case clickRamdomPoint: res=cmd.clickRamdomPoint();break;
    	case pressContexMenu: res=cmd.pressContexMenu();break;
    	case takeScreenshot:
    	    String picName=System.currentTimeMillis()+"";
    	    if(aparamPair!=null&&!aparamPair.isEmpty())
    	    	picName=aparamPair.get(0).value;
    		cmd.takeScreenshot(picName);
    		break;
    	default:break;
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
        return res;
    }
    
    private List<TPair> generateLParams(UiNode node) throws NotFindParamException{
 	   List<TPair> rlist=new ArrayList<TPair>();
 	   if(node.getNClass().endsWith("EditText")){
 	   //resource-id
 	  if(node.getResourceId()!=null&&!node.getResourceId().isEmpty()) {
		   System.out.println("reource-id="+ node.getResourceId());
		   rlist.add(new TPair("resource-id",node.getResourceId()));
		 /*  if(onlyOneMatch(rlist)) return rlist;
		   else rlist.clear();*/
		   return rlist;  //bug  以后再修改
	   }
	   System.out.println("not find only one node");
 	   //text
	   if(node.getText()!=null&&!node.getText().isEmpty()) {
		   rlist.add(new TPair("text",node.getText()));
		  /* if(onlyOneMatch(rlist)) return rlist;
		   else rlist.clear(); */
		  return rlist;
	   }
	  //content-desc
	   if(node.getContentDesc()!=null&&!node.getContentDesc().isEmpty()) {
		   rlist.add(new TPair("content-desc",node.getContentDesc()));
		   /*if(onlyOneMatch(rlist)) return rlist;
		   else rlist.clear(); */
		   return rlist;
	   }
	   System.out.println("resourse is empty "+node.getResourceId().isEmpty() );
 	   
 	   //if(parent-class is ListView ,parent-class instance text
 	   if(node.getParent()!=null&&((UiNode)node.getParent()).getNClass().endsWith("ListView")){
 		   String text=getTextFromChild(node);
 		   if(text!=null){
 			   rlist.add(new TPair("class","android.widget.ListView"));
 			   int instance=getInstance(rlist,(UiNode)node.getParent());
 			   if(instance!=-1){
 				   rlist.clear();
 				   rlist.add(new TPair("parent-class","android.widget.ListView"));
     			   rlist.add(new TPair("instance",instance+""));
     			   rlist.add(new TPair("class",node.getNClass()));
     			   rlist.add(new TPair("text",text));     
     			   return rlist;
 			   }else rlist.clear();
 		   }else rlist.clear();
 		   
 	   }
 	   }
 	   //xpath 
 	   rlist.add(new TPair("xpath",node.getXPath()));
 	   return rlist;
 	   
 }
    /**
     * 
     * @param plist conditions to find UiNode 
     * @return is 
     */
    private boolean onlyOneMatch(List<TPair> plist){
    	if(StrategyImpl.curState==null) return false;  
    	List<UiNode> matchs=new ArrayList<UiNode>();
    	
    	for(UiNode node:StrategyImpl.curState.uiSet){
    		if(!node.isEnabled()) continue; 
    		//System.out.println("resource-id= "+node.getResourceId() );
    		boolean match=true;
    		for(TPair param:plist){
    		//	System.out.println("isequal = "+param.value.equals(node.getResourceId()));
    			 switch(param.key){
    			 case  "resource-id": match=param.value.equals(node.getResourceId());break;
     			 case  "content-desc": match=param.value.equals(node.getContentDesc());break;
    			 case  "text": match=param.value.equals(node.getText());break;
    			 case  "class": match=param.value.equals(node.getNClass());break;
    			 case  "package": match=param.value.equals(node.getPackage());break;
    			 case  "checkable": match=param.value.equals(node.isCheckable()+"");break;
     			 case  "checked": match=param.value.equals(node.isChecked()+"");break;
     			 case  "clickable": match=param.value.equals(node.isClickable()+"");break;
     			 case  "enabled": match=param.value.equals(node.isEnabled()+"");break;
     			 case  "focusable": match=param.value.equals(node.isFocusable()+"");break;
     			 case  "focused": match=param.value.equals(node.isFocused()+"");break;
     			 case  "scrollable": match=param.value.equals(node.isScrollable()+"");break;
     			 case  "long-clickable": match=param.value.equals(node.isLongClickable()+"");break;
     			 case  "password": match=param.value.equals(node.isPassword()+"");break;
     			 case  "selected": match=param.value.equals(node.isSelected()+"");break;
    		//	 case  "instance": match=param.value.equals(node.getInstance()+"");break;
    			 case  "parent-class": match=(node.getParent()!=null)&&param.value.equals(((UiNode)node.getParent()).getNClass());break;
    		//	 case  "parent-instance": match=param.value.equals((UiNode)node.getParent()).getInstance()+"");break;
    			 default:break;
    			 }
    			// if(!match) break;
    		}
    		if(match) matchs.add(node);
    		if(matchs.size()>1) {
    			return false;
    		}
    	}
    	System.out.println("match nod count ="+matchs.size());
    	return matchs.size()==1;
    }
    
    /**
     * 
     * @param plist
     * @return -1: not node match  0: first
     */
    private int getInstance(List<TPair> plist,UiNode curNode){
    	if(BFS.curState==null) return -1;  
    	int  result=-1;
    	for(UiNode node:BFS.curState.uiSet){
    		boolean match=true;
    		for(TPair param:plist){
    			 switch(param.key){
    			 case  "resource-id": match=param.value.equals(node.getResourceId());break;
     			 case  "content-desc": match=param.value.equals(node.getContentDesc());break;
    			 case  "text": match=param.value.equals(node.getText());break;
    			 case  "class": match=param.value.equals(node.getNClass());break;
    			 case  "package": match=param.value.equals(node.getPackage());break;
    			 case  "checkable": match=param.value.equals(node.isCheckable()+"");break;
     			 case  "checked": match=param.value.equals(node.isChecked()+"");break;
     			 case  "clickable": match=param.value.equals(node.isClickable()+"");break;
     			 case  "enabled": match=param.value.equals(node.isEnabled()+"");break;
     			 case  "focusable": match=param.value.equals(node.isFocusable()+"");break;
     			 case  "focused": match=param.value.equals(node.isFocused()+"");break;
     			 case  "scrollable": match=param.value.equals(node.isScrollable()+"");break;
     			 case  "long-clickable": match=param.value.equals(node.isLongClickable()+"");break;
     			 case  "password": match=param.value.equals(node.isPassword()+"");break;
     			 case  "selected": match=param.value.equals(node.isSelected()+"");break;
    		//	 case  "instance": match=param.value.equals(node.getInstance()+"");break;
    			 case  "parent-class": match=(node.getParent()!=null)&&param.value.equals(((UiNode)node.getParent()).getNClass());break;
    		//	 case  "parent-instance": match=param.value.equals((UiNode)node.getParent()).getInstance()+"");break;
    			 default:break;
    			 }
    			 if(!match) break;
    		}
    		if(match) ++result;
    		if(isSameNode(node,curNode)) break;
    		//if(node.equals(curNode)) break;
    	}
    	return result;
    }
    
    
    private boolean isSameNode(UiNode nodeOne,UiNode nodeTwo){
    	if(nodeOne==null||nodeTwo==null) return false;
    	else {
    		if(nodeOne.getXPath()!=null)
    			return nodeOne.getXPath().equals(nodeTwo.getXPath());
    		else return false;
    	}
    }
    
    private String getTextFromChild(UiNode node){
    	   List<UiNode> children=new ArrayList<UiNode>();
		   for(BasicTreeNode cnode:node.getChildrenList()){
			   children.add((UiNode)cnode);
		   }
		   for(UiNode uiNode:children){
			   if(uiNode.getText()!=null&&!uiNode.getText().isEmpty()){
				   return uiNode.getText();
			   }else {
				   if(uiNode.getChildCount()>0){
					   return getTextFromChild(uiNode);
				   }
			   }
		   }
		   return null;
    }
    
    private List<TEvent> getSysEvent(){
    	if(preSysEvent==null)
    	    return 	obtainSysEvent();
    	else return preSysEvent;
    	
    }
    
    private List<TEvent> obtainSysEvent(){
    	List<TEvent> sysEvents=new ArrayList<TEvent>();
    	if(Config.g().goBack)
    	sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.back,null,5));
    	if(Config.g().rotate)
    	  sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.rotate,null,10));
    	if(Config.g().swipe){
    	  sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.swipeLeft,null,30));
    	  sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.swipeRight,null,30));
    	  sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.swipeUp,null,30));
    	  sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.swipeDown,null,30));
    	}
    	//sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.pressContexMenu,null,10));
    	//sysEvents.add(new TEvent(EventType.SysEvent,ActionOper.clickRamdomPoint,null,10));
    	return sysEvents;
    }
    
    
    public boolean hasTypeEvent(TState s){
    	if(s==null) return false;
    	for(TEvent e:s.events){
    		 if(e.action==ActionOper.setText) return true;
    	}
    	return false;
    }
    
    public void fillAllText(TState s){
    	if(s==null) return ;
    	for(TEvent e:s.events){
   		 if(e.action==ActionOper.setText){
   			executeEvent(e);
   		 }
   	  }
    }
    
    public List<TEvent> filterEvent(List<TEvent> events){
    	 if(Statistic.strategy.obtainGraph()==null) return events;
		 List<TEvent> rlist=new ArrayList<TEvent>();
		 Set<TEvent> allEvents=Statistic.strategy.obtainGraph().getAllEvents();
		 for(TEvent e:events){
			 if(!allEvents.contains(e)) rlist.add(e); 
		 }
		 System.out.println("no uiEvent "+(rlist.size()==0));
		 System.out.println("filter event count "+ (events.size()- rlist.size()));
		 return rlist;
	}
    
    

}
