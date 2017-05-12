package edu.iscas.expdroidclient.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;
import android.util.Log;

public class UtilEC {
	private static final String TAG=UtilEC.class.getSimpleName();
	public static final String serverIP="133.133.134.100"; //133.133.134.100 //10.0.2.2
	public static final int portForException=5050;
    public static final int portForSocketMsg=5051;
	   /**
     * format current system time eg: yyyy-MM-dd HH:mm:ss 
     * @return
     */
	public static String getCurTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(new Date());
	}
	
	public static void appendToFile(String filePath,String content){
		try{ 
			File file=new File(filePath);
			if(!file.exists()){
				File dir=new File(file.getParent());
			    dir.mkdirs();
			    file.createNewFile();
			}
			
			FileOutputStream out=new FileOutputStream(file,true);
			out.write(content.getBytes());
			out.close();
		}catch(Exception e){
		    e.printStackTrace();
		}
	}
	
	public static void saveToFile(String filePath,String content){
		try{ 
			File file=new File(filePath);
			if(!file.exists()){
				File dir=new File(file.getParent());
			    dir.mkdirs();
			    file.createNewFile();
			}
			
			FileOutputStream out=new FileOutputStream(file,false);
			out.write(content.getBytes());
			out.close();
		}catch(Exception e){
		    e.printStackTrace();
		}
	}
	
	
	public static void writeToFile(String fileName,String write_str){   
		 try{   
		  
		       FileOutputStream fout = new FileOutputStream(fileName);   
		       byte [] bytes = write_str.getBytes();   
		  
		       fout.write(bytes);   
		       fout.close();   
		     }catch(Exception e){   
		        e.printStackTrace();   
		    }   
     }   
	
	public static String readFromFile(String fileName){   
		  String res="";   
		    try{   
		         FileInputStream fin = new FileInputStream(fileName);   
		         int length = fin.available();   
		         byte [] buffer = new byte[length];   
		         fin.read(buffer);       
			     res = EncodingUtils.getString(buffer, "UTF-8");   
		         fin.close();  
		         
		        }catch(Exception e){   
		         e.printStackTrace();   
		       }   
		        return res;   
		}   
	
	  //删除文件
		public static void delFile(String filePath){
			File file = new File(filePath);
			if(file.isFile()){
				file.delete();
	        }
		}
		
		//删除文件夹和文件夹里面的文件
		public static void deleteDir(String dirPath) {
			File dir = new File(dirPath);
			if (dir == null || !dir.exists() || !dir.isDirectory())
				return;
			
			for (File file : dir.listFiles()) {
				if (file.isFile())
					file.delete(); // 删除所有文件
				else if (file.isDirectory())
					deleteDir(file.getPath()); // 递规的方式删除文件夹
			}
			//dir.delete();// 删除目录本身
		}
		
		
		
		public static void  createFile(String filePath){
			File file=new File(filePath);
			File dir=file.getParentFile();
			if(!dir.exists()||!dir.isDirectory()){
				dir.mkdirs();
			}
			    try {
					if(!file.createNewFile())
                          Log.e(TAG, "file create failed!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	
	
	 public static void sendMessageBySocket(String msg,String serverIP,int serverPort){
			try { 
				// 1.建立客户端socket连接，指定服务器位置及端口
				System.out.println("begin socket");
			    System.out.println("socket ip ="+ serverIP);
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
	
	
}
