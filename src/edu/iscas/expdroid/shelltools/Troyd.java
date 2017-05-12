package edu.iscas.expdroid.shelltools;

import java.io.File;
import java.io.IOException;

import edu.iscas.expdroid.utils.XmlOper;

public class Troyd {
    static String TROYD=new File(".").getAbsolutePath();
    static String TD="ExpDroidClient";
    static String TDIR=TROYD+"/"+TD;
    static String QUIET=" > /dev/null 2>&1";
    final static String clientPackageName="edu.iscas.expdroidclient";
 

/**
 * reinstall app     
 * @param pkgName  package name of target-app
 * @param apk  the file path of apk
 */
    public static void reInstallApp(String pkgName,String apk){
    	ADB.uninstall(pkgName);
    	ADB.install(apk);
    	
    }
    
    static String META=TDIR+"/AndroidManifest.xml";
    static String ROOT="manifest";
    static String INST="instrumentation";
    static String TAGT="android:targetPackage";
    public static void rename(String pkg){
    	 XmlOper.UpdateXMLNodeAttribute(TAGT, pkg, INST, META);
    }
}
