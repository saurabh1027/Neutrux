package com.neutrux.server.NeutruxFileServer.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
	public String imageUpload(MultipartFile file) throws Exception;
}
