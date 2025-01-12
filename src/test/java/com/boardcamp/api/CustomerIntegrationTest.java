package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RentalsRepository rentalsRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private GamesRepository gamesRepository;

    @BeforeEach
    public void cleanUpDataBase(){
        rentalsRepository.deleteAll();
        customersRepository.deleteAll();
        gamesRepository.deleteAll();  
    }

    //HappyPath
    @Test
    void givenCustomerRepositoryEmpty_whenCreatingCustomer_thenCreateCustomer(){
        //given
        CustomersDTO customer = new CustomersDTO("Test01","12345678910","12345678910");


        HttpEntity<CustomersDTO> body = new HttpEntity<>(customer);
        //when
        ResponseEntity<String> response = restTemplate.exchange(
            "/customers",
            HttpMethod.POST,
            body,
            String.class);
        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, customersRepository.count());
    }

    @Test
    void givenRepeatedCustomer_whenCreatingCustomer_thenThrowsError(){
        //given
        CustomersDTO customer = new CustomersDTO("Test02","12345678910","12345678910");
        CustomersModel  customerRepeated = new CustomersModel(customer);
        
        customersRepository.save(customerRepeated);

        HttpEntity<CustomersDTO> body = new HttpEntity<>(customer);
        
        //when
        ResponseEntity<String> response = restTemplate.exchange(
            "/customers",
            HttpMethod.POST,
            body,
            String.class);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(1, customersRepository.count());
    }

    @Test
    void givenCreatedCustomer_whenGetCustomerById_thenReturnCustomer(){
        //given
        CustomersDTO customer = new CustomersDTO("Test03","12345678910","12345678910");
        CustomersModel  customerCreater = new CustomersModel(customer);
        
        CustomersModel customerCreated = customersRepository.save(customerCreater);
        
        //when
        ResponseEntity<CustomersDTO> response = restTemplate.exchange(
            "/customers/{id}",
            HttpMethod.GET,
            null,
            CustomersDTO.class,
            customerCreated.getId());

        // then
        CustomersDTO returnedCustomer = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerCreated.getName(), returnedCustomer.getName());
    }

    @Test
    void givenCreatedAndDeletedCustomer_whenGetCustomerById_thenThrowErrors(){
        //given
        CustomersDTO customer = new CustomersDTO("Test03","12345678910","12345678910");
        CustomersModel  customerCreater = new CustomersModel(customer);
        
        CustomersModel customerCreated = customersRepository.save(customerCreater);
        customersRepository.deleteById(customerCreated.getId());
        
        //when
        ResponseEntity<CustomersDTO> response = restTemplate.exchange(
            "/customers/{id}",
            HttpMethod.GET,
            null,
            CustomersDTO.class,
            customerCreated.getId());

        // then
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
