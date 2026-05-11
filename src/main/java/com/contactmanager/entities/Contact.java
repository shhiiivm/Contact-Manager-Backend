package com.contactmanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact
{
    @Id
    private  String id;
    private  String name;
    private  String email;
    private  String phoneNumber;
    private  String address;
    private  String picture;

    @Column(columnDefinition = "TEXT")
    private  String description;
    private  boolean favourite = false;


    private  String websiteLink;
    private String  linkedInLink;

    private String cloudinaryImagePublicId;


    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SocialLink> links = new ArrayList<>();

}
