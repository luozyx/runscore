package me.zohar.lottery.storage.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.storage.domain.Storage;
import me.zohar.lottery.storage.repo.StorageRepo;
import me.zohar.lottery.storage.vo.StorageVO;

@Service
public class StorageService {
	
	@Value("${storage.storagePath:#{null}}")
	private String storagePath;

	@Autowired
	private StorageRepo storageRepo;

	public StorageVO findById(String id) {
		return StorageVO.convertFor(storageRepo.getOne(id));
	}

	public Resource loadAsResource(String id) {
		try {
			Path file = Paths.get(storagePath).resolve(id);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String store(InputStream inputStream, Long fileSize, String fileType, String fileName) {
		String id = IdUtils.getId();
		try {
			Files.copy(inputStream, Paths.get(storagePath).resolve(id), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Failed to store file " + id, e);
		}

		Storage storage = new Storage();
		storage.setId(id);
		storage.setFileName(fileName);
		storage.setFileType(fileType);
		storage.setFileSize(fileSize);
		storage.setUploadTime(new Date());
		storageRepo.save(storage);
		return id;
	}

}
