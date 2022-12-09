package org.piazza.services;

import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MinIOService {

    void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    List<Item> getAllItems(String bucketName);

    String getItem(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException, IOException, NoSuchAlgorithmException, InvalidKeyException;

    String uploadImage(String bucketName, String objectName, MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException, IOException, NoSuchAlgorithmException, InvalidKeyException;

    String uploadVideo(String bucketName, String objectName, MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException, InternalException, IOException, NoSuchAlgorithmException, InvalidKeyException;

    void deleteObject(String URL) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException ;

}
