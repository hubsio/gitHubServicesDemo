package com.example.demo.controller;

import com.example.demo.service.GitHubDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubDataControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GitHubDataService gitHubDataService;

    @Test
    void testGetRepositoryDetails() throws Exception {
        String owner = "hubsio";
        String repo = "medical-clinic";
        mockMvc.perform(MockMvcRequestBuilders.get("/github-details/{owner}/{repo}",owner, repo)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserRepositories() throws Exception {
        String owner = "hubsio";
        mockMvc.perform(MockMvcRequestBuilders.get("/github-details/{owner}", owner)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
