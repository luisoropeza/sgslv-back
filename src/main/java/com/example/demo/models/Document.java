package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "document")
@Data
public class Document {
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @OneToOne(mappedBy = "document")
    private Request request;
}
