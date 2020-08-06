package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.exception.DogNotFoundException;
import com.example.demo.model.Cat;
import com.example.demo.model.Dog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dog")
public class DogController {
    List<Dog> dogList = new ArrayList<>();

    public DogController() {
        dogList.add(new Dog(1, "First Dog"));
        dogList.add(new Dog(2, "Second Dog"));
        dogList.add(new Dog(3, "Third Dog"));
    }
    @GetMapping
    public List<Dog> getDog() {
        return dogList;
    }

    @PostMapping
    public Dog postDog(@RequestBody Dog dog) {
        dogList.add(dog);
        return dog;
    }

    @GetMapping("/{id}")
    public Dog getDogById(@PathVariable int id) throws DogNotFoundException {
        int dogListSize = dogList.size();
        if (dogList.size() > id) {
            return dogList.get(id);
        } else {
            throw new DogNotFoundException("Cannot return a dog with index " + id + " because dog list size = " + dogListSize);
        }
    }
}
