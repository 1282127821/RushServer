package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class AdminCmdRequestor extends Thread {
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	public AdminCmdRequestor(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
	}

	@Override
	public void run() {
		if (reader == null || writer == null) {
			return;
		}

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			close();
		}
	}

	public void send(String line) {
		try {
			writer.write(line);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			close();
		}
	}

	public void close() {
		try {
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("welcome admin... type quit or exit to exit");
		String ip;
		int port;
		AdminCmdRequestor adminCmdRequestor = null;
		if (args.length == 0) {
			ip = "127.0.0.1";
			port = 6100;
			adminCmdRequestor = connect(ip, port);
		} else {
			printConnectHelp();
		}

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() == 0) {
					continue;
				}
				String[] params = line.split(" ");
				if (adminCmdRequestor == null) {
					if (params.length == 3 && params[0].equals("connect")) {
						ip = params[1];
						port = Integer.valueOf(params[2]);
						adminCmdRequestor = connect(ip, port);
					} else if (params[0].equals("exit") || params[0].equals("quit")) {
						System.exit(0);
					} else {
						System.out.println("输入有误!");
						printConnectHelp();
					}
				} else {
					if (params[0].equals("exit") || params[0].equals("quit")) {
						adminCmdRequestor.close();
						System.out.println("服务器连接已经断开");
					} else {
						adminCmdRequestor.send(line);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AdminCmdRequestor connect(String ip, int port) {
		try {
			AdminCmdRequestor adminCmdRequestor = new AdminCmdRequestor(ip, port);
			adminCmdRequestor.start();
			return adminCmdRequestor;
		} catch (UnknownHostException e) {
			System.out.println("服务器主机地址或者端口错误");
		} catch (IOException e) {
			System.out.println("服务器未启动或者连接失败");
		} catch (Exception e) {
			System.out.println("服务器主机地址或者端口错误");
			printConnectHelp();
		}
		return null;
	}

	private static void printConnectHelp() {
		System.out.println("请输入连接的服务器ip & port");
		System.out.println("connect ip port");
	}
}
