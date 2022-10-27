/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sm360.interview.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Adnaane
 */
@Table
@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Listing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id", nullable = false)
    private Dealer dealerId;
    @Column(name = "vehicle", nullable = false)
    private String vehicle;
    @Column(name = "price", nullable = false)
    private Float price;
    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();
    @Column(name = "state", nullable = false)
    private String state;

    @Transient
    public static final String STATE_DRAFT = "draft";
    @Transient
    public static final String STATE_PUBLISHED = "published";

        public String getState(String state) {
        if (state.trim().equalsIgnoreCase(STATE_DRAFT)) {
            return STATE_DRAFT;
        }
        return STATE_PUBLISHED;
    }

    public Listing(String id) {
        this.id = id;
    }

    public Listing(Dealer dealerId, String vehicle, Float price, String state) {
        this.dealerId = dealerId;
        this.vehicle = vehicle;
        this.price = price;
        this.state = state;
    }

        
        
}
