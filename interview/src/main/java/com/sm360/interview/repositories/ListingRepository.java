/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.repositories;

import com.sm360.interview.entities.Listing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adnaane
 */
@Repository
public interface ListingRepository extends JpaRepository<Listing, String> {

    @Query("SELECT count(1) FROM Listing l WHERE l.state = :state AND l.dealerId.id = :dealer")
    public int countStateListingByDealer(@Param("state") String state, @Param("dealer") String dealer_id);

    @Query("SELECT l FROM Listing l WHERE l.dealerId.id = :dealer AND l.state = :state")
    public List<Listing> findAllByDealerIdAndState(@Param("dealer") String dealer_id, @Param("state") String state);

    @Query(value = "SELECT * FROM Listing l WHERE l.dealer_id = ?1 AND l.state = ?2 ORDER BY l.created_at ASC limit 1", nativeQuery = true)
    public Listing findOldesListingByUser(@Param("dealer") String dealer_id, @Param("state") String state);
}
