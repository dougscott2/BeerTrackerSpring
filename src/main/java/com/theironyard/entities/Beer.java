package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by DrScott on 11/10/15.
 */
@Entity
public class Beer {
    @Id
    @GeneratedValue
    Integer id;

    public String name;
    public String type;
    public Integer calories;
    @ManyToOne
    public User user;


}
