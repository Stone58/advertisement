/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.services;

import com.sm360.interview.entities.Dealer;
import com.sm360.interview.repositories.DealerRepository;
import com.sm360.interview.services.exceptions.DealerNotDefinedException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adnaane
 */
@Service
@Slf4j
public class DealerService extends RuntimeException {

    @Autowired
    private DealerRepository dealerRepository;

    /**
     * this function aims at saving a dealer object
     *
     * @param dealer a dealer object
     * @return
     */
    public Dealer saveOne(Dealer dealer) {
        // We can either add another field (security number for exemple ) to distinguish dealer with the same name
        dealer.setId(null);
        dealer.setListings(null);
        dealer = dealerRepository.save(dealer);
        log.info("creating a new dealer");
        return dealer;
    }

    /**
     * This funtion aims at update a dealer information
     *
     * @param dealer a dealer object
     * @return
     */
    public Dealer updateOne(Dealer dealer) {
        Optional<Dealer> oldDealer = dealerRepository.findById(dealer.getId());
        try {
            Dealer newDealer = oldDealer.get();
            newDealer.setName(dealer.getName());
            newDealer.setTierLimit(dealer.getTierLimit());
            return dealerRepository.save(newDealer);
        } catch (Exception e) {
            log.error("Failded to increase limit of dealer because he/she doesn't exist !");
            throw new DealerNotDefinedException();
        }
    }

    /**
     * This function aims at update the limit of a dealer
     *
     * @param dealerId the dealer's ID
     * @param limit the new value of the limit
     * @return
     */
    public Dealer setLimit(String dealerId, int limit) {
        Optional<Dealer> oldDealer = dealerRepository.findById(dealerId);
        try {
            Dealer newDealer = oldDealer.get();
            newDealer.setTierLimit(limit);
            return dealerRepository.save(newDealer);
        } catch (Exception e) {
            log.error("Failded to increase limit of dealer because he/she doesn't exist !");
            throw new DealerNotDefinedException();
        }
    }
}
