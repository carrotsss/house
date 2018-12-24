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
	
	@Value("${file.path:}")
	private String filePath;
	
	
	public List<String> getImgPaths(List<MultipartFile> files) {
	    if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
            System.out.println("file path 为：" + filePath);
        }
		List<String> paths = Lists.newArrayList();
		files.forEach(file -> {
			File localFile = null;
			if (!file.isEmpty()) {
				try {
					localFile =  saveToLocal(file, filePath);
					System.out.println("localFile.getAbsolutePath()" + localFile.getAbsolutePath() + "filePath" + filePath);
					String absolutePath = localFile.getAbsolutePath().replace("\\", "/");
					String path = StringUtils.substringAfterLast(absolutePath, filePath);
					System.out.println("path = " + path);
					paths.add(path);
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		return paths;
	}
	
	public static String getResourcePath(){
	  File file = new File(".");
	  String absolutePath = file.getAbsolutePath();
	  return absolutePath;
	}

	private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
	 File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
	 if (!newFile.exists()) {
		 newFile.getParentFile().mkdirs();
		 newFile.createNewFile();
	 }
	 Files.write(file.getBytes(), newFile);
     return newFile;
	}
	
	public static void main(String[] args) {
		String a = "D:\\tmp-------------\\house\\imgs\\1527787676\\101006tdv3i6ojekjj4znn.jpg";
		a.replace("\\", "/");
		System.out.println(a);
		String b = StringUtils.substringAfterLast(a, ",");
		System.out.println(b);
		
		File file = new File(".");
		String absolutePath = file.getAbsolutePath();
		System.out.println(absolutePath);
		
	}

}
