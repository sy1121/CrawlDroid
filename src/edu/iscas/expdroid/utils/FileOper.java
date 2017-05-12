package edu.iscas.expdroid.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class FileOper {
	  /**
	   * 向在指定文件目录指定内容
	   * @param filePath   文件路径
	   * @param content   待写入的内容
	   */
	  public static  void WriteStringToFile(String filePath,String content) {
	        try {
	        	System.out.println("file path="+filePath);
	            PrintWriter pw = new PrintWriter(new FileWriter(filePath));
	            pw.println(content);
	            pw.close();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	  /**
	   * 追加内容到指定文件
	   * @param filePath
	   * @param content
	   */
	  public static void appendStringToFile(String filePath,String content){
		   try{
			   FileWriter fileWriter=new FileWriter(filePath,true);
			   BufferedWriter bufferWriter=new BufferedWriter(fileWriter);
			   bufferWriter.write(content);
			   bufferWriter.flush();
			   bufferWriter.close();
			   fileWriter.close();
		   }catch(Exception e){
			   e.printStackTrace();
		   }
	  }
	  
	  public static String readFromFile(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	  
	  
	    public static void copyFileToDir(String toDir, File file, String newName) {// 复制文件到指定目录  
	        String newFile = "";  
	        if (newName != null && !"".equals(newName)) {  
	            newFile = toDir + "/" + newName;  
	        } else {  
	            newFile = toDir + "/" + file.getName();  
	        }  
	        File tFile = new File(newFile);  
	        copyFile(tFile, file);// 调用方法复制文件  
	    }  
	  
	    public static void copyFile(File toFile, File fromFile) {// 复制文件  
	        if (toFile.exists()) {// 判断目标目录中文件是否存在  
	            System.out.println("文件" + toFile.getAbsolutePath() + "已经存在，跳过该文件！");  
	            return;  
	        } else {  
	            createFile(toFile, true);// 创建文件  
	        }  
	        System.out.println("复制文件" + fromFile.getAbsolutePath() + "到"  
	                + toFile.getAbsolutePath());  
	        try {  
	            InputStream is = new FileInputStream(fromFile);// 创建文件输入流  
	            FileOutputStream fos = new FileOutputStream(toFile);// 文件输出流  
	            byte[] buffer = new byte[1024];// 字节数组  
	            while (is.read(buffer) != -1) {// 将文件内容写到文件中  
	                fos.write(buffer);  
	            }  
	            is.close();// 输入流关闭  
	            fos.close();// 输出流关闭  
	        } catch (FileNotFoundException e) {// 捕获文件不存在异常  
	            e.printStackTrace();  
	        } catch (IOException e) {// 捕获异常  
	            e.printStackTrace();  
	        }  
	    }  
	    
	    public static void createFile(File file, boolean isFile) {// 创建文件  
	        if (!file.exists()) {// 如果文件不存在  
	            if (!file.getParentFile().exists()) {// 如果文件父目录不存在  
	                createFile(file.getParentFile(), false);  
	            } else {// 存在文件父目录  
	                if (isFile) {// 创建文件  
	                    try {  
	                        file.createNewFile();// 创建新文件  
	                    } catch (IOException e) {  
	                        e.printStackTrace();  
	                    }  
	                } else {  
	                    file.mkdir();// 创建目录  
	                }  
	            }  
	        }  
	    }  
	    
	    
	  
	  
}
