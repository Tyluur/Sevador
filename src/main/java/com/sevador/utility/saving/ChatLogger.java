package com.sevador.utility.saving;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sevador.utility.Constants;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ChatLogger {

	public static void logMessage(String[] format) throws IOException {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final Calendar calendar = Calendar.getInstance();
		File file = new File(Constants.CHAT_LOG_FILE + "/"+format[0]+".txt");
		if (!file.exists())
			file.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.CHAT_LOG_FILE+ "/"+format[0]+".txt", true));
		bw.write("[" + dateFormat.format(calendar.getTime()) + " - "+format[2]+"]" + format[0] + " sent chat message: " + format[1] + "");
		bw.newLine();
		bw.flush();
		bw.close();
	}

}
