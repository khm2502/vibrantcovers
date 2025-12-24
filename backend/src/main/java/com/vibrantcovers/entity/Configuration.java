package com.vibrantcovers.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    @Id
    @Column(length = 255)
    private String id;
    
    @Column(nullable = false)
    private Integer width;
    
    @Column(nullable = false)
    private Integer height;
    
    @Column(nullable = false, length = 1000)
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CaseColor color;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PhoneModel model;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CaseMaterial material;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CaseFinish finish;
    
    @Column(length = 1000)
    private String croppedImageUrl;
    
    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL)
    private List<Order> orders;
}

