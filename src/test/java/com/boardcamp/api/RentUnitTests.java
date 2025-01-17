package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.NoGameStock;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;
import com.boardcamp.api.services.RentalsService;

@SpringBootTest
public class RentUnitTests {
    @InjectMocks
    private RentalsService rentalsService;

    @Mock
    private RentalsRepository rentalsRepository;

    @Mock
    private GamesRepository gamesRepository;

    @Mock
    private CustomersRepository customersRepository;

    @Test
    void givenGameOutOfStock_whenCreatingRentals_thenThrowError(){
        //given
        RentalsDTO rent = new RentalsDTO(1L, 2L, 5);
        CustomersDTO customer = new CustomersDTO("Test", "12345678901", "12345678901");
        GamesDTO game = new GamesDTO("Test", "Test", 5, 5.0);

        CustomersModel newCustomer = new CustomersModel(customer);
        GamesModel newGame = new GamesModel(game);

        doReturn(Optional.of(newCustomer)).when(customersRepository).findById(any());
        doReturn(Optional.of(newGame)).when(gamesRepository).findById(any());

        doReturn(5L).when(rentalsRepository).countByReturnDateIsNullAndGame_Id(any());

        //when
        NoGameStock exceptions = assertThrows(
            NoGameStock.class, () -> rentalsService.postRental(rent));

        //then
        verify(gamesRepository,times(1)).findById(any());
        verify(customersRepository,times(1)).findById(any());
        verify(rentalsRepository,times(1)).countByReturnDateIsNullAndGame_Id(any());

        assertNotNull(exceptions);
        assertEquals("Game out of stock!", exceptions.getMessage());
    }

    @Test
    void givenDaysRentalsEqualsDaysRented_whenFinishRent_thenDelayFeeIsZero(){
        //given
        RentalsDTO rent = new RentalsDTO(1L, 2L, 5);
        CustomersDTO customer = new CustomersDTO("Test", "12345678901", "12345678901");
        GamesDTO game = new GamesDTO("Test", "Test", 5, 5.0);

        CustomersModel newCustomer = new CustomersModel(customer);
        GamesModel newGame = new GamesModel(game);

        RentalsModel newRent = new RentalsModel(newCustomer,newGame,rent);
        newRent.setId(3L);
        newRent.setRentDate(LocalDate.now().minusDays(5));

        RentalsModel newRentFinish = new RentalsModel(newRent,0L);

        doReturn(Optional.of(newRent)).when(rentalsRepository).findById(3L);
        doReturn(newRentFinish).when(rentalsRepository).saveAndFlush(any());

        //when
        RentalsModel result = rentalsService.returnRental(3L);

        //then
        verify(rentalsRepository,times(1)).saveAndFlush(any());
        assertNotNull(result.getReturnDate());
        assertEquals(0.0, result.getDelayFee());
        }

    @Test
    void givenValidId_whenDeleteRent_thenNoReturn(){
        //given
        RentalsDTO rent = new RentalsDTO(1L, 2L, 5);
        CustomersDTO customer = new CustomersDTO("Test", "12345678901", "12345678901");
        GamesDTO game = new GamesDTO("Test", "Test", 5, 5.0);

        CustomersModel newCustomer = new CustomersModel(customer);
        GamesModel newGame = new GamesModel(game);

        RentalsModel newRent = new RentalsModel(newCustomer,newGame,rent);
        newRent.setId(3L);
        newRent.setRentDate(LocalDate.now().minusDays(5));
        newRent.setReturnDate(LocalDate.now());
        newRent.setDelayFee(0.0);

        doReturn(Optional.of(newRent)).when(rentalsRepository).findById(any());

        //when
        rentalsService.deleteRental(3L);

        //then
        verify(rentalsRepository,times(1)).deleteById(any());
    }
}
