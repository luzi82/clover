package com.luzi82.adb;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public class Adb {

	File adbFile;

	public Adb(File adbFile) {
		this.adbFile = adbFile;
	}

	public void check() {

	}

	public String getVersion() throws IOException, InterruptedException {
		String stdOut = processToString("version");
		Pattern p = Pattern.compile("Android Debug Bridge version ([^\\n]+)\\n");
		Matcher m = p.matcher(stdOut);
		m.find();
		return m.group(1);
	}

	public String getRevision() throws IOException, InterruptedException {
		String stdOut = processToString("version");
		Pattern p = Pattern.compile("Revision ([^\\n]+)\\n");
		Matcher m = p.matcher(stdOut);
		m.find();
		return m.group(1);
	}

	static public class Device {
		public enum State {
			OFFLINE("offline"), //
			DEVICE("device"), //
			NO_DEVICE("no device"); //

			String name;

			State(String x) {
				name = x;
			}

			@JsonValue
			public String toString() {
				return name;
			}

			public static State fromAdb(String name) {
				for (State s : State.values()) {
					if (s.name.equals(name))
						return s;
				}
				return null;
			}
		}

		public String serial_number;
		public State state;
	}

	public List<Device> getDeviceList() throws IOException, InterruptedException {
		String stdOut = processToString("devices");

		BufferedReader reader = new BufferedReader(new StringReader(stdOut));
		String line = null;
		line = reader.readLine();
		if (!line.equals("List of devices attached")) {
			throw new Error(String.format("[FSCMPCSR] Unexpected adb output: %s", line));
		}

		List<Device> ret = new LinkedList<Adb.Device>();

		Pattern pattern = Pattern.compile("([^\\t]+)\\t([^\\t]+)");
		while (true) {
			line = reader.readLine();
			if (line.isEmpty())
				break;
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches()) {
				throw new Error(String.format("[QZSCEBJS] Unexpected adb output: %s", line));
			}
			Device device = new Device();
			device.serial_number = matcher.group(1);
			device.state = Device.State.fromAdb(matcher.group(2));
			ret.add(device);
		}

		line = reader.readLine();
		if (line != null) {
			throw new Error(String.format("[PUKQHIDM] Unexpected adb output: %s", line));
		}

		return ret;
	}

	public byte[] screencap(String deviceId) throws IOException, InterruptedException {
		byte[] retBa = process("-s", deviceId, "shell", "screencap", "-p");
		ByteArrayOutputStream baos = new ByteArrayOutputStream(retBa.length);
		boolean d = false;
		for (byte b : retBa) {
			if (b == 0x0a) {
				d = false;
				baos.write(b);
			} else if (b == 0x0d) {
				if (d) {
					baos.write(0x0d);
				}
				d = true;
			} else {
				if (d) {
					baos.write(0x0d);
					d = false;
				}
				baos.write(b);
			}
		}
		return baos.toByteArray();
	}

//	public byte[] screencap(String deviceId) throws IOException, InterruptedException {
//		process("-s", deviceId, "shell", "screencap", "/sdcard/tmp.jpg");
//		process("-s", deviceId, "pull", "screencap", "/sdcard/tmp.jpg");
//		process("-s", deviceId, "shell", "rm", "/sdcard/tmp.jpg");
//		byte[] retBa = FileUtils.readFileToByteArray(new File("tmp.jpg"));
//		ByteArrayOutputStream baos = new ByteArrayOutputStream(retBa.length);
//		boolean d = false;
//		for (byte b : retBa) {
//			if (b == 0x0a) {
//				d = false;
//				baos.write(b);
//			} else if (b == 0x0d) {
//				if (d) {
//					baos.write(0x0d);
//				}
//				d = true;
//			} else {
//				if (d) {
//					baos.write(0x0d);
//					d = false;
//				}
//				baos.write(b);
//			}
//		}
//		return baos.toByteArray();
//	}
	
	public void tap(String deviceId, int x, int y) throws IOException, InterruptedException {
		process("-s", deviceId, "shell", "input", "tap", Integer.toString(x), Integer.toString(y));
	}

	public String processToString(String... argv) throws IOException, InterruptedException {
		byte[] retBa = process(argv);
		return new String(retBa);
	}

	public byte[] process(String... argv) throws IOException, InterruptedException {
		List<String> cmd = new LinkedList<String>();
		cmd.add(adbFile.getAbsolutePath());
		for (String arg : argv) {
			cmd.add(arg);
		}
		Process process = Runtime.getRuntime().exec(cmd.toArray(new String[0]));
		InputStream is = process.getInputStream();
		byte[] retBa = IOUtils.toByteArray(is);
		is.close();
		process.waitFor();
		return retBa;
	}

}
