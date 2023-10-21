package com.kubis.microservices.customers.repository;

import com.kubis.microservices.customers.model.CustomerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository <CustomerModel, Long> {

}
