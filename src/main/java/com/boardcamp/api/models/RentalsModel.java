package com.boardcamp.api.models;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

import com.boardcamp.api.dtos.RentalsDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rentals")
public class RentalsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name =  "customerId")
    private CustomersModel customer;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private GamesModel game;
    
    @Column
    @Temporal(TemporalType.DATE)
    private LocalDate rentDate;

    @Column
    private Integer daysRented;

    @Column
    private LocalDate returnDate;

    @Column
    private Double originalPrice;
    
    @Column
    private Double delayFee;

    public RentalsModel(CustomersModel customersDTO, GamesModel gamesDTO, RentalsDTO rentalsDTO){
        this.rentDate = LocalDate.now();
        this.daysRented = rentalsDTO.getDaysRented();
        this.originalPrice = gamesDTO.getPricePerDay()*rentalsDTO.getDaysRented();
        this.delayFee = 0.0;
        this.customer = customersDTO;
        this.game = gamesDTO;
    }

    public RentalsModel(RentalsModel rental, Long diff){
        this.id = rental.id;
        this.customer = rental.customer;
        this.game = rental.game;
        this.rentDate = rental.rentDate;
        this.daysRented = rental.daysRented;
        this.returnDate = LocalDate.now();
        this.originalPrice = rental.originalPrice;
        this.delayFee = diff*rental.originalPrice;
    }
}
