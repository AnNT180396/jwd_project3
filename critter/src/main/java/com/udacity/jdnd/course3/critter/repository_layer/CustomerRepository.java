package com.udacity.jdnd.course3.critter.repository_layer;

import com.udacity.jdnd.course3.critter.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
