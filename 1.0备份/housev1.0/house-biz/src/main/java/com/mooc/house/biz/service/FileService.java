package com.mooc.house.biz.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

@Service
public class FileService {
	
	
	//可以使用其他module配置的application.properties中的配置变量
	@Value("${file.path:}")
	private String filePath;
	
	
//	public List<String> getImgPaths(List<MultipartFile> files) {
//	    if (Strings.isNullOrEmpty(filePath)) {
//            filePath = getResourcePath();
//        }
//		List<String> paths = Lists.newArrayList();
//		files.forEach(file -> {
//			File localFile = null;
//			if (!file.isEmpty()) {
//				try {
//					localFile =  saveToLocal(file, filePath);
//					String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
//					paths.add(path);
//				} catch (IOException e) {
//					throw new IllegalArgumentException(e);
//				}
//			}
//		});
//		return paths;
//	}
	
	/**
	 * 获得所有取出绝对路径以外的图片路径字符串
	 * @param files
	 * @return
	 */
	public List<String> getImgPaths(List<MultipartFile> files){
		if(Strings.isNullOrEmpty(filePath)){
			filePath = getResourcePath();
		}
		
		List<String> paths = Lists.newArrayList();
		files.forEach(file -> {
			File localFile = null;
			if(!file.isEmpty()){
				try{
					//把传过来的文件，存到本地
					localFile = saveToLocal(file,filePath);
					//获取所有图片文件的文件路径，去除文件路径
					String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(),filePath);
					System.out.println(path);
					paths.add(path);
				}catch(Exception e){
					throw new IllegalArgumentException(e);
				}
			}
		});
		return paths;
	} 
	
//	public static String getResourcePath(){
//	  File file = new File(".");
//	  String absolutePath = file.getAbsolutePath();
//	  return absolutePath;
//	}
	
	/**
	 * 获得当前文件所在目录绝对路径
	 * @return
	 */
	public static String getResourcePath(){ 
		File file = new File(".");
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}
	
//	private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
//	 File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
//	 if (!newFile.exists()) {
//		 newFile.getParentFile().mkdirs();
//		 newFile.createNewFile();
//	 }
//	 Files.write(file.getBytes(), newFile);
//     return newFile;
//	}

	
	/**
	 * 把传过来的File类型的文件，保存在本地
	 * @param file
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private File saveToLocal(MultipartFile file, String filePath) throws IOException{
		File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
		if(!newFile.exists()){
			newFile.getParentFile().mkdirs();
			newFile.createNewFile();
		}
		Files.write(file.getBytes(), newFile);
		return newFile;
	}
	
	public static void main(String[] args) {
		String a = "aaaad,cbbbbb";
		String b = StringUtils.substringAfterLast(a, ",");
		System.out.println(b);
		
		File file = new File(".");
		String absolutePath = file.getAbsolutePath();
		System.out.println(absolutePath);
		
	}
}
