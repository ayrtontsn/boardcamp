package com.boardcamp.api.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.GameReturned;
import com.boardcamp.api.exceptions.IdNotFound;
import com.boardcamp.api.exceptions.NoGameReturned;
import com.boardcamp.api.exceptions.NoGameStock;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@Service
public class RentalsService {
    final RentalsRepository rentalsRepository;
    final CustomersRepository customersRepository;
    final GamesRepository gamesRepository;

    RentalsService(RentalsRepository rentalsRepository,
                    CustomersRepository customersRepository,
                    GamesRepository gamesRepository){
        this.rentalsRepository = rentalsRepository;
        this.customersRepository = customersRepository;
        this.gamesRepository = gamesRepository;
    }

    public List<RentalsModel> getAllRentals(){
        return rentalsRepository.findAll();
    }

    public RentalsModel postRental(RentalsDTO dto){
        Optional<CustomersModel> customer = customersRepository.findById(dto.getCustomerId());
        if(customer.isEmpty()){
            throw new IdNotFound("Customer with this id was not found");
        }
        Optional<GamesModel> game = gamesRepository.findById(dto.getGameId());
        if(game.isEmpty()){
            throw new IdNotFound("Game with this id was not found");
        }

        if(rentalsRepository.countByReturnDateIsNullAndGame_Id(game.get().getId())>=game.get().getStockTotal()){
            throw new NoGameStock("Game out of stock!");
        }

        RentalsModel rent = new RentalsModel(customer.get(),game.get(),dto);

        return rentalsRepository.save(rent);
    }
    
    public RentalsModel returnRental(Long id){
        Optional<RentalsModel> rental = rentalsRepository.findById(id);
        if(rental.isEmpty()){
            throw new IdNotFound("Rental with this id was not found");
        }
        else if(rental.get().getReturnDate()!=null){
            throw new GameReturned("Rental with this id already returned");
        }

        
        LocalDate today = LocalDate.now();
        Long diff = ChronoUnit.DAYS.between(today, rental.get().getRentDate());
        
        RentalsModel rent;
        if(diff-rental.get().getDaysRented()<=0){
            rent = new RentalsModel(rental.get(),0L);
        }else{
            rent = new RentalsModel(rental.get(),diff - rental.get().getDaysRented());
        }
        return rentalsRepository.saveAndFlush(rent);
    }

    public void deleteRental(Long id){
        Optional<RentalsModel> rental = rentalsRepository.findById(id);
        if(rental.isEmpty()){
            throw new IdNotFound("Rental with this id was not found");
        }
        else if(rental.get().getReturnDate()==null){
            throw new NoGameReturned("Rental with this id not yet been returned");
        }

        rentalsRepository.deleteById(id);
    }
}
