package edu.iscas.expdroid.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.UiNode;

public class inputHelper {
	public enum strType{num,alpha,alphanumeric};
	public enum specialType{email,phoneNum,username,paseword,birthday,nickname,address};
    public static String generateTextInput(UiNode node){
    	String inputValue="AutoTest";
    	//first check predefined-input config file
    	System.out.println("in generage input resourceId"+Config.g().preDefineInput.getProperty(node.getResourceId(),""));
    	System.out.println("in generage input xpath"+Config.g().preDefineInput.getProperty(node.getXPath(),""));
    	 if(!Config.g().preDefineInput.getProperty(node.getResourceId(),"").equals("")){
			 inputValue=Config.g().preDefineInput.get(node.getResourceId())+"";
			 if(!inputValue.trim().isEmpty())
    	     return inputValue;
    	 }
    	 if(!Config.g().preDefineInput.getProperty(node.getXPath(),"").equals("")){
			 inputValue=Config.g().preDefineInput.get(node.getResourceId())+"";
			 if(!inputValue.trim().isEmpty())
    	     return inputValue;
    	 }
    	//guess input from neighbour node text
    	 UiNode parent=null;
    	 if(node.getParent()!=null){  //up-1-level-parent 
    		 parent =(UiNode)node.getParent();
    		 if(parent.getParent()!=null){ //up-2-level-parent
    			 parent=(UiNode)node.getParent();
    		 }
    	 }
    	 List<String> texts=new ArrayList<String>();
    	 //bfs to collect text-infos
    	 if(parent!=null){
    		 int level=0;
        	 Queue<UiNode> q=new LinkedList<UiNode>();
        	 q.offer(parent);
        	 while(!q.isEmpty()){
        		 UiNode cur=q.poll();
        		 if(cur.getText()!=null&&!"".equals(cur.getText())) 
        			 texts.add(cur.getText());
        		 for(BasicTreeNode child:cur.getChildren()){
        			 q.offer((UiNode)child);
        		 }
                 level++;
                 if(level==2) break;
        	 }
    	 }
    	 for(String text:texts){
    		 //check such as 
    		 if(text.contains("如")
    				 ||text.contains("such as")
    				 ||text.contains("e.g")
    				 ||text.contains("eg.")){
    			 int index=-1;
    			 index=text.indexOf("如")==-1?-1:text.indexOf("如");
    			 index=text.indexOf("such as")==-1?-1:text.indexOf("such as");
    			 index=text.indexOf("e.g")==-1?-1:text.indexOf("e.g");
    			 index=text.indexOf("eg.")==-1?-1:text.indexOf("eg.");

    			 if(index!=-1){
    			 String[] examples=text.substring(index).split(",");
    			 if(examples.length>0) {
    				 inputValue=examples[0];
    			     break; 
    			 }
    			 }
    		 }
    		 
    		 //check email eg: email 邮箱
    		 if(text.contains("email")||text.contains("邮箱")){
    			 inputValue="623559714@qq.com";
    			 break;
    		 }
    		 //check phone
    		 if(text.contains("phone")||text.contains("电话")||text.contains("手机号码")){
    			 inputValue="18401698098";
    			 break;
    		 }
    		 
    		 
    	 }
    	 if(!texts.isEmpty()){
    		 Random r=new Random(System.currentTimeMillis());
			 inputValue=texts.get(r.nextInt(texts.size()));
			 return inputValue;
		 }
    	   //guess by type eg: number string or num+str
    	    Random r=new Random();
    	    r.setSeed(System.currentTimeMillis());
    	    int s=r.nextInt(3);
    	    switch(s){
    	    case 0:inputValue=getRandomString(8,strType.num);
    	    	break;
    	    case 1:inputValue=getRandomString(8,strType.alpha);
	    	    break;
    	    case 2:inputValue=getRandomString(8,strType.alphanumeric);
	    	    break;
	    	   default:inputValue=getRandomString(8,strType.alphanumeric);
    	    }
    	    //inputValue=getRandomString(8,strType.alphanumeric);
    	    return inputValue;
    }
    
    public static String getRandomString(int length,strType type){
        String base_num="0123456789";
        String base_alpha="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	String base_alphanumeric="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String cur_base="";
    	switch(type){
        case num: cur_base=base_num;break;
        case alpha:cur_base=base_alpha;break;
        case alphanumeric:cur_base=base_alphanumeric;break;
        default:cur_base=base_alphanumeric;break;
        }
        Random random=new Random();
        random.setSeed(System.currentTimeMillis());
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
          int number=random.nextInt(cur_base.length());
          sb.append(cur_base.charAt(number));
        }
        return sb.toString();
    }
    
    public static String getSpecialString(specialType stype){
    	StringBuilder sb=new StringBuilder("");
    	/*switch(stype){
    	case email:sb.append();break;
    	case phoneNum:sb.append();break;
    	case alphanumeric:sb.append();break;
    	}*/
    	return sb.toString();
    }
    
}
