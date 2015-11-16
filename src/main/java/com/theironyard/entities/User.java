package com.theironyard.entities;


import com.theironyard.entities.Beer;

import javax.persistence.*;
import java.util.List;

/**
 * Created by DrScott on 11/11/15.
 */
@Entity
@Table(name = "users") //modifies table name
public class User {
    @Id
    @GeneratedValue
    Integer id;

    public String name;
    public String password;

    @OneToMany(mappedBy = "user") //this is the name of the field in the beer class
    public List<Beer> beers;


}
