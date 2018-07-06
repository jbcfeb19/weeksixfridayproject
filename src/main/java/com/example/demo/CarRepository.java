package com.example.demo;

import com.example.demo.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface CarRepository extends CrudRepository<Car,Long> {
}
