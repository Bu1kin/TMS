package com.example.TMS.Test;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameTest;
    private String status;
    private Double version;
    private Date dateStart;
    private Date dateEnd;
    private String results;
    private String description;
}
