package com.sevador.utility;

import java.io.OutputStream;
import java.io.PrintStream;

import com.sevador.Main;

/**
 * Used for Custom Text Logging.
 * 
 * @author Tyluur
 *
 */
public class ServerLogger extends PrintStream {

	/**
	 * Sets the Out Stream.
	 * 
	 * @param out
	 */
	public ServerLogger(OutputStream out) {
		super(out);
	}
	
	@Override
	public void println(Object x) {
		Main.getLogger().info(x.toString());
	}

	@Override
	public void println(String x) {
		/*SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Throwable throwable = new Throwable();
		String date = sdf.format(new Date());
		String className = throwable.getStackTrace()[1].getFileName().replace(".java", ""), methodName = throwable.getStackTrace()[1].getMethodName();
		super.println("[" + date + " | " + className + " | " + methodName + "] " + x);*/
		Main.getLogger().info(x);
	}

}
