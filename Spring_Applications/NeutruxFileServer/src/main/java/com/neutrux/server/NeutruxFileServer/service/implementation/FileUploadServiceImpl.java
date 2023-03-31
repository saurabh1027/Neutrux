package com.neutrux.server.NeutruxFileServer.service.implementation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.neutrux.server.NeutruxFileServer.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	private final Path root = Paths
		.get("C:\\Users\\Lenovo\\OneDrive\\Documents\\Projects\\Neutrux\\Angular\\NeutruxApp\\src\\assets\\blog_pictures");

	@Override
	public String imageUpload(MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename().toString().replace(' ', '_');
		try {
			Files.copy(file.getInputStream(), this.root.resolve( fileName ));
		} catch (Exception e) {
			System.out.println(e);
		}
		return fileName;
	}

}
