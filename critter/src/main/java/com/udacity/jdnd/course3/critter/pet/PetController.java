package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.service_layer.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    /**
     * convertToPetDTO
     * @param pet
     * @return petDTO
     */
    private PetDTO convertToPetDTO(Pet pet) {
        PetDTO petsDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petsDTO);
        petsDTO.setOwnerId(pet.getCustomer().getId());
        return petsDTO;
    }

    @PostMapping("/save")
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet p = new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());
        PetDTO convertedPet;
        try {
            convertedPet = convertToPetDTO(petService.savePet(p, petDTO.getOwnerId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet could not be saved", e);
        }
        return convertedPet;
    }

    @GetMapping("/{petId}")
    public PetDTO getPetById(@PathVariable long petId) {
        Pet pet;
        try {
            pet = petService.getPetById(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet with id: " + petId + " not found", e);
        }
        return convertToPetDTO(pet);
    }

    @GetMapping("/get_all")
    public List<PetDTO> getAllPets(){
        List<Pet> pets = petService.getAllPets();
        return pets.stream().map(this::convertToPetDTO).collect(Collectors.toList());

    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets;
        try {
            pets = petService.getPetsByCustomerId(ownerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner pet with id " + ownerId + " not found", e);
        }
        return pets.stream().map(this::convertToPetDTO).collect(Collectors.toList());

    }
}
