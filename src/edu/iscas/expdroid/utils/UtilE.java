package edu.iscas.expdroid.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import edu.iscas.expdroid.shelltools.ADB;
import edu.iscas.expdroid.shelltools.ExceCmd;
import edu.iscas.expdroid.tools.Statistic;

public class UtilE {
	private static final long SOCKET_WAIT_TIMEOUT=20000;
	public final static Object mLock=new Object();
    /**
     * format current system time eg: yyyy-MM-dd HH:mm:ss 
     * @return
     */
	public static String getCurTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(new Date());
	}
	
	public static Properties loadConfigFile(String filePath){
		   Properties prop = new Properties();     
           try {
     	     //读取属性文件a.properties
             InputStream in = new BufferedInputStream (new FileInputStream(filePath));
			 prop.load(in);
		   } catch (IOException e) {
		  	 // TODO Auto-generated catch block
		     e.printStackTrace();
		   }    
           return prop;
	}
	/**
	 * deep copy list ,element in list should be Serializable Object
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException { 
	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); 
	    ObjectOutputStream out = new ObjectOutputStream(byteOut); 
	    out.writeObject(src); 
	   
	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray()); 
	    ObjectInputStream in = new ObjectInputStream(byteIn); 
	    @SuppressWarnings("unchecked") 
	    List<T> dest = (List<T>) in.readObject(); 
	    return dest; 
	}
	
	public static boolean waitForSocketMsg(String msg){
		  boolean res=false;
		  ServerSocket serverSocket=null;
		  Socket socket=null;
		  try{
		    System.out.println("wait at port 5051");
		    serverSocket=new ServerSocket(5051);
		    WaitSocketTimeOut t=new WaitSocketTimeOut(serverSocket);
		    t.start();
				if((socket=serverSocket.accept())!=null){ 
					System.out.println("waitForSocketMsg accept info from client");
	     			//input  
	   	            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));  
	   	            //output
	   	            PrintWriter pw=new PrintWriter(socket.getOutputStream());  
	   	            //read info
	   	            String info="";  
	   	            StringBuffer sb=new StringBuffer();
	   	            if(!((info=br.readLine())==null)){  
	   	            	sb.append(info);
	   	            }  
	   	            System.out.println("accept:"+sb.toString());
	   	            if(!sb.toString().contains("timeout")) res=true;
	   	            //reply to client  
	   	            String reply="welcome";  
	   	            pw.write(reply);  
	   	            pw.flush();  
	   	            //关闭资源  
	   	            pw.close();  
	   	            br.close();  
	   	            socket.close();   
	   	            t.exit=true;
				}
		 }catch(Exception e){
			 e.printStackTrace();
			 System.out.println("error occur:"+e.getMessage());
		 }finally{
			 //close socke-server
			 synchronized(UtilE.class){
			  if(serverSocket!=null){
				    try{
						serverSocket.close();
						System.out.println("socket close finish");
						
					}catch(IOException e){
						e.printStackTrace();
					}
			   }
			 }
		 }
		  return res;
	}
	
	private static class WaitSocketTimeOut extends Thread{
		private ServerSocket serverSocket;
		public boolean exit=false;
		public WaitSocketTimeOut(ServerSocket ss){
			serverSocket=ss;
        }
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
			long startMills=System.currentTimeMillis();
			long currentMills=0;
			System.out.println("waitForTimeout Thread start!");
			while(true){
				if(exit) break;
				Thread.sleep(100); //sleep 100 ms
				currentMills=System.currentTimeMillis()-startMills;
				if(currentMills>SOCKET_WAIT_TIMEOUT){
					System.out.println("expired time "+currentMills);
					synchronized(UtilE.class){
					if(serverSocket!=null&&!serverSocket.isClosed()){
						System.out.println("close socket after timeout");
						//serverSocket.close();
						sendMessageBySocket("timeout","localhost",5051);
					  }
					}
					 break;
					 //new Socket("localhost",5051);
				}else {
					//System.out.println("wait time elapsed: "+currentMills);
				}
			}
			}catch(Exception e){
				//e.printStackTrace();
				System.out.println("socket timeout "+e.getMessage());
			}
		}
		
	}
	
	
	public static void sendMessageBySocket(String msg,String serverIP,int serverPort){
		try { 
			// 1.建立客户端socket连接，指定服务器位置及端口
			System.out.println("begin socket");
			Socket socket = new Socket(serverIP,serverPort);
			System.out.println("end socket");
			// 2.得到socket读写流
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			// 输入流
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			// System.out.println(outMsg);
			pw.write(msg + System.getProperty("line.separator"));
			pw.flush();
			// socket.shutdownOutput();
			System.out.println("发送成功");
			// 接收服务器的相应
			String reply = null;
			while (!((reply = br.readLine()) == null)) {
				System.out.println("接收服务器的信息：" + reply);
			}
			// 4.关闭资源
			br.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
	
    public static String getCurrentActivityName(){
    	try{
	       String pull=ADB.acmd+" pull /sdcard/expDroid/an.txt "+Statistic.outDirPath;
		   ExceCmd.getInstance().execCommand(pull);
		   String actName=FileOper.readFromFile(new File(Statistic.outDirPath+"an.txt"));
		   return actName;
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	return "";
   }
	
	
}
