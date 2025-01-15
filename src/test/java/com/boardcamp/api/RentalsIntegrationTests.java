package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

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
import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RentalsIntegrationTests {
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
    void givenGameCustomer_whenCreatingRent_thenCreateRent(){
        //given
        GamesDTO game = new GamesDTO("Game01","",10,10.0);
        CustomersDTO customer = new CustomersDTO("Customer01","12345678910","12345678910");
        
        CustomersModel customerCreated = customersRepository.save(new CustomersModel(customer));
        GamesModel gameCreated = gamesRepository.save(new GamesModel(game));

        RentalsDTO rent = new RentalsDTO(customerCreated.getId(),gameCreated.getId(),3);


        HttpEntity<RentalsDTO> body = new HttpEntity<>(rent);
        //when
        ResponseEntity<RentalsDTO> response = restTemplate.exchange(
            "/rentals",
            HttpMethod.POST,
            body,
            RentalsDTO.class);
        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gamesRepository.count());
    }  

    @Test
    void givenGameWithOutStock_whenCreatingRent_thenThrowsError(){
        //given
        GamesDTO game = new GamesDTO("Game01","",1,10.0);
        CustomersDTO customer = new CustomersDTO("Customer01","12345678910","12345678910");
        
        CustomersModel customerCreated = customersRepository.save(new CustomersModel(customer));
        GamesModel gameCreated = gamesRepository.save(new GamesModel(game));

        RentalsDTO rent = new RentalsDTO(customerCreated.getId(),gameCreated.getId(),3);
        rentalsRepository.save(new RentalsModel(customerCreated,gameCreated,rent));

        HttpEntity<RentalsDTO> body = new HttpEntity<>(rent);
        
        //when
        ResponseEntity<String> response = restTemplate.exchange(
            "/rentals",
            HttpMethod.POST,
            body,
            String.class);

        // then

        assertEquals(1, gamesRepository.count());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void givenRent_whenReturnRent_thenFinishRent(){
        //given
        GamesDTO game = new GamesDTO("Game01","",1,10.0);
        CustomersDTO customer = new CustomersDTO("Customer01","12345678910","12345678910");
        
        CustomersModel customerCreated = customersRepository.save(new CustomersModel(customer));
        GamesModel gameCreated = gamesRepository.save(new GamesModel(game));

        RentalsDTO rent = new RentalsDTO(customerCreated.getId(),gameCreated.getId(),3);
        RentalsModel rentCreated = rentalsRepository.save(new RentalsModel(customerCreated,gameCreated,rent));
        
        //when
        ResponseEntity<RentalsModel> response = restTemplate.exchange(
            "/rentals/{id}/return",
            HttpMethod.POST,
            null,
            RentalsModel.class,
            rentCreated.getId()
            );

        // then
        assertEquals(1, gamesRepository.count());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(LocalDate.now(), response.getBody().getReturnDate());
    }

    @Test
    void givenRentRerturned_whenDeleteRent_thenDeleteRent(){
        //given
        GamesDTO game = new GamesDTO("Game01","",1,10.0);
        CustomersDTO customer = new CustomersDTO("Customer01","12345678910","12345678910");
        
        CustomersModel customerCreated = customersRepository.save(new CustomersModel(customer));
        GamesModel gameCreated = gamesRepository.save(new GamesModel(game));

        RentalsDTO rent = new RentalsDTO(customerCreated.getId(),gameCreated.getId(),3);
        RentalsModel rentCreated = rentalsRepository.save(new RentalsModel(customerCreated,gameCreated,rent));
        
        rentalsRepository.saveAndFlush(new RentalsModel(rentCreated,1L));
        
        //when
        ResponseEntity<RentalsModel> response = restTemplate.exchange(
            "/rentals/{id}",
            HttpMethod.DELETE,
            null,
            RentalsModel.class,
            rentCreated.getId()
            );

        // then
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, rentalsRepository.count());
    }
}