package com.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class FileUtil
{
	/**
	 * 图片到byte数组
	 */
	public static byte[] image2byte(String path)
	{
		byte[] data = null;
		FileImageInputStream input = null;
		try
		{
			input = new FileImageInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1)
			{
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		}
		catch (FileNotFoundException ex1)
		{
			ex1.printStackTrace();
		}
		catch (IOException ex1)
		{
			ex1.printStackTrace();
		}
		return data;
	}

	/**
	 * 从图片URL中读取字节数组
	 */
	public static byte[] urlToByte(String url)
	{
		ByteArrayOutputStream baos = null;
		try
		{
			URL u = new URL(url);
			BufferedImage image = ImageIO.read(u);
			baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			baos.flush();

			return baos.toByteArray();
		}
		catch (Exception e)
		{
			Log.error("", e);
		}
		finally
		{
			if (baos != null)
			{
				try
				{
					baos.close();
				}
				catch (IOException e)
				{
					Log.error("", e);
				}
			}
		}
		return null;
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readTxt(String filePathAndName, String encoding) throws IOException
	{
		File dir = new File(filePathAndName);
		if (!dir.exists())
		{
			Log.error("路径输入错误，请检查路径是否正确    dirPath = " + filePathAndName);
			return "";
		}

		StringBuffer str = new StringBuffer("");
		try (BufferedReader bufReader = Files.newBufferedReader(Paths.get(filePathAndName), Charset.forName("UTF-8")))
		{
			String data = "";
			while ((data = bufReader.readLine()) != null)
			{
				str.append(data + " ");
			}
		}
		catch (IOException e)
		{
			Log.error("Read File Error: pathFileName: {}", filePathAndName, e);
		}

		return str.toString();
	}

	/**
	 * 一行一行的读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return
	 * @throws IOException
	 *             返回文本文件的内容
	 */
	public static List<String> readLines(String filePathAndName, String encoding)
	{
		List<String> lines = new ArrayList<String>();
		encoding = encoding.trim();
		int length = 80960;

		FileInputStream fs;
		BufferedReader br = null;
		try
		{
			fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals(""))
			{
				isr = new InputStreamReader(fs);
			}
			else
			{
				isr = new InputStreamReader(fs, encoding);
			}
			br = new BufferedReader(isr, length);
			String line = "";
			while ((line = br.readLine()) != null)
			{
				lines.add(line.trim());
			}
		}
		catch (FileNotFoundException e)
		{
			Log.info("FileNotFoundException: {} ", filePathAndName);
		}
		catch (UnsupportedEncodingException e)
		{
			Log.error("error", e);
		}
		catch (IOException e)
		{
			Log.error("error", e);
		}
		finally
		{
			try
			{
				if (null != br)
					br.close();
			}
			catch (IOException e)
			{
				Log.error("error", e);
			}
		}
		return lines;
	}

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            目录
	 * @return 返回目录创建后的路径
	 */
	public static String createFolder(String folderPath) throws IOException
	{
		String txt = folderPath;
		File myFilePath = new File(txt);
		txt = folderPath;
		if (!myFilePath.exists())
		{
			myFilePath.mkdir();
		}
		return txt;
	}

	/**
	 * 多级目录创建
	 * 
	 * @param folderPath
	 *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
	 * @param paths
	 *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
	 * @return 返回创建文件后的路径 例如 c:myfac
	 */
	public static String createFolders(String folderPath, String paths) throws IOException
	{
		String txts = folderPath;
		String txt;
		txts = folderPath;
		StringTokenizer st = new StringTokenizer(paths, "|");
		while (st.hasMoreTokens())
		{
			txt = st.nextToken().trim();
			if (txts.lastIndexOf("/") != -1)
			{
				txts = createFolder(txts + txt);
			}
			else
			{
				txts = createFolder(txts + txt + "/");
			}
		}
		return txts;
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @return
	 */
	public static void createFile(String filePathAndName, String fileContent) throws IOException
	{
		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists())
		{
			myFilePath.createNewFile();
		}

		FileWriter resultFile = new FileWriter(myFilePath);
		PrintWriter myFile = new PrintWriter(resultFile);
		String strContent = fileContent;
		myFile.println(strContent);
		myFile.close();
		resultFile.close();
	}

	/**
	 * 有编码方式的文件创建
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @param encoding
	 *            编码方式 例如 GBK 或者 UTF-8
	 * @return
	 */
	public static void createFile(String filePathAndName, String fileContent, String encoding) throws IOException
	{
		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists())
		{
			myFilePath.createNewFile();
		}

		PrintWriter myFile = new PrintWriter(myFilePath, encoding);
		String strContent = fileContent;
		myFile.println(strContent);
		myFile.close();
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @return Boolean 成功删除返回true遭遇异常返回false
	 */
	public static boolean delFile(String filePathAndName)
	{
		String filePath = filePathAndName;
		if (filePath.isEmpty())
			return false;

		File myDelFile = new File(filePath);
		if (myDelFile.exists())
		{
			myDelFile.delete();
			return true;
		}

		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 * @return
	 */
	public static void delFolder(String folderPath)
	{
		// 删除完里面所有内容
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		// 删除空文件夹
		myFilePath.delete();
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 * @return
	 */
	public static boolean delAllFile(String path)
	{
		boolean bea = false;
		File file = new File(path);
		if (!file.exists())
		{
			return false;
		}

		if (!file.isDirectory())
		{
			return bea;
		}

		String[] tempList = file.list();
		if (null == tempList)
		{
			return false;
		}

		File temp = null;
		for (int i = 0; i < tempList.length; i++)
		{
			if (path.endsWith(File.separator))
			{
				temp = new File(path + tempList[i]);
			}
			else
			{
				temp = new File(path + File.separator + tempList[i]);
			}

			if (temp.isFile())
			{
				temp.delete();
			}

			if (temp.isDirectory())
			{
				// 先删除文件夹里面的文件
				delAllFile(path + "/" + tempList[i]);

				// 再删除空文件夹
				delFolder(path + "/" + tempList[i]);

				bea = true;
			}
		}

		return bea;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param newPathFile
	 *            拷贝到新绝对路径带文件名
	 * @return
	 * @throws IOException
	 */
	public static void copyFile(String oldPathFile, String newPathFile) throws IOException
	{
		int byteRead = 0;
		File oldfile = new File(oldPathFile);
		if (!oldfile.exists())
			return;

		InputStream oldFs = new FileInputStream(oldPathFile);
		FileOutputStream newFs = new FileOutputStream(newPathFile);
		byte[] buffer = new byte[1444];
		while ((oldFs.read(buffer)) != -1)
		{
			newFs.write(buffer, 0, byteRead);
		}

		newFs.close();
		oldFs.close();
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param oldPath
	 *            准备拷贝的目录
	 * @param newPath
	 *            指定绝对路径的新目录
	 * @return
	 * @throws IOException
	 */
	public static void copyFolder(String oldPath, String newPath) throws IOException
	{
		// 如果文件夹不存在 则建立新文件夹
		new File(newPath).mkdirs();

		File a = new File(oldPath);
		String[] file = a.list();
		if (null == file)
			return;

		File temp = null;
		for (int i = 0; i < file.length; i++)
		{
			if (oldPath.endsWith(File.separator))
			{
				temp = new File(oldPath + file[i]);
			}
			else
			{
				temp = new File(oldPath + File.separator + file[i]);
			}

			if (temp.isFile())
			{
				FileInputStream input = new FileInputStream(temp);
				FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());

				int len;
				byte[] b = new byte[1024 * 5];
				while ((len = input.read(b)) != -1)
				{
					output.write(b, 0, len);
				}

				output.flush();
				output.close();
				input.close();
			}

			if (temp.isDirectory())
			{
				// 如果是子文件夹
				copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
			}
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @throws IOException
	 */
	public static void moveFile(String oldPath, String newPath) throws IOException
	{
		copyFile(oldPath, newPath);

		delFile(oldPath);
	}

	/**
	 * 移动目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @throws IOException
	 */
	public static void moveFolder(String oldPath, String newPath) throws IOException
	{
		copyFolder(oldPath, newPath);

		delFolder(oldPath);
	}

	/**
	 * 文件下载
	 * 
	 * @param urlPath
	 * @param dirPath
	 * @return
	 */
	public static boolean downLoadFromUrl(String urlPath, String dirPath)
	{
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		try
		{
			byte[] buf = new byte[8096];
			int size = 0;

			// 建立链接
			url = new URL(urlPath);
			httpUrl = (HttpURLConnection) url.openConnection();
			// 连接指定的资源
			httpUrl.connect();
			// 获取网络输入流
			bis = new BufferedInputStream(httpUrl.getInputStream());
			// 建立文件
			File file = new File(dirPath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			fos = new FileOutputStream(dirPath);
			// 保存文件
			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();
			httpUrl.disconnect();
		}
		catch (Exception e)
		{
			Log.error("error", e);
			return false;
		}
		finally
		{
			try
			{
				if (null != fos)
					fos.close();

				if (null != bis)
					bis.close();

				if (null != httpUrl)
					httpUrl.disconnect();
			}
			catch (IOException e)
			{
				Log.error("error", e);
			}
		}
		return true;
	}

	/**
	 * 获得文件数据
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath)
	{
		int index = filePath.lastIndexOf("\\");
		if (index < 0)
			index = filePath.lastIndexOf("/");
		if (index < 0)
		{
			return null;
		}
		// 默认地址(测试暂时)
		String fileName = filePath.substring(index + 1, filePath.length());
		return fileName;
	}

	public static String getFileNamePath(String fileName)
	{
		int index = fileName.lastIndexOf("\\.");
		if (index < 0)
			return null;

		// 默认地址(测试暂时)
		String result = fileName.substring(0, index + 1);
		return result;
	}

	/**
	 * echo filepath > data
	 *
	 * @param filePath
	 *            文件路径
	 * @param data
	 *            文件内容
	 */
	public static void echo(String filePath, String data)
	{
		try
		{
			PrintWriter myFile = new PrintWriter(filePath, "UTF-8");
			myFile.print(data);
			myFile.close();
		}
		catch (IOException e)
		{
			Log.error("", e);
		}
	}

	/**
	 * 加载默认的日志配置文件
	 */
	public static void loadLogbackFile()
	{
		File logbackFile = new File("../Lib/config/logback.xml");
		if (logbackFile.exists())
		{
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			try
			{
				configurator.doConfigure(logbackFile);
			}
			catch (JoranException e)
			{
				e.printStackTrace(System.err);
				System.exit(-1);
			}
		}
	}

	/**
	 * 清空文件内容
	 *
	 * @param filePath
	 *            文件路径
	 */
	public static void clearFileContent(String filePath)
	{
		echo(filePath, "");
	}

	public static void delteFiles(String dirPath)
	{
		File dir = new File(dirPath);
		if (!dir.exists())
		{
			Log.error("路径输入错误，请检查路径是否正确    dirPath = " + dirPath);
			return;
		}

		File[] files = dir.listFiles();
		if (files == null)
		{
			Log.error("路径输入错误，请检查路径是否正确    dirPath = " + dirPath);
			return;
		}

		for (File f : files)
		{
			f.delete();
		}
	}
}
