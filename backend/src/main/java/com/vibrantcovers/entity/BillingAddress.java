package com.vibrantcovers.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "billing_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddress {
    @Id
    @Column(length = 255)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String street;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String postalCode;
    
    @Column(nullable = false)
    private String country;
    
    @Column(length = 100)
    private String state;
    
    @Column(length = 50)
    private String phoneNumber;
    
    @OneToMany(mappedBy = "billingAddress", cascade = CascadeType.ALL)
    private List<Order> orders;
}

