package edu.iscas.expdroidclient.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler{
	
	private static final String tag=CrashHandler.class.getSimpleName();
	//系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    private CrashHandler() {
    }

    /**
      * 获取CrashHandler实例 ,单例模式
      */
    public static CrashHandler getInstance() {
         if (INSTANCE == null)
               INSTANCE = new CrashHandler();
               return INSTANCE;
         }
    /**
     * 初始化
     *
     * @param context
     */
   public void init(Context context) {
		 Log.i(tag,"in init");
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
   }
   
   /**
    * 当UncaughtException发生时会转入该函数来处理
    */
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
	   Log.i(tag, "crashOccured");
       if (!handleException(ex) && mDefaultHandler != null) {
             //如果用户没有处理则让系统默认的异常处理器来处理
             mDefaultHandler.uncaughtException(thread, ex);
       } else {
             try {
                   Thread.sleep(3000);
             } catch (InterruptedException e) {
                  // LogUtils.e(e.toString());
             }
             //退出程序
             android.os.Process.killProcess(android.os.Process.myPid());
             System.exit(1);
       }
 }

 /**
   * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
   *
   * @param ex
   * @return true:如果处理了该异常信息;否则返回false.
   */
  private boolean handleException(Throwable ex) {
       if (ex == null) {
             return false;
       }
       Log.i(tag,"exception:"+ex.getMessage());
       ex.printStackTrace();
       Log.i(tag,"handleException");
       
       //通知server
       new Thread(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
		 /*  Looper.prepare();
		   Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_LONG);
		   Looper.loop();*/
			 notifyServer();
		}
    	   
       }.start();
       //收集设备参数信息
       collectDeviceInfo(mContext);
       //保存日志文件
       saveCrashInfo2File(ex);
       return true;
  }
  
  public void notifyServer(){
	    Log.i(tag,"notifyServer");
	    String crashMessage="crash occured at "+ mContext.getApplicationContext().getPackageName();
	    UtilEC.sendMessageBySocket(crashMessage,UtilEC.serverIP,UtilEC.portForException);
 }
  
  /**
   * 收集设备参数信息
   *
   * @param ctx
   */
 public void collectDeviceInfo(Context ctx) {
      try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                  String versionName = pi.versionName == null ? "null" : pi.versionName;
                  String versionCode = pi.versionCode + "";
                  infos.put("versionName", versionName);
                  infos.put("versionCode", versionCode);
            }
       } catch (NameNotFoundException e) {
          //  LogUtils.e("CrashHandleran.NameNotFoundException---> error occured when collect package info", e);
       }
       Field[] fields = Build.class.getDeclaredFields();
       for (Field field : fields) {
            try {
                  field.setAccessible(true);
                  infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
             //     LogUtils.e("CrashHandler.NameNotFoundException---> an error occured when collect crash info", e);
            }
       }
 }
 
 /**
  * 保存错误信息到文件中
  *
  * @param ex
  * @return 返回文件名称, 便于将文件传送到服务器
  */
 private String saveCrashInfo2File(Throwable ex) {
       StringBuffer sb = new StringBuffer();
       sb.append("---------------------sta--------------------------");
       for (Map.Entry<String, String> entry : infos.entrySet()) {
             String key = entry.getKey();
             String value = entry.getValue();
             sb.append(key + "=" + value + "\n");
       }

       Writer writer = new StringWriter();
       PrintWriter printWriter = new PrintWriter(writer);
       ex.printStackTrace(printWriter);
       Throwable cause = ex.getCause();
       while (cause != null) {
             cause.printStackTrace(printWriter);
             cause = cause.getCause();
       }
       printWriter.close();
       String result = writer.toString();
       sb.append(result);
       sb.append("--------------------end---------------------------");
       saveMsgToFile(sb.toString());
    //   LogUtils.e(sb.toString());
       return null;
}
 /**
  * 
  * @param msg 发送的错误信息
  * @return 文件名
  */
 public String  saveMsgToFile(String msg){
	     try{
	    	   long  timestamp=System.currentTimeMillis();
	    	  SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	    	   String  time=formatter.format(new Date());
	    	   String fileName="crash-"+time+"-"+timestamp+"-"+mContext.getPackageName()+".log";
	    	   if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	    		   String path="/mnt/sdcard/crash/"+mContext.getPackageName()+"/";
	    		   File dir=new File(path);
	    		   if(!dir.exists()){
	    			   dir.mkdirs();
	    		   }
	    		   FileOutputStream fos=new FileOutputStream(path+fileName);
	    		   fos.write(msg.getBytes());
	    		   //通过邮件等方式发送给开发人员
	    		   //sendCrashLog2PM();
	    		   fos.close();
	    	   }
	    	   return fileName;
	     }catch( Exception e){
	    	 Log.e(tag,"an error occured while writing file...",e);
	     }
	     return null;
 }

}
