package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Cat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CatControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    CatController catController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getCat() {
        ResponseEntity<Cat[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/cat", Cat[].class);
        assertNotNull(responseEntity);
        Cat[] cats = responseEntity.getBody();
        assertNotNull(cats);
        assertTrue(cats.length == 3);
    }

    @Test
    void getCatById() {
        ResponseEntity<Cat> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/cat/2", Cat.class);
        assertNotNull(responseEntity);
        Cat cat = responseEntity.getBody();
        assertEquals("Third Cat", cat.getName());
    }

    @Test
    void getCatById404() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/cat/100", String.class);
        assertNotNull(responseEntity);
        String body = responseEntity.getBody();
        assertEquals("Cannot return a cat with index 100 because cat list size = 4", body);
    }

    @Test
    void postCatNotValid() throws JsonProcessingException {
        Cat cat = new Cat(123456, "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errors = new HashMap<>();
        errors.put("timeStamp", new Date().toString());
        errors.put("status", "method 'postCat' parameter 0");
        List<String> errorsList = new ArrayList<>();
        errorsList.add("Name can't be blank");
        errorsList.add("Age should not be greater than 5");
        errors.put("errorsList", errorsList);
        String s = "{timeStamp=" + new Date().toString() + ", errorsList=[Age should not be greater than 5, Name can't be blank], status=method 'postCat' " +
                "parameter 0}";
        String body = mapper.writeValueAsString(errors);
        String catString = mapper.writeValueAsString(cat);
        HttpEntity<String> httpEntity = new HttpEntity<>(catString, headers);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/cat", httpEntity, Object.class);
        //assertEquals(s, responseEntity.getBody());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void postCat() throws JsonProcessingException {
        Cat cat = new Cat(3, "Barsik");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = mapper.writeValueAsString(cat);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Cat> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/cat", httpEntity, Cat.class);
        assertEquals(cat, responseEntity.getBody());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

}
