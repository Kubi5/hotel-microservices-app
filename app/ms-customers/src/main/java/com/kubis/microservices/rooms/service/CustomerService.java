package com.kubis.microservices.rooms.service;

import com.kubis.microservices.rooms.model.CustomerModel;
import com.kubis.microservices.rooms.repository.CustomerRepository;
import com.kubis.microservices.rooms.request.CustomerRequest;
import com.kubis.microservices.rooms.utils.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Long addCustomer(CustomerRequest request){
        if(request.getRole() == null){
            request.setRole("user");
        }
        CustomerModel customerModel = CustomerModel.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(Security.hashPassword(request.getPassword()))
                .role(request.getRole())
                .build();
        return customerRepository.save(customerModel).getCustomerId();
    }

    public List<CustomerModel> getCustomers(){
        return customerRepository.findAll();
    }

    public Optional<CustomerModel> getCustomerById(long id){
        return customerRepository.findById(id);
    }

    public void deleteCustomer(long customerId){
        customerRepository.deleteById(customerId);
    }

    public Optional<CustomerModel> updateCustomer(long id, CustomerRequest request){
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

    public boolean customerDataCorrect(String email, String password){
        Optional<CustomerModel> customer = customerRepository.findByEmail(email);

        if(customer.isPresent()) {
            return BCrypt.checkpw(password.getBytes(StandardCharsets.UTF_8), customer.get().getPassword());
        }
        return false;
    }

    public Optional<CustomerModel> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
