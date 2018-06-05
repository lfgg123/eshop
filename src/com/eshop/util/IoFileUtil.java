package com.eshop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

import com.jfinal.upload.UploadFile;

public class IoFileUtil {

	public static void saveImages(List<UploadFile> list) {
		try {
			for (int i = 0; i < list.size(); i++) {
				File tmp = list.get(i).getFile();
				long k = tmp.length();
				// File tmp = new
				// File(list.get(i).getUploadPath()+"\\"+list.get(i).getFileName());
				// fileChannelCopy(list.get(i).getFile(),tmp);
				FileInputStream fis = new FileInputStream(tmp);

				File targetDir = new File(list.get(i).getUploadPath() + "/../nmw/");
				if (!targetDir.exists()) {
					targetDir.mkdirs();
				}
				File target = new File(targetDir, list.get(i).getFileName());
				if (!target.exists()) {
					target.createNewFile();
				}

				FileOutputStream fos = new FileOutputStream(target);
				byte[] bts = new byte[1024];
				// int test=fis.read(bts, 0, 1024);
				while (fis.read(bts, 0, 1024) != -1) {
					fos.write(bts, 0, 1024);
				}
				fos.close();
				fis.close();

			}

		} catch (FileNotFoundException e) {
			int test = 0;
			System.out.println(e);

		} catch (IOException e) {

		}
	}

	/**
	 * 发帖专用，上面那个方法被众多模块调用，为了不影响其他部分，暂时独立一个方法来处理。 后面再统一修改。
	 * 
	 * @param list
	 */
	public static String savePostImages(List<UploadFile> list) {
		try {
			StringBuilder imgStringBuilder = new StringBuilder();
			String targetName = "";
			for (int i = 0; i < list.size(); i++) {
				File tmp = list.get(i).getFile();
				long k = tmp.length();
				// File tmp = new
				// File(list.get(i).getUploadPath()+"\\"+list.get(i).getFileName());
				// fileChannelCopy(list.get(i).getFile(),tmp);
				FileInputStream fis = new FileInputStream(tmp);

				File targetDir = new File(list.get(i).getUploadPath() + "/community/");
				if (!targetDir.exists()) {
					targetDir.mkdirs();
				}
				// 保存为新的名称
				String nex = list.get(i).getFileName().split("\\.")[1];
				targetName = Calendar.getInstance().getTimeInMillis() + "." + nex;
				File target = new File(targetDir, targetName);
				if (!target.exists()) {
					target.createNewFile();
				}

				FileOutputStream fos = new FileOutputStream(target);
				byte[] bts = new byte[1024];
				// int test=fis.read(bts, 0, 1024);
				while (fis.read(bts, 0, 1024) != -1) {
					fos.write(bts, 0, 1024);
				}
				fos.close();
				fis.close();
				// 拼图片名称
				imgStringBuilder.append(targetName);
				if (i < list.size() - 1) {
					imgStringBuilder.append(";");
				}

			}
			return imgStringBuilder.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void fileChannelCopy(File s, File t) {

		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public static void cutFile(String oldPath, String newPath) {
		File file1 = new File(oldPath);
		File file2 = new File(newPath);
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		byte[] bytes = new byte[1024];
		int temp = 0;
		try {
			inputStream = new FileInputStream(file1);
			fileOutputStream = new FileOutputStream(file2);
			while ((temp = inputStream.read(bytes)) != -1) {
				fileOutputStream.write(bytes, 0, temp);
				fileOutputStream.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			boolean del = file1.delete();
		}

	}
}
