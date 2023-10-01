package com.udacity.jdnd.course3.critter.service_layer;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository_layer.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository_layer.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customersRepository;

    /**
     * saveCustomer
     * @param customer
     * @param petIds
     * @return customer
     */
    public Customer saveCustomer(Customer customer, List<Long> petIds) {
        List<Pet> pets = new ArrayList<>();
        if (!CollectionUtils.isEmpty(petIds)) {
            pets = petIds.stream().map((petId) -> petRepository.getOne(petId)).collect(Collectors.toList());
        }
        customer.setPets(pets);
        return customersRepository.save(customer);
    }

    /**
     * getAllCustomers
     * @return list customer
     */
    public List<Customer> getAllCustomers() {
        return customersRepository.findAll();
    }

    /**
     * getOwnerByPet
     * @param petId
     * @return customer
     */
    public Customer getOwnerByPet(Long petId) {
        Pet petInfo = petRepository.getOne(petId);
        if (Objects.nonNull(petInfo)) {
          return petInfo.getCustomer();
        }
        return null;
    }
}
