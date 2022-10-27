/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.controller;

import com.sm360.interview.entities.Dealer;
import com.sm360.interview.services.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/dealer/")
public class DealerController {

    @Autowired
    public DealerService dealerService;

    /**
     * This method aims to create a new dealer
     *
     * @param dealer dealer object
     * @return a dealer
     */
    @PostMapping(path = "/saveOne")
    public ResponseEntity saveOne(@RequestBody Dealer dealer) {
        dealer = dealerService.saveOne(dealer);
        return ResponseEntity.ok(dealer);
    }

    /**
     * This method aims to update a dealer informations
     *
     * @param dealer dealer object
     * @return a dealer
     */
    @PostMapping(path = "/updateOne")
    public ResponseEntity updateOne(@RequestBody Dealer dealer) {
        dealer = dealerService.saveOne(dealer);
        return ResponseEntity.ok(dealer);
    }

    /**
     * This method aims at updating the limit of a dealer
     *
     * @param dealer_id the dealer id
     * @param limit the new limit of the dealer
     * @return
     */
    @PutMapping(path = "/setLimit/{id}/{newlimit}")
    public ResponseEntity setLimit(@PathVariable("id") String dealer_id, @PathVariable("newlimit") int limit) {
        Dealer dealer = dealerService.setLimit(dealer_id, limit);
        return ResponseEntity.ok(dealer);
    }

}
