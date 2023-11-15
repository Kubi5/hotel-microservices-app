package com.kubis.microservices.rooms.repository;

import com.kubis.microservices.rooms.model.CustomerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository <CustomerModel, Long> {
    List<CustomerModel> findAll();
    Optional<CustomerModel> findByEmail(String email);
}
