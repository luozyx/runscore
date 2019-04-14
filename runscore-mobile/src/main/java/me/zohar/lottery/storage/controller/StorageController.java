package me.zohar.lottery.storage.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.storage.service.StorageService;
import me.zohar.lottery.storage.vo.StorageVO;

@Controller
@RequestMapping("/storage")
public class StorageController {

	@Autowired
	private StorageService storageService;

	@GetMapping("/fetch/{id:.+}")
	public ResponseEntity<Resource> fetch(@PathVariable String id) {
		StorageVO vo = storageService.findById(id);
		if (vo == null) {
			return ResponseEntity.notFound().build();
		}

		String fileType = vo.getFileType();
		MediaType mediaType = MediaType.parseMediaType(fileType);
		Resource file = storageService.loadAsResource(vo.getId());
		if (file == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(mediaType).body(file);
	}

	@PostMapping("/upload")
	@ResponseBody
	public Result upload(@RequestParam("file_data") MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		String storageId = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), filename);
		return Result.success().setData(storageId);
	}

}
