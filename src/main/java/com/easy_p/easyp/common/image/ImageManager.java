package com.easy_p.easyp.common.image;

import com.easy_p.easyp.common.image.dto.ImageURLDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageManager {
    private final RestTemplate restTemplate;
    @Value("${image-server-base-url}")
    private String baseUrl;

    public String saveImage(MultipartFile image){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            ByteArrayResource fileAsResource = new ByteArrayResource(image.getBytes()){
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            body.add("image", fileAsResource);
            HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<ImageURLDto> response =
                    restTemplate.exchange(baseUrl + "/image/upload", HttpMethod.POST, requestEntity, ImageURLDto.class);

            ImageURLDto imageURLDto = response.getBody();
            return imageURLDto.getImageUrl();
        } catch (IOException e){
            throw new RuntimeException();
        }
    }

    public void deleteImage(String imageUrl){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            String url = UriComponentsBuilder.fromUriString(baseUrl + "/image/delete")
                    .queryParam("imageUrl", imageUrl).toUriString();

            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
        }
        catch (HttpClientErrorException e) {
            throw new RuntimeException("이미지 삭제 요청 실패: " + e.getMessage(), e);
        }
    }
}
