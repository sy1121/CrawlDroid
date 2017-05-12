package edu.iscas.expdroid.tools;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.android.uiautomator.tree.UiNode;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import edu.iscas.expdroid.model.TEvent;
import edu.iscas.expdroid.model.TEvent.ActionOper;
import edu.iscas.expdroid.model.TEvent.EventType;
import edu.iscas.expdroid.model.TState;
import edu.iscas.expdroid.model.TStatement;
import edu.iscas.expdroid.shelltools.Cmd;
import edu.iscas.expdroid.utils.FileOper;
import edu.iscas.expdroid.utils.TPair;


public class ManualScript {
	 private static Cmd cmd=new Cmd();
	 private static EventHelper eHelper=new EventHelper();
	   //visit the target method in the test class And paser the statement
	   /*
	    *cu: AST for the test class
	    *method: target test method 
	    */
	 
	   //paser test class 
	    public static CompilationUnit getCompilationUnit(InputStream in) {
	        try {
	            CompilationUnit cu;
	            try {
	                // parse the file
	                cu = JavaParser.parse(in,"UTF-8");
	                return cu;
	            } finally {
	                in.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	  public static List<TStatement> PaserTestMethod(CompilationUnit cu,String methodName) {
	    	List<TStatement> rlist=new ArrayList<TStatement>();
	        List<TypeDeclaration> types = cu.getTypes();
	        for (TypeDeclaration type : types) {
	            List<BodyDeclaration> members = type.getMembers();
	            for (BodyDeclaration member : members) {
	                if (member instanceof MethodDeclaration) {
	                    MethodDeclaration method = (MethodDeclaration) member;
	                    BlockStmt block=method.getBody();
	                    if(method.getName().equals(methodName)){
	                    List<Statement> statementList=block.getStmts();
	                    for(Statement statement:statementList){           
	                    	if(statement instanceof ExpressionStmt){
	                    		rlist.add(VisitEspressoin((ExpressionStmt)statement));
	                    	}
	                     }
	                    }
	                }
	            }
	        }
	        return rlist;
	    }
         
	   // paser a certain statement in the target method
	    private static TStatement VisitEspressoin(ExpressionStmt espression){
	    	  MyVoidVisitor mvv=new MyVoidVisitor();
	    	  mvv.visit(espression, null);
	    	  return mvv.ps;
	    }
	    
	    //execute statement
	    public static boolean executeTest(TStatement statement,TState state,TEvent event){
	    	if(statement==null||statement.performs.isEmpty()) return false;
	    	List<TPair> param=new ArrayList<TPair>();
	    	if(statement.type==EventType.SysEvent){
	    		switch(statement.performs.get(0).key){
	    		case "pressBack":
	    			  event.action=ActionOper.back;
	    		      break;
	    		case "swipeRight":
	    			  event.action=ActionOper.swipeRight;
	    		      break;
	    		case "swipeLeft":
	    			  event.action=ActionOper.swipeLeft;
	    		      break;
	    		case "swipeDown":
	    			  event.action=ActionOper.swipeDown;
	    		      break;
	    		case "swipeUp":
	    			  event.action=ActionOper.swipeUp;
	    		      break;
	    		case "takeScreenshot":
	    			  event.action=ActionOper.takeScreenshot;
	    			  String picName="manual_"+state.No;
	    			  if(!statement.performs.get(0).value.equals(""))
	    				  picName=statement.performs.get(0).value;
	    			  param.add(new TPair("picName",picName));
	    			  break;
	    		}
	    		event.eType=EventType.SysEvent;
	    		event.actionParams=param;
	    		event.object=null;
	    	}else if(statement.type==EventType.UIEvent){
	    		switch(statement.performs.get(0).key){
	    		case "click":
	    			event.action=ActionOper.click;
	    			break;
	    		case "longClick":
	    			event.action=ActionOper.longClick;
	    			break;
	    		case "clearText":
	    			event.action=ActionOper.setText;
	    			param.add(new TPair("inputValue",""));
	    			break;
	    		case "typeTextIntoFocusedView":
	    		case "typeText":
	    			event.action=ActionOper.setText;
	    			System.out.println("key="+statement.performs.get(0).key +" pram= "+statement.performs.get(0).value );
	    			String inputValue="";
	    			 if(!statement.performs.get(0).value.equals(""))
	    				 inputValue=statement.performs.get(0).value;
	    			param.add(new TPair("inputValue",inputValue));
	    			break;
	    		}
	    		event.eType=EventType.UIEvent;
	    		UiNode obj=searchUiNode(state,statement.select);
	    		System.out.println("obj="+obj);
	    		if(obj==null) return false;
	    		event.object=obj;
	    		event.actionParams=param;
	    		saveInput(obj,event); //save type-input to file
	    	}
	    	return eHelper.executeEvent(event);
	    }
	    
	   private static void saveInput(UiNode node,TEvent event){
		   if(node==null||node.getResourceId()==null||"".equals(node.getResourceId())
				   ||event==null||event.action!=ActionOper.setText) return;
		   String filePath="./config/inputRecord.txt";
		   TPair param=(TPair)event.actionParams.get(0);
		   String content=param.key.equals("inputValue")?param.value:"";
		  // String fileContent=FileOper.readFromFile(new File(filePath));
		   if(Config.g().preDefineInput.getProperty(node.getResourceId(),"").isEmpty()){
		   Config.g().preDefineInput.put(node.getResourceId(),content); //use for cur explore
		   FileOper.appendStringToFile(filePath, node.getResourceId()+"="+content+"\n");
		   }
		   if(Config.g().preDefineInput.getProperty(node.getXPath(),"").isEmpty()){
			   Config.g().preDefineInput.put(node.getXPath(),content); //use for cur explore
			   FileOper.appendStringToFile(filePath, node.getXPath()+"="+content+"\n");
		   }
	   }
	    
	    /**
	     * select uiNode in the state 
	     * @param state  which wiil been searched
	     * @param selects  select conditions 
	     * @return  if find return UiNode ,or null
	     */
	    private static UiNode searchUiNode(TState state,List<TPair> selects){
	    	for(UiNode node:state.uiSet){
	    		System.out.println("node.id="+node.getResourceId());
	    		System.out.println("node.class="+node.getNClass());
	    		System.out.println("node.text="+node.getText());
	    		boolean match=true;
	    		for(TPair param:selects){
	    			 System.out.println("param.key="+param.key+" param.value="+param.value+" node.text="+node.getText());
	    			 switch(param.key){
	    			 case  "withId": match=param.value.equals(node.getResourceId());break;
	     			 case  "withContentDescription": match=param.value.equals(node.getContentDesc());break;
	    			 case  "withText": match=param.value.equals(node.getText());break;
	    			 case  "withXPath": match=param.value.equals(node.getXPath());break;
	    			 default:break;
	    			 }
	    			 if(!match) break;
	    		}
	    		System.out.println("match="+match);
	    		if(match) return node;
	    	}
	    	return null;
	    }
	    
}
	    //AST visitor
	class MyVoidVisitor extends VoidVisitorAdapter{
	    boolean isOnView=false;
	    boolean isPerform=false;
	    boolean isCheck=false;
	    public TStatement ps=new TStatement();
	    //here , we just focus on MethodCall
		@Override
		public void visit(MethodCallExpr n, Object arg) {
			// TODO Auto-generated method stub
			  switch(n.getName()){
			  case "pressBack":
			  case "takeScreenshot":
			  case "pressKey":
			  case "pressMenuKey":
			  case "swipeLeft":
			  case "swipeRight":
			  case "swipeUp":
			  case "swipeDown":
			  case "scrollTo":
			  {
				    ps.type=EventType.SysEvent;
				    TPair pair=new TPair(n.getName(),n.getArgs().isEmpty()?"":n.getArgs().get(0).toString());
				    ps.performs.add(pair);
				     return ; }		  
			  case "onView":
				  isOnView=true;break;
			  case "inAdapterView":   //for adapterView 
				  isOnView=true;break;
			  case "perform":
			      isPerform=true;break;
			  case "check":
				  isCheck=true;
				  ps.type=EventType.SysEvent;
				  break;
				  default:break;
			  }
			if(n.getArgs().isEmpty()||!(n.getArgs().get(0) instanceof MethodCallExpr)){
				TPair pair=new TPair();
				if(n.getName().startsWith("is")){
					if(n.getParentNode().toString().contains("not"))
					   pair.value="false";
					else 
						pair.value="true";
					pair.key=n.getName();
				}else{
					pair.key=n.getName();
					pair.value=n.getArgs().isEmpty()?"":n.getArgs().get(0).toString().replace("\"", "")+"";
				}
	            if(isOnView){
	            	ps.select.add(pair);
	            }else if(!isOnView&&isPerform){
	            	ps.performs.add(pair);
	            }else if(!isOnView&&isCheck){
	            	ps.performs.add(pair);
	            }
			}
			 super.visit(n, arg);
			  switch(n.getName()){
			  case "onView":
				  isOnView=false;break;
			  case "inAdapterView":
				  isOnView=false;break;
			  case "perform":
			      isPerform=false;break;
			  case "check":
				  isCheck=false;break;
				  default:break;
			  }
		}
	}