package edu.iscas.expdroid.utils;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class XmlOper {
	 public static boolean doc2XmlFile(Document document,String filename) 
	    { 
	      boolean flag = true; 
	      try 
	       { 
	            /** 将document中的内容写入文件中   */ 
	             TransformerFactory tFactory = TransformerFactory.newInstance();    
	             Transformer transformer = tFactory.newTransformer();  
	            /** 编码 */ 
	            transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312"); 
	             DOMSource source = new DOMSource(document);  
	             StreamResult result = new StreamResult(new File(filename));    
	             transformer.transform(source, result);  
	         }catch(Exception ex) 
	         { 
	             flag = false; 
	             ex.printStackTrace(); 
	         } 
	        return flag;       
	    }
	 
	 public static Document load(String filename) 
	    { 
	       Document document = null; 
	      try  
	       {  
	            DocumentBuilderFactory   factory = DocumentBuilderFactory.newInstance();    
	            DocumentBuilder builder=factory.newDocumentBuilder();    
	            document=builder.parse(new File(filename));    
	            document.normalize(); 
	       } 
	      catch (Exception ex){ 
	           ex.printStackTrace(); 
	       }   
	      return document; 
	    }
	   /** 
	     *   演示修改文件的具体某个节点的值  
	     */ 
	   public static void UpdateXMLNodeAttribute(String targetAttribute,String newValue,String nodename, String xmlFilePath) 
	    { 
	       Document document = load(xmlFilePath); 
	       Element ele=null;
		   if(document==null) return ;
	       Node root=document.getDocumentElement();
	       Queue<Node> q=new LinkedList<Node>();
	       if(root==null) return ;
	       else q.offer(root);
	       while(!q.isEmpty()){
	    	   Node curNode=q.poll();
	    	   if(curNode.getNodeName().equals(nodename)){
	    		   ele=(Element)curNode; break;
	    	   }
	    	   if(curNode.hasChildNodes()){
	    		   for(int i=0;i<curNode.getChildNodes().getLength();i++){
	    			   q.offer(curNode.getChildNodes().item(i));
	    		   }
	    	   }
	       }
	       if(ele!=null)
	       ele.setAttribute(targetAttribute,newValue);
	       doc2XmlFile(document,xmlFilePath); 
	    }
	   
	   public static void main(String args[])throws Exception
	   {
		   
		  // XmlOper.UpdateTarget("","");
	   }
}
