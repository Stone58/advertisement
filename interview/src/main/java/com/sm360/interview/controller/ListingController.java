/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.controller;

import com.sm360.interview.entities.Listing;
import com.sm360.interview.repositories.ListingRepository;
import com.sm360.interview.services.ListingService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Adnaane
 */
@RestController
@RequestMapping("api/listing/")
public class ListingController {

    @Autowired
    public ListingRepository listingRepository;
    @Autowired
    public ListingService listingService;

    @GetMapping(path = "/list_all")
    public ResponseEntity listAll() {
        List<Listing> result = listingService.findAll();
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/list_all_by_id/{id}/{state}")
    public ResponseEntity listAllByIdandState(@PathVariable("id") String dealer_id, @PathVariable("state") String state) {
        List<Listing> result = listingService.findAllByDealerIdAndState(dealer_id, state);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @PutMapping(path = "/publishOne/{id}/{throwerrorwhenlimitreached}")
    public ResponseEntity publishOne(@PathVariable("id") String listing_id, @PathVariable("throwerrorwhenlimitreached") boolean throwError) {
        //Check the http status when there is exception
        Listing listing;
        try {
            listing = listingService.publishOne(listing_id, throwError);
            return ResponseEntity.ok(listing);
        } catch (Exception ex) {
            Logger.getLogger(ListingController.class.getName()).log(Level.SEVERE, null, ex);
            Map<String, Object> excep_message = new HashMap<>();
            excep_message.put("message", ex.getMessage());
            excep_message.put("detail", ex.getStackTrace());
            return ResponseEntity.ok(excep_message);
        }
    }

    @PutMapping(path = "/unpublishOne/{id}")
    public ResponseEntity unpublishOne(@PathVariable("id") String listing_id) {
        Listing listing = listingService.unpublishOne(listing_id);
        return ResponseEntity.ok(listing);
    }

    @PostMapping(path = "/saveOne")
    public ResponseEntity saveOne(@RequestBody Listing listing) {
        listing = listingService.saveOne(listing);
        return ResponseEntity.ok(listing);
    }

    @PutMapping(path = "/updateOne")
    public ResponseEntity updateOne(@RequestBody Listing listing) {
        listing = listingService.updateOne(listing);
        return ResponseEntity.ok(listing);
    }

//    @PostMapping(path = "/unpublish/{id}")
//    public ResponseEntity unpublishOne(@PathVariable("id") String listing_id) {
//        Listing listing = listingService.unpublishOne(listing_id);
//        return ResponseEntity.ok(listing);
//    }
}
