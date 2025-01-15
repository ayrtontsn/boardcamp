package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.GameNameConflict;
import com.boardcamp.api.exceptions.IdNotFound;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.services.CustomersService;

@SpringBootTest
public class CustomerUnitTests {
    @InjectMocks
    private CustomersService customersService;

    @Mock
    private CustomersRepository customersRepository;

    @Test
    void givenRepeatedCPFRegister_whenCreatingCustomer_thenThrowsError(){
        //given
        CustomersDTO customer = new CustomersDTO("Test", "12345678901", "12345678901");
        doReturn(true).when(customersRepository).existsCustomersByCpf(any());

        //when
        GameNameConflict exception = assertThrows(
            GameNameConflict.class, () -> customersService.postCustomer(customer));
        
        //then
        verify(customersRepository,times(1)).existsCustomersByCpf(any());
        verify(customersRepository,times(0)).save(any());
        assertNotNull(exception);
        assertEquals("Customer already registered with this CPF", exception.getMessage());
    }

    @Test
    void givenWrongCustomerId_whenGetFilterCustomer_thenTrowsError(){
        //given
        Long id=1L;
        doReturn(false).when(customersRepository).existsById(any());

        //when
        IdNotFound exception = assertThrows(
            IdNotFound.class, () -> customersService.getCustomerById(id));

        //then
        verify(customersRepository,times(1)).existsById(any());
        verify(customersRepository,times(0)).findById(id);
        assertNotNull(exception);
        assertEquals("Customer with this id was not found", exception.getMessage());
    }

    @Test
    void  givenValidCustomer_whenCreatingCustomer_thenCreatesRecipe(){
        //given
        CustomersDTO customer = new CustomersDTO("Test", "12345678901", "12345678901");
        CustomersModel newCustomer = new CustomersModel(customer);

        doReturn(false).when(customersRepository).existsCustomersByCpf(any());
        doReturn(newCustomer).when(customersRepository).save(any());

        //when
        CustomersModel result = customersService.postCustomer(customer);

        //then
        verify(customersRepository,times(1)).existsCustomersByCpf(any());
        verify(customersRepository,times(1)).save(any());
        assertEquals(newCustomer, result);
    }
}
