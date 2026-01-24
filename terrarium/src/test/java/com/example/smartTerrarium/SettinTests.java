package com.example.smartTerrarium;

import com.example.smartTerrarium.dto.CreateSettingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SettinTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
        @Test
        void shouldCreateSettingWithImage() throws Exception {

            MockMultipartFile image = new MockMultipartFile(
                    "image",                 // field name in DTO
                    "plant.jpg",             // filename
                    MediaType.IMAGE_JPEG_VALUE,
                    "fake-image-content".getBytes()
            );

            mockMvc.perform(multipart("/settings")
                            .file(image)
                            .param("name", "Tropical Mode")
                            .param("description", "For tropical plants")
                            .param("temperature", "26.5")
                            .param("moisture", "70")
                            .param("waterOverWeek", "2.0")
                            .param("lightStart", "08:00")
                            .param("lightStop", "20:00")
                            .param("wateringMethod", "DRIP")
                            .param("lightVolume", "80")
                            .param("wateringDays", "MONDAY", "THURSDAY")
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andExpect(status().isNoContent());
        }
}
