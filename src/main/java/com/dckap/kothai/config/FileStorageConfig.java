package com.dckap.kothai.config;

import com.dckap.kothai.constant.FileConfigConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = FileConfigConstants.FILE_UPLOAD_PATH)
public class FileStorageConfig {

	private List<String> folders;

	private String base;

}
