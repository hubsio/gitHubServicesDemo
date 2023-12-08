package com.example.demo.controller;

import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.model.entity.RepositoryDetails;
import com.example.demo.repository.RepositoryDetailsRepository;
import com.example.demo.service.RepositoryDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RepositoryDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RepositoryDetailsRepository repositoryDetailsRepository;
    @MockBean
    private RepositoryDetailsService repositoryDetailsService;
    private final String owner = "username";
    private final String repositoryName = "repo";

    @BeforeEach
    public void setUp() {
        Optional<RepositoryDetails> existingUser = repositoryDetailsRepository.findByOwnerAndRepositoryName("doctor@test.com", "name");
        if (existingUser.isEmpty()) {
            RepositoryDetails repositoryDetails = new RepositoryDetails(1L, "owner", "doctor@test.com", "password", "clone", 3, LocalDateTime.now());
            repositoryDetailsRepository.save(repositoryDetails);
        }
    }

    @Test
    public void testSaveRepositoryDetails() throws Exception {
        RepositoryDetailsDTO repositoryDetailsDTO = RepositoryDetailsDTO.builder()
                .owner(owner)
                .repositoryName(repositoryName)
                .description("Sample description")
                .cloneUrl("https://github.com/sample/repo.git")
                .stars(10)
                .createdAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repositoryDetailsDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetRepositoryDetails() throws Exception {
        RepositoryDetailsDTO repositoryDetailsDTO = RepositoryDetailsDTO.builder()
                .owner(owner)
                .repositoryName(repositoryName)
                .build();

        when(repositoryDetailsService.getRepositoryDetails(anyString(), anyString()))
                .thenReturn(Optional.of(repositoryDetailsDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value(owner))
                .andExpect(jsonPath("$.repositoryName").value(repositoryName));
    }

    @Test
    public void testDeleteRepository() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateRepositoryDetails() throws Exception {
        RepositoryDetailsDTO updatedDetails = RepositoryDetailsDTO.builder()
                .owner("Hubsio")
                .repositoryName("UpdatedRepo")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/repositories/{owner}/{repositoryName}", updatedDetails.getOwner(), updatedDetails.getRepositoryName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk());
    }
}
