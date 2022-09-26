package com.neutrux.server.NeutruxFileServer.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.neutrux.server.NeutruxFileServer.service.FileUploadService;

@RestController
public class FileUploadController {
	
	private FileUploadService fileUploadService;
	
	@Autowired
	public FileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}
	
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@PostMapping("image-upload")
	public ResponseEntity<String> imageUpload(
		@RequestParam("image") MultipartFile file
	) throws Exception {
		return ResponseEntity.ok( 
			this.fileUploadService.imageUpload(file)
		);
	}

}