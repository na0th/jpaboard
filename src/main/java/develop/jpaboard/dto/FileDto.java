package develop.jpaboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class FileDto {

        private Long id;
        private String fileName;
        private String contentType;
        private Long size;
        private byte[] data;

        public String getBase64Data() {
            return Base64.getEncoder().encodeToString(this.data);
        }
    }
