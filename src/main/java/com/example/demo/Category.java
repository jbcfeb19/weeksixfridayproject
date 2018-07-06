package com.example.demo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String categoryName;

    @OneToMany(mappedBy = "category",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER)
    public Set<Car> cars;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}
