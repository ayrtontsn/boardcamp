package com.boardcamp.api.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.services.CustomersService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/customers")
public class CustomersController {
    final CustomersService customersService;
    CustomersController(CustomersService customersService){
        this.customersService = customersService;
    }
    
    @GetMapping
    public ResponseEntity<Object> getCustomers(){
        return ResponseEntity.status(HttpStatus.OK).body(customersService.getAllCustomers());
    }

    @PostMapping
    public ResponseEntity<Object> postCustomer(@RequestBody @Valid CustomersDTO body) {       
        return ResponseEntity.status(HttpStatus.CREATED).body(customersService.postCustomer(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("id") Long id) {
        Optional<CustomersModel> customer = customersService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer.get());
    }
    
    
}
