package com.example.myblogtest.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadFileUserProfile {
    private String name;
    private String fileDownloadUri;
    private String type;
    private Long size;
}
