package com.udacity.jdnd.course3.critter.service_layer;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.repository_layer.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository_layer.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;
   
    /**
     * savePet
     * @param pet
     * @param customerId
     * @return pet
     */
    public Pet savePet(Pet pet, Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Pet> pets = new ArrayList<>();

        pet.setCustomer(customer);
        pet = petRepository.save(pet);
        pets.add(pet);
        customer.setPets(pets);
        customerRepository.save(customer);

        return pet;
    }

    /**
     * getAllPets
     * @return list pet
     */
    public List<Pet> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets;
    }

    /**
     * getPetById
     * @param petId
     * @return pet
     */
    public Pet getPetById(Long petId) {
        Pet pet = petRepository.getOne(petId);
        return pet;
    }

    /**
     * getPetsByCustomerId
     * @param customerId
     * @return list pet
     */
    public List<Pet> getPetsByCustomerId(long customerId) {
        List<Pet> pets = petRepository.findPetByCustomerId(customerId);
        return pets;
    }
}
