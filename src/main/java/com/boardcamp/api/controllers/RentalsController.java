package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.services.RentalsService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/rentals")
public class RentalsController {
    final RentalsService rentalsService;
    RentalsController(RentalsService rentalsService){
        this.rentalsService = rentalsService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllRentals() {
        return ResponseEntity.status(HttpStatus.OK).body(rentalsService.getAllRentals());
    }

    @PostMapping()
    public ResponseEntity<Object> postRental(@RequestBody @Valid RentalsDTO body) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalsService.postRental(body));
    }
    
    
}
