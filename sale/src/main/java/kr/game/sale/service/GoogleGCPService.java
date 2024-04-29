package kr.game.sale.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleGCPService {
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    public String updateImageInfo(MultipartFile dto,String url) throws IOException {
        if (dto != null) {
        String uuid = UUID.randomUUID().toString();
        String ext = dto.getContentType();
        // Cloud에 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, url + "/" + uuid)
                        .setContentType(ext)
                        .build(),
                dto.getInputStream()
        );
        blobInfo.getName();
        log.info("Name={}", blobInfo.getName());
        log.info("blobInfo = {}", blobInfo.getMediaLink());
        return blobInfo.getMediaLink();
        }
        return "false";
    }
    // GCP 이미지 지우기
//    public void deleteImageInfo(String image){
//        BlobId blodId = BlobId.of(bucketName,image);
//        log.info(blodId.getName());
//        storage.delete(blodId);
//    }
}
