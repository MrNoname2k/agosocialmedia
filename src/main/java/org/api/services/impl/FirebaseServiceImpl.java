package org.api.services.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.apache.commons.io.FilenameUtils;
import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantFirebase;
import org.api.services.FirebaseService;
import org.api.utils.ApiValidateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@LogExecutionTime
@Service
@Transactional(rollbackFor = {ApiValidateException.class, Exception.class})
public class FirebaseServiceImpl implements FirebaseService {

    public static final String ALIAS = "Image";

    @Override
    public String uploadImage(MultipartFile file, String key, String store) throws Exception {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String fileName = this.getStorageFilename(file, uuidString, key);
        this.storeUpload(file, fileName, store);
        return fileName;
    }

    @Override
    public byte[] readImage(String fileName, String store) throws Exception {
        return this.storeRead(fileName, store);
    }

    @Override
    public String getStorageFilename(MultipartFile file, String uuid, String key) throws Exception {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return key + "-" + uuid + "." + ext;
    }

    @Override
    public String storeUpload(MultipartFile file, String filename, String store) throws Exception {
        Storage storage = StorageClient.getInstance().bucket(ConstantFirebase.FIREBASE_STORAGE_BUCKET).getStorage();
        BlobId blobId = BlobId.of(ConstantFirebase.FIREBASE_STORAGE_BUCKET, store + "/" + filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        return blob.getMediaLink();
    }

    @Override
    public byte[] storeRead(String storageFilename, String store) throws Exception {
        Storage storage = StorageClient.getInstance().bucket(ConstantFirebase.FIREBASE_STORAGE_BUCKET).getStorage();
        BlobId blobId = BlobId.of(ConstantFirebase.FIREBASE_STORAGE_BUCKET, store + "/" + storageFilename);
        Blob blob = storage.get(blobId);
        return blob.getContent();
    }
}
