package com.theironyard;

import javax.persistence.*;

/**
 * Created by DrScott on 11/10/15.
 */
@Entity
public class Beer {
    @Id
    @GeneratedValue
    Integer id;

    String name;
    String type;
    Integer calories;
    @ManyToOne
    User user;


}
