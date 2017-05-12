package edu.iscas.expdroid.shelltools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExceCmd {
	private static ExceCmd instance = new ExceCmd();

	private ExceCmd() {
	}

	public static ExceCmd getInstance() {
		return instance;
	}

	public int execCommand(String command, StringBuffer result) {
		Runtime runtime = Runtime.getRuntime();
		System.out.println("command: " + command.toString());
		Process proc =null;
		try {
		    proc = runtime.exec(command);
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " + proc.exitValue());
				StringBuffer errorMessage = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					errorMessage.append(line + " ");
				}
				System.out.println("error occured :" + errorMessage.toString());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				result.append(line + " ");
			}
			System.out.println("result: " + result.toString());
			return 0;
		} catch (InterruptedException | IOException e) {
			System.err.println(e);
			return 1;
		} finally {
			try {
				if(proc!=null)
				proc.destroy();
			 } catch (Exception e2) {
			}
		}
	}
/**
 * execute not wait finished 
 * @param command
 * @throws IOException
 * @throws InterruptedException
 */
	public void execCommandExitVlaue(String command){
		try{
		Runtime runtime = Runtime.getRuntime();
		System.out.println("command: " + command.toString());
		Process proc = runtime.exec(command);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void execCommand(String command) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		System.out.println("command: " + command.toString());
		Process proc =null;
		try {
		    proc = runtime.exec(command);
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " + proc.exitValue());
				StringBuffer errorMessage = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					errorMessage.append(line + " ");
				}
				System.out.println("error occured :" + errorMessage.toString());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				stringBuffer.append(line + " ");
			}
			System.out.println(stringBuffer.toString());

		} catch (InterruptedException e) {
			System.err.println(e);
		} finally {
			try {
				proc.destroy();
			} catch (Exception e2) {
			}
		}
	}

	public String execCommandForResult(String command) {
		Runtime runtime = Runtime.getRuntime();
	//	System.out.println("command: " + command.toString());
		Process proc =null;
		StringBuffer result = new StringBuffer();
		try {
		    proc = runtime.exec(command);
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " + proc.exitValue());
				StringBuffer errorMessage = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					errorMessage.append(line + " ");
				}
				System.out.println("error occured :" + errorMessage.toString());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				result.append(line + " ");
			}
			return result.toString();
		} catch (InterruptedException | IOException e) {
			System.err.println(e);
			return "";
		} finally {
			try {
				proc.destroy();
			} catch (Exception e2) {
			}
		}
	}

	public void execCommand(String[] command){
		Runtime runtime = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = runtime.exec(command);
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " + proc.exitValue());
				StringBuffer errorMessage = new StringBuffer();
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					errorMessage.append(line + " ");
				}
				System.out.println("error occured :" + errorMessage.toString());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			while ((line = in.readLine()) != null) {
				stringBuffer.append(line + " ");
			}
			System.out.println(stringBuffer.toString());

		} catch (InterruptedException | IOException e) {
			System.err.println(e);
		} finally {
			try {
				proc.destroy();
			} catch (Exception e2) {
			}
		}
	}
}
