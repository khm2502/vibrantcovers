package com.vibrantcovers.dto;

import lombok.Data;

@Data
public class UploadThingCallbackRequest {
    private FileData file;
    private Metadata metadata;
    
    @Data
    public static class FileData {
        private String url;
        private String name;
        private Long size;
        private String type;
    }
    
    @Data
    public static class Metadata {
        private InputData input;
    }
    
    @Data
    public static class InputData {
        private String configId;
    }
}

