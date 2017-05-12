package edu.iscas.expdroid.tools;

import java.io.File;
import java.util.List;

import com.android.uiautomator.tree.UiNode;

import edu.iscas.expdroid.model.TEdge;
import edu.iscas.expdroid.model.TEdge.EdgeType;
import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.model.TState;
import edu.iscas.expdroid.model.TStateGraph;
import edu.iscas.expdroid.utils.FileOper;
import edu.iscas.expdroid.utils.TPair;

public class ScriptHelper {
   public static final String DOT_FILE_PATH=Statistic.outDirPath+"graph.dot";
   public static final String Crash_SCRIPT_PATH=Statistic.outDirPath+"crash-record-script.txt";
   public static final String e2e_SCRIPT_PATH=Statistic.outDirPath+"edge2event.txt";
   public static void produceGraphScript(TStateGraph graph){
	    try{
	      StringBuffer sb=new StringBuffer();
		  if(graph==null) return;
	      sb.append("digraph G {\n");
	      //all state
	    /*  for(TState state:graph.allState){
	    	  if(state.No==0) 
	    		  sb.append(state.No+" [style=filled;color=lightgrey,label=\""+((char) (state.No+'A'))+"\"];"+"\n");
	    	  else
	    	      sb.append(state.No+" [label=\""+((char) (state.No+'A'))+"\"];"+"\n");
	    	  prepath2event(state.No,state.preTrace);
	      }*/
	      for(TState state:graph.allState){
	    	  sb.append(state.No+" [shape=\"none\",image=\""+Statistic.outDirPath+"screenshot"+File.separator+state.No+".png\",label=\"\",imagescale=true,fixedsize=true,width=\"3.6\",height=\"6.4\"];"+"\n");
	    	  prepath2event(state.No,state.preTrace);
	      }
	      
	      int edgeIndex=0;
	      //all edges
	    /*  for(TState state:graph.allState){
	    	  for(TEdge edge:state.edges){
	    		  if(edge.edgeType==EdgeType.auto){
					  sb.append(state.No+"->"+edge.nextState.No+" [label=\"e"+(edgeIndex++)+"\"];"+"\n");
					 }else if(edge.edgeType==EdgeType.manual){
					  //sb.append(state.No+"->"+edge.nextState.No+" [style=dotted,color=green,label=\"e"+(edgeIndex++)+"\"];"+"\n"); 
					  sb.append(state.No+"->"+edge.nextState.No+" [color=green,label=\"e"+(edgeIndex++)+"\"];"+"\n"); 
					 }
	    		   //System.out.println("edge to event");
	    		   edge2event(edgeIndex-1,edge.eventTrace);  
	    	  }
	      }*/
	      
	      for(TState state:graph.allState){
	    	  for(TEdge edge:state.edges){
	    		   String label=traceToStr(edge.eventTrace);
				   sb.append(state.No+"->"+edge.nextState.No+" [label=\""+(label)+"\"];"+"\n");
	    		   //System.out.println("edge to event");
	    		   edge2event(edgeIndex-1,edge.eventTrace);  
	    	  }
	      }
	      sb.append("}");
		  //写文件
	      FileOper.WriteStringToFile(DOT_FILE_PATH, sb.toString());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
   }
   
   private static String traceToStr(List<TEvent> traces){
	   StringBuffer sb=new StringBuffer("");
	   for(int i=0;i<traces.size();i++){
		   sb.append((i+1)+".");
		   TEvent event=traces.get(i);
		   switch(event.action){
			 case click:
				 sb.append("onView("+brewSelectForGraph(event.object)+").perform(click());");
				 break;
			 case longClick:
				 sb.append("onView("+brewSelectForGraph(event.object)+").perform(longClick());");
				 break;
			 case setText:
				 UiNode node=(UiNode)event.object;
				 String perform="";
	    		 if(node.isPassword()){
	    			 perform=").perform(clearText(),typeTextIntoFocusedView(password));";
	    		 }else{
	    			 String inputValue="AutoTest"; 
	    			 for(TPair param:event.actionParams){
	    				 if(param.key.equals("inputValue")){
	    					 inputValue=param.value;break;
	    				 }
	    			 }
	    			 perform=").perform(clearText(),typeTextIntoFocusedView(\""+inputValue+"\"));";
	    		 }
				 sb.append("onView("+brewSelectForGraph(event.object)+perform);
				 break;
			 case scrollForward:
				 sb.append("onView("+brewSelectForGraph(event.object)+").perform(scrollTo());");
				 break;
			 case back:
				 sb.append("Espresso.pressBack();");
				 break;
			 case takeScreenshot:
				 sb.append("takeScreenshot();");
				 break;
			 case rotate:
				 sb.append("rotate();");
				 break;
			 case swipeLeft:
				 sb.append("onView(isRoot()).swipeLeft();");
				 break;
			 case swipeRight:
				 sb.append("onView(isRoot()).swipeRight();");
				 break;
			 case swipeUp:
				 sb.append("onView(isRoot()).swipeUp();");
				 break;
			 case swipeDown:
				 sb.append("onView(isRoot()).swipeDown();");
				 break;
			 case clickRamdomPoint:
				 sb.append("onView(isRoot()).clickRamdomPoint();");
				 break;
			 case pressContexMenu:
				 sb.append("onView(isRoot()).pressMenuKey();");
				 break;
				 default:break;
			 }
		 sb.append("");
	   }
	   return sb.toString();
   }
   private static String brewSelectForGraph(UiNode node){
	    if(node==null) return "";
	    String select="";
	    if(node.getResourceId()!=null&& !node.getResourceId().isEmpty()){
	        select="withId(\\\""+node.getResourceId()+"\\\")";
	    }else if(node.getContentDesc()!=null&&!node.getContentDesc().isEmpty()){
	    	select="withContentDescription(\\\""+node.getContentDesc()+"\\\")";
	    }else if(node.getText()!=null&&!node.getContentDesc().isEmpty()){
	    	select="withText(\\\""+node.getText()+"\\\")";
	    }else {
	    	select="withXPath(\\\""+node.getXPath()+"\\\")";
	    }
	    return select;
}
   
   private static void edge2event(int edgeIndex,List<TEvent> traces){
	   String common="e"+edgeIndex;
	   buildEspressoScript(traces,common,e2e_SCRIPT_PATH);
   }
   
   private static void prepath2event(int stateIndex,List<TEvent> traces){
	   String common="s"+stateIndex;
	   buildEspressoScript(traces,common,e2e_SCRIPT_PATH);
   }
   
    public static void buildEspressoScript(List<TEvent> events,String common,String filePath){
    	 if(events==null||events.isEmpty()) return ;
    	 try{
    		 StringBuffer sb=new StringBuffer();
    	     sb.append("//"+common+"\n");
    		 for(TEvent event:events){
    			 if(event==null) continue;
    			 switch(event.action){
    			 case click:
    				 sb.append("onView("+brewSelect(event.object)+").perform(click());\n");
    				 break;
    			 case longClick:
    				 sb.append("onView("+brewSelect(event.object)+").perform(longClick());\n");
    				 break;
    			 case setText:
    				 sb.append("onView("+brewSelect(event.object)+brewPerform(event));
    				 break;
    			 case scrollForward:
    				 sb.append("onView("+brewSelect(event.object)+brewPerform(event));
    				 break;
    			 case back:
    				 sb.append("Espresso.pressBack();\n");
    				 break;
    			 case takeScreenshot:
    				 sb.append("takeScreenshot();\n");
    				 break;
    			 case rotate:
    				 sb.append("rotate();\n");
    				 break;
    			 case swipeLeft:
    				 sb.append("onView(isRoot()).swipeLeft();\n");
    				 break;
    			 case swipeRight:
    				 sb.append("onView(isRoot()).swipeRight();\n");
    				 break;
    			 case swipeUp:
    				 sb.append("onView(isRoot()).swipeUp();\n");
    				 break;
    			 case swipeDown:
    				 sb.append("onView(isRoot()).swipeDown();\n");
    				 break;
    			 case clickRamdomPoint:
    				 sb.append("onView(isRoot()).clickRamdomPoint();\n");
    				 break;
    			 case pressContexMenu:
    				 sb.append("onView(isRoot()).pressMenuKey();\n");
    				 break;
    				 default:break;
    			 }
    		 }
    		 sb.append("\n");
    		 FileOper.appendStringToFile(filePath, sb.toString());
    	 }catch(Exception e){
    		e.printStackTrace();
    	 }
    }
    
    private static String brewSelect(UiNode node){
    	    if(node==null) return "";
    	    String select="";
    	    if(node.getResourceId()!=null&& !node.getResourceId().isEmpty()){
    	        select="withId(\""+node.getResourceId()+"\")";
    	    }else if(node.getContentDesc()!=null&&!node.getContentDesc().isEmpty()){
    	    	select="withContentDescription(\""+node.getContentDesc()+"\")";
    	    }else if(node.getText()!=null&&!node.getContentDesc().isEmpty()){
    	    	select="withText(\""+node.getText()+"\")";
    	    }else {
    	    	select="withXPath(\""+node.getXPath()+"\")";
    	    }
    	    return select;
    }
    
    private static String brewPerform(TEvent event){
    	String perform="";
    	switch(event.action){
    	case click:
    		perform=").perform(click());\n"; break;
    	case longClick:
    		perform=").perform(longClick());\n";break;
    	case setText:
    		 UiNode node=(UiNode)event.object;
    		 if(node.isPassword()){
    			 perform=").perform(clearText(),typeTextIntoFocusedView(password));\n";
    		 }else{
    			 String inputValue="AutoTest"; 
    			 for(TPair param:event.actionParams){
    				 if(param.key.equals("inputValue")){
    					 inputValue=param.value;break;
    				 }
    			 }
    			 perform=").perform(clearText(),typeTextIntoFocusedView(\""+inputValue+"\"));\n";
    		 }
    		 break;
    	case scrollForward:
    		 perform=").perform(scrollTo());\n";
    		 break;
    	default :break;
    		 
    	}
    	return perform;
    }
   
}
