package com.example.demo.controller;

import com.example.demo.model.dto.RepositoryDetailsDTO;
import com.example.demo.service.RepositoryDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    @MockBean
    private RepositoryDetailsService repositoryDetailsService;
    private final String owner = "username";
    private final String repositoryName = "repo";

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
        RepositoryDetailsDTO repositoryDetailsDTO = new RepositoryDetailsDTO();
        repositoryDetailsDTO.setOwner(owner);
        repositoryDetailsDTO.setRepositoryName(repositoryName);

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
        RepositoryDetailsDTO updatedDetails = new RepositoryDetailsDTO();
        updatedDetails.setOwner("Hubsio");
        updatedDetails.setRepositoryName("UpdatedRepo");

        mockMvc.perform(MockMvcRequestBuilders.put("/repositories/{owner}/{repositoryName}", updatedDetails.getOwner(), updatedDetails.getRepositoryName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk());
    }
}
