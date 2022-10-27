/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.services;

import com.sm360.interview.entities.Dealer;
import com.sm360.interview.entities.Listing;
import com.sm360.interview.repositories.DealerRepository;
import com.sm360.interview.repositories.ListingRepository;
import com.sm360.interview.services.exceptions.DealerNumberLimitException;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adnaane
 */
@Service
@Slf4j
public class ListingService {

    private static final String STATE_DRAFT = "draft";
    private static final String STATE_PUBLISHED = "published";

    @Autowired
    DealerRepository dealerRepository;
    @Autowired
    ListingRepository listingRepository;

    /**
     * This function aims at unpublishing a listing
     *
     * @param listing_id listing ID
     * @return
     */
    public Listing unpublishOne(String listing_id) {
        Listing listing = listingRepository.findById(listing_id).get();
        listing = unpublishOne(listing);
        log.info("\"" + listing_id + "\" unpublished successfully !");
        return listing;
    }

    /**
     * This function aims at unplishing a listing given a listing object
     *
     * @param listing listing object
     * @return
     */
    public Listing unpublishOne(Listing listing) {
        // Send a message if there is no listing for that id
        // put a condition before setting the listing before draft
        listing.setState(STATE_DRAFT);
        listing = listingRepository.save(listing);
        log.info("\"" + listing.getId() + "\" unpublished successfully !");
        return listing;
    }

    /**
     * This fucntion aims at publishing a listing accroding to the given
     * specification This function is transactional because we're doing
     * different operations
     *
     * @param listing_id the listing ID
     * @param sendError a boolean that specify if an error should be thrown or
     * the oldes published listing whould be unpublished
     * @return
     * @throws java.lang.Exception
     */
    @Transactional
    public Listing publishOne(String listing_id, boolean sendError) {
        // Controller la présence du listing en question.
        Listing listing = listingRepository.findById(listing_id).get();
        Dealer dealer = dealerRepository.findById(listing.getDealerId().getId()).get();

        // return the same listing if it is already published
        if (listing.getState().equalsIgnoreCase(STATE_PUBLISHED)) {
            return listing;
        }

        int count = listingRepository.countStateListingByDealer(STATE_PUBLISHED, dealer.getId());

        // if the user reach his limit of publishing listing then update the oldest listing to draft state
        if (count >= dealer.getTierLimit() & count > 0) {
            log.info("User has reached his limit");
            if (sendError) {
                throw new DealerNumberLimitException();
            }
            Listing oldestListing = listingRepository.findOldesListingByUser(dealer.getId(), STATE_PUBLISHED);
            unpublishOne(oldestListing);
        }

        listing.setState(STATE_PUBLISHED);
        listing = listingRepository.save(listing);
        log.info("\"" + listing_id + "\" published successfully !");
        return listing;
    }

    /**
     * This method aims at creating a new listing. A new listing is created with
     * the draft by default
     *
     * @param listing a listing object
     * @return
     */
    public Listing saveOne(Listing listing) {
        Dealer dealer = dealerRepository.findById(listing.getDealerId().getId()).get();
        listing.setId(null);
        listing.setDealerId(dealer);
        listing.setState(STATE_DRAFT);
        listing.setCreatedAt(new Date());
        listing = listingRepository.save(listing);
        log.info("New listing created  !");
        return listing;
    }

    /**
     * This method aims at updating a listing a listing
     *
     * @param new_listing a listing object
     * @return
     */
    public Listing updateOne(Listing new_listing) {
        Listing listing = listingRepository.findById(new_listing.getId()).get();
        if (new_listing.getPrice() != null || new_listing.getPrice() != 0) {
            listing.setPrice(new_listing.getPrice());
        }
        if (new_listing.getVehicle() != null || !new_listing.getVehicle().isEmpty()) {
            listing.setVehicle(new_listing.getVehicle());
        }
        // If the state change and is valid then update it as well
        // I've commented this part of the code because I personnally think that the user cannot modifiy the state of this adversitsement , if he really want to he can just publish or unpublish it through that funcitonallity. However a functionality can be created to do the two operation if needed
//        if (new_listing.getState().trim().equalsIgnoreCase(STATE_DRAFT) || new_listing.getState().trim().equalsIgnoreCase(STATE_PUBLISHED)) {
//            listing.setState(new_listing.getState());
//        }

        listing = listingRepository.save(listing);
        log.info("Listing \"" + listing.getId() + "\" updated");
        return listing;
    }

    /**
     * this function aims at finding a dealer advertisement by a given state
     *
     * @param dealer_id the dealer id
     * @param state the state
     * @return
     */
    public List<Listing> findAllByDealerIdAndState(String dealer_id, String state) {
        state = getState(state);
        return listingRepository.findAllByDealerIdAndState(dealer_id, state);
    }

    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    /**
     * A fucntion to handle the different state
     *
     * @param state
     * @return
     */
    public String getState(String state) {
        if (state.trim().equalsIgnoreCase(STATE_DRAFT)) {
            return STATE_DRAFT;
        }
        return STATE_PUBLISHED;
    }

}
