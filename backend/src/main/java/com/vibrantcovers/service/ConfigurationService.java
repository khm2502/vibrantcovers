package com.vibrantcovers.service;

import com.vibrantcovers.dto.SaveConfigurationRequest;
import com.vibrantcovers.entity.Configuration;
import com.vibrantcovers.repository.ConfigurationRepository;
import com.vibrantcovers.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    
    private final ConfigurationRepository configurationRepository;
    
    public Configuration createConfiguration(String imageUrl, Integer width, Integer height) {
        Configuration configuration = new Configuration();
        configuration.setId(IdGenerator.generateId());
        configuration.setImageUrl(imageUrl);
        configuration.setWidth(width);
        configuration.setHeight(height);
        return configurationRepository.save(configuration);
    }
    
    public Configuration getConfiguration(String id) {
        return configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
    }
    
    @Transactional
    public Configuration updateConfiguration(String id, SaveConfigurationRequest request) {
        Configuration configuration = getConfiguration(id);
        configuration.setColor(request.getColor());
        configuration.setFinish(request.getFinish());
        configuration.setMaterial(request.getMaterial());
        configuration.setModel(request.getModel());
        return configurationRepository.save(configuration);
    }
    
    @Transactional
    public Configuration updateCroppedImage(String id, String croppedImageUrl) {
        Configuration configuration = getConfiguration(id);
        configuration.setCroppedImageUrl(croppedImageUrl);
        return configurationRepository.save(configuration);
    }
}

