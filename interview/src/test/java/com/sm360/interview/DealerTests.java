/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm360.interview.controller.DealerController;
import com.sm360.interview.entities.Dealer;
import com.sm360.interview.repositories.DealerRepository;
import com.sm360.interview.services.DealerService;
import java.util.Optional;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import org.springframework.restdocs.payload.PayloadDocumentation;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

/**
 *
 * @author Adnaane
 */
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(DealerController.class)
@ContextConfiguration(classes = {DealerService.class, DealerRepository.class, DealerController.class})
public class DealerTests {

    @MockBean
    private DealerRepository dealerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private DealerController dealerController;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    void contextLoads() {
        assertThat(dealerController).isNotNull();
    }

    /**
     * This method aim to test the creation of a dealer
     *
     * @throws Exception
     */
    @Test
    public void shouldCreateDealer() throws Exception {
        Dealer dealer = new Dealer(null, "Alfonse", 12);
        Dealer createdDealer = new Dealer("625ca19e-b167-44de-b87c-3e95303735a0", "Alfonse", 12);

        when(dealerRepository.save(ArgumentMatchers.any())).thenReturn(createdDealer);

        mockMvc.perform(
                post("/api/dealer/saveOne").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("create-dealer",
                        requestFields(
                                fieldWithPath("id").description("The id of the input"),
                                fieldWithPath("name").description("The name of the dealer"),
                                fieldWithPath("tierLimit").description("The tier limit of the dealer"),
                                fieldWithPath("listings").description("The number of advertisement of the user which is null by default")
                        ), responseFields(fieldWithPath("id").description("The id of the input"),
                                fieldWithPath("name").description("The name of the dealer"),
                                fieldWithPath("listings").description("The number of advertisement of the user which is null by default"),
                                fieldWithPath("tierLimit").description("The tier limit of the dealer"))));
    }

    /**
     * This method aim to update a dealer information
     *
     * @throws Exception
     */
    @Test
    public void shouldUpdateDealer() throws Exception {
        Dealer dealer = new Dealer("625ca19e-b167-44de-b87c-3e95303735a0", "Alfonse", 13);
        Dealer oldDealer = new Dealer("625ca19e-b167-44de-b87c-3e95303735a0", "Alfonse", 12);

        when(dealerRepository.save(ArgumentMatchers.any())).thenReturn(dealer);
        when(dealerRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(oldDealer));

        mockMvc.perform(
                post("/api/dealer/updateOne").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("update dealer",
                        requestFields(
                                fieldWithPath("id").description("The id of the input"),
                                fieldWithPath("name").description("The id of the input"),
                                fieldWithPath("tierLimit").description("The title of the input"),
                                fieldWithPath("listings").description("The body of the input")
                        )));

    }

    /**
     * this test is to update a dealer's tier limit
     *
     * @throws Exception
     */
    @Test
    public void shouldUpdateTier() throws Exception {
        Dealer dealer = new Dealer("625ca19e-b167-44de-b87c-3e95303735a0", "Alfonse", 13);
        Dealer oldDealer = new Dealer("625ca19e-b167-44de-b87c-3e95303735a0", "Alfonse", 12);

        when(dealerRepository.save(ArgumentMatchers.any())).thenReturn(dealer);
        when(dealerRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(oldDealer));

        mockMvc.perform(
                put("/api/dealer/setLimit/{id}/{newlimit}", "625ca19e-b167-44de-b87c-3e95303735a0", 13).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("update-tier",
                        responseFields(
                                fieldWithPath("id").description("The id of the input"),
                                fieldWithPath("name").description("The id of the input"),
                                fieldWithPath("tierLimit").description("The title of the input"),
                                fieldWithPath("listings").description("The body of the input")
                        )));
    }
}
