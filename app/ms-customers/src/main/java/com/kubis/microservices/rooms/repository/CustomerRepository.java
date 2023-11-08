package com.kubis.microservices.rooms.repository;

import com.kubis.microservices.rooms.model.CustomerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository <CustomerModel, Long> {
    List<CustomerModel> findAll();
    CustomerModel findByEmail(String email);
}
