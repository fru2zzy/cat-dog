package com.example.demo.controller;

import com.example.demo.model.Dog;
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
public class DogControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    DogController dogController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getDog() {
        ResponseEntity<Dog[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/dog", Dog[].class);
        assertNotNull(responseEntity);
        Dog[] dogs = responseEntity.getBody();
        assertNotNull(dogs);
        assertTrue(dogs.length == 3);
    }

    @Test
    void getDogById() {
        ResponseEntity<Dog> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/dog/0", Dog.class);
        assertNotNull(responseEntity);
        Dog dog = responseEntity.getBody();
        assertEquals("First Dog", dog.getName());
    }

    @Test
    void getDogById404() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/dog/100", String.class);
        assertNotNull(responseEntity);
        String body = responseEntity.getBody();
        assertEquals("Cannot return a dog with index 100 because dog list size = 3", body);
    }

    @Test
    void postDog() throws JsonProcessingException {
        Dog dog = new Dog(3, "doggy");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = mapper.writeValueAsString(dog);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Dog> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/dog", httpEntity, Dog.class);
        assertEquals(dog, responseEntity.getBody());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

}
