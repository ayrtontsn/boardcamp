package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.GameNameConflict;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;

@Service
public class CustomersService {

    final CustomersRepository customersRepository;

    CustomersService(CustomersRepository customersRepository){
        this.customersRepository = customersRepository;
    }
    public List<CustomersModel> getAllCustomers(){
        return customersRepository.findAll();
    }

    public CustomersModel postCustomer(CustomersDTO dto){

        if(customersRepository.existsCustomersByCpf(dto.getCpf())){
            throw new GameNameConflict("Customer already registered with this CPF");
        }

        CustomersModel customer = new CustomersModel(dto);
        return customersRepository.save(customer);
    }
}
