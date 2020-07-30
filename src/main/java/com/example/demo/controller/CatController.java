package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.example.demo.exception.CatNotFoundException;
import com.example.demo.model.Cat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cat")
public class CatController {
    List<Cat> catList = new ArrayList<>();

    public CatController() {
        catList.add(new Cat(1, "First Cat"));
        catList.add(new Cat(2, "Second Cat"));
        catList.add(new Cat(3, "Third Cat"));
    }

    @GetMapping
    public List<Cat> getCat() {
        return catList;
    }

    @PostMapping
    public Cat postCat(@Valid @RequestBody Cat cat) {
        catList.add(cat);
        return cat;
    }

    @GetMapping("/{ids}")
    public Cat getCatById(@PathVariable(name = "ids") int id) throws CatNotFoundException {
        int catListSize = catList.size();
        if (catListSize > id) {
            return catList.get(id);
        } else {
            throw new CatNotFoundException("Cannot return a cat with index " + id + " because cat list size = " + catListSize);
        }
    }
}
