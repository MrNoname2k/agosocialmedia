package org.api.services;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseService {

    public String uploadImage(MultipartFile file, String key, String store) throws Exception;

    public byte[] readImage(String fileName, String store) throws Exception;

    public String getStorageFilename(MultipartFile file, String uuid, String key) throws Exception;

    public String storeUpload(MultipartFile file, String storageFilename, String store) throws Exception;

    public byte[] storeRead(String storageFilename, String store) throws Exception;

}
