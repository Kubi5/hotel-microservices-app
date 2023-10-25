package com.kubis.microservices.customers.service;

import com.kubis.microservices.customers.model.CustomerModel;
import com.kubis.microservices.customers.repository.CustomerRepository;
import com.kubis.microservices.customers.request.CustomerRequest;
import com.kubis.microservices.customers.utils.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Long addCustomer(CustomerRequest request){
        CustomerModel customerModel = CustomerModel.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(Security.hashPassword(request.getPassword()))
                .build();
        return customerRepository.save(customerModel).getId();
    }

    public List<CustomerModel> getCustomers(){
        return customerRepository.findAll();
    }

    public Optional<CustomerModel> getCustomerById(Long id){
        return customerRepository.findById(id);
    }

    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    public Optional<CustomerModel> updateCustomer(Long id, CustomerRequest request){
        Optional<CustomerModel> customerToUpdate = customerRepository.findById(id);

        if(customerToUpdate.isPresent()) {
            if(request.getEmail() != null){
                customerToUpdate.get().setEmail(request.getEmail());
            }
            if(request.getPassword() != null){
                customerToUpdate.get().setPassword(request.getPassword());
            }
            if(request.getFirstName() != null){
                customerToUpdate.get().setFirstName(request.getFirstName());
            }
            if(request.getLastName() != null){
                customerToUpdate.get().setLastName(request.getLastName());
            }
            if(request.getPhoneNumber() != null){
                customerToUpdate.get().setPhoneNumber(request.getPhoneNumber());
            }

            customerRepository.save(customerToUpdate.get());
        }

        return customerToUpdate;
    }
}
