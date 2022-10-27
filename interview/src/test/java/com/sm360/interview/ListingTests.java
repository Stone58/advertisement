package com.sm360.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm360.interview.controller.ListingController;
import com.sm360.interview.entities.Dealer;
import com.sm360.interview.entities.Listing;
import com.sm360.interview.repositories.DealerRepository;
import com.sm360.interview.repositories.ListingRepository;
import com.sm360.interview.services.ListingService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ListingController.class)
@ContextConfiguration(classes = {ListingService.class, DealerRepository.class, ListingController.class})
class ListingTests {

    @MockBean
    private ListingRepository listingRepository;
    @MockBean
    private DealerRepository dealerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private ListingController listingController;

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
        assertThat(listingController).isNotNull();
    }

    /**
     * This method aims at testing the list of listing of a dealer by a given
     * state
     *
     * @throws Exception
     */
    @Test
    public void shouldReturnListOfListingsByDealerID() throws Exception {

        List<Listing> listings = new ArrayList<>(
                Arrays.asList(
                        new Listing("3415cac3-dadf-4d87-a161-0363847ba597", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW", 50000F, new Date(2022, 02, 03), Listing.STATE_DRAFT),
                        new Listing("98171fe6-2f38-4d57-80e0-103441596f77", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW", 50000F, new Date(2022, 02, 03), Listing.STATE_DRAFT),
                        new Listing("33a03beff-9b6c-4516-91ec-93a797e5cffe", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW", 50000F, new Date(2022, 02, 03), Listing.STATE_DRAFT)
                ));

        String dealer_id = "d7532f09-b690-42bd-802c-3597e3707c25";
        when(listingRepository.findAllByDealerIdAndState(dealer_id, Listing.STATE_DRAFT)).thenReturn(listings);
        mockMvc.perform(get("/api/listing/list_all_by_id/{id}/{state}", dealer_id, Listing.STATE_DRAFT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listings.size()))
                .andDo(document("list-listing-by-dealer-id",
                        responseFields(fieldWithPath("[].id").description("The id of the input"),
                                fieldWithPath("[].dealerId").description("The name of the dealer"),
                                fieldWithPath("[].dealerId.id").ignored(),
                                fieldWithPath("[].dealerId.name").ignored(),
                                fieldWithPath("[].dealerId.tierLimit").ignored(),
                                fieldWithPath("[].dealerId.listings").ignored(),
                                fieldWithPath("[].state").description("The state of the state"),
                                fieldWithPath("[].vehicle").description("The tier limit of the dealer"),
                                fieldWithPath("[].createdAt").description("The date of creation"),
                                fieldWithPath("[].price").description("The number of advertisement of the user which is null by default")
                        )
                ));
    }

    /**
     * This test is about creating a new listing/advertisement
     *
     * @throws Exception
     */
    @Test
    public void shouldCreateListing() throws Exception {
        String dealer_id = "d7532f09-b690-42bd-802c-3597e3707c25";
        Dealer d = new Dealer(dealer_id);

        when(dealerRepository.findById(dealer_id)).thenReturn(Optional.of(d));
        // I use the same id just because i
        Listing newListing = new Listing("3415cac3-dadf-4d87-a161-0363847ba597",
                new Dealer(dealer_id), "BMW", 50000F,
                new Date(2022, 02, 03), Listing.STATE_DRAFT);

        Listing listing = new Listing(null,
                new Dealer(dealer_id), "BMW", 50000F,
                new Date(2022, 02, 03), Listing.STATE_DRAFT);

        when(listingRepository.save(ArgumentMatchers.any())).thenReturn(newListing);

        List<FieldDescriptor> listingdescriptor = Arrays.asList(fieldWithPath("id").description("The id of the input"),
                fieldWithPath("vehicle").description("The tier limit of the dealer"),
                fieldWithPath("createdAt").description("The date of creation"),
                fieldWithPath("price").description("The number of advertisement of the user which is null by default"),
                fieldWithPath("state").description("The state of the creation"),
                fieldWithPath("dealerId").ignored(),
                fieldWithPath("dealerId.id").ignored(),
                fieldWithPath("dealerId.name").ignored(),
                fieldWithPath("dealerId.tierLimit").ignored(),
                fieldWithPath("dealerId.listings").ignored());

        mockMvc.perform(
                post("/api/listing/saveOne").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("create-listing",
                        requestFields(listingdescriptor
                        ), responseFields(listingdescriptor)
                ));
    }

    /**
     * This test is about updating a listing
     *
     * @throws Exception
     */
    @Test
    public void updateListing() throws Exception {
        String dealer_id = "d7532f09-b690-42bd-802c-3597e3707c25";
        Listing oldlisting = new Listing("98171fe6-2f38-4d57-80e0-103441596f77", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW", 50000F, new Date(2022, 02, 03), Listing.STATE_DRAFT);
        Listing newListing = new Listing("98171fe6-2f38-4d57-80e0-103441596f77", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW2", 30000F, new Date(2022, 02, 03), Listing.STATE_DRAFT);

        when(listingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(oldlisting));
        when(listingRepository.save(ArgumentMatchers.any())).thenReturn(newListing);

        List<FieldDescriptor> listingdescriptor = Arrays.asList(fieldWithPath("id").description("The id of the input"),
                fieldWithPath("vehicle").description("The tier limit of the dealer"),
                fieldWithPath("createdAt").description("The date of creation"),
                fieldWithPath("price").description("The number of advertisement of the user which is null by default"),
                fieldWithPath("state").description("The state of the creation"),
                fieldWithPath("dealerId").ignored(),
                fieldWithPath("dealerId.id").ignored(),
                fieldWithPath("dealerId.name").ignored(),
                fieldWithPath("dealerId.tierLimit").ignored(),
                fieldWithPath("dealerId.listings").ignored());

        mockMvc.perform(
                put("/api/listing/updateOne").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newListing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("update-listing",
                        requestFields(listingdescriptor
                        ), responseFields(listingdescriptor)
                ));
    }

    /**
     * This test is about unpublish a listing
     *
     * @throws Exception
     */
    @Test
    public void shouldUnpublishListing() throws Exception {
        String dealer_id = "d7532f09-b690-42bd-802c-3597e3707c25";
        Listing oldlisting = new Listing("98171fe6-2f38-4d57-80e0-103441596f77", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW", 50000F, new Date(2022, 02, 03), Listing.STATE_DRAFT);
        Listing newListing = new Listing("98171fe6-2f38-4d57-80e0-103441596f77", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW2", 30000F, new Date(2022, 02, 03), Listing.STATE_DRAFT);

        when(listingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(oldlisting));
        when(listingRepository.save(ArgumentMatchers.any())).thenReturn(newListing);

        List<FieldDescriptor> listingdescriptor = Arrays.asList(fieldWithPath("id").description("The id of the input"),
                fieldWithPath("vehicle").description("The tier limit of the dealer"),
                fieldWithPath("createdAt").description("The date of creation"),
                fieldWithPath("price").description("The number of advertisement of the user which is null by default"),
                fieldWithPath("state").description("The state of the creation"),
                fieldWithPath("dealerId").ignored(),
                fieldWithPath("dealerId.id").ignored(),
                fieldWithPath("dealerId.name").ignored(),
                fieldWithPath("dealerId.tierLimit").ignored(),
                fieldWithPath("dealerId.listings").ignored());

        mockMvc.perform(
                put("/api/listing/unpublishOne/{id}", dealer_id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("unpublish-listing", responseFields(listingdescriptor)
                ));
    }

    /**
     * This test is about publishing a listing
     *
     * @throws Exception
     */
    @Test
    public void shouldPublishListing() throws Exception {
        String dealer_id = "d7532f09-b690-42bd-802c-3597e3707c25";
        Listing oldlisting = new Listing("98171fe6-2f38-4d57-80e0-103441596f87", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW", 50000F, new Date(2022, 02, 03), Listing.STATE_DRAFT);
        Listing newListing = new Listing("98171fe6-2f38-4d57-80e0-103441596f87", new Dealer("d7532f09-b690-42bd-802c-3597e3707c25"), "BMW2", 30000F, new Date(2022, 02, 03), Listing.STATE_PUBLISHED);

        when(listingRepository.countStateListingByDealer(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(1);
        when(listingRepository.findOldesListingByUser(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(oldlisting);
        when(listingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(oldlisting));
        when(dealerRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(new Dealer("d7532f09-b690-42bd-802c-3597e3707c25", "Adnaane", 1)));

        when(listingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(oldlisting));
        when(listingRepository.save(ArgumentMatchers.any())).thenReturn(newListing);

        List<FieldDescriptor> listingdescriptor = Arrays.asList(fieldWithPath("id").description("The id of the input"),
                fieldWithPath("vehicle").description("The tier limit of the dealer"),
                fieldWithPath("createdAt").description("The date of creation"),
                fieldWithPath("price").description("The number of advertisement of the user which is null by default"),
                fieldWithPath("state").description("The state of the creation"),
                fieldWithPath("dealerId").ignored(),
                fieldWithPath("dealerId.id").ignored(),
                fieldWithPath("dealerId.name").ignored(),
                fieldWithPath("dealerId.tierLimit").ignored(),
                fieldWithPath("dealerId.listings").ignored());

        mockMvc.perform(
                put("/api/listing/publishOne/{id}/{throwerrorwhenlimitreached}", dealer_id, false).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(document("publish-listing", responseFields(listingdescriptor)
                ));
    }

}
