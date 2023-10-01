package com.udacity.jdnd.course3.critter.service_layer;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.repository_layer.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository_layer.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository_layer.PetRepository;
import com.udacity.jdnd.course3.critter.repository_layer.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    /**
     * saveSchedule
     * @param schedule
     * @param employeeIds
     * @param petIds
     * @return schedule
     */
    public Schedule saveSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Pet> pets = petRepository.findAllById(petIds);
        List<Employee> employee = employeeRepository.findAllById(employeeIds);

        schedule.setPets(pets);
        schedule.setEmployee(employee);

        return scheduleRepository.save(schedule);
    }

    /**
     * getAllSchedule
     * @return list schedule
     */
    public List<Schedule> getAllSchedules() {
        List<Schedule> allSchedule = scheduleRepository.findAll();
        return allSchedule;
    }

    /**
     * getScheduleByCustomerId
     * @param customerId
     * @return list schedule
     */
    public List<Schedule> getScheduleByCustomerId(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Schedule> schedules = new ArrayList<>();
        if (Objects.nonNull(customer)) {
            customer.getAllPets().forEach(pet -> {
                schedules.addAll(scheduleRepository.findScheduleByPet(pet));
            });
        }
        return schedules;

    }

    /**
     * getScheduleByEmployeeId
     * @param employeeId
     * @return list schedule
     */
    public List<Schedule> getScheduleByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> schedules = new ArrayList<>();
        if (Objects.nonNull(employee)) {
            schedules = scheduleRepository.findScheduleByEmployee(employee);
        }
        return schedules;
    }

    /**
     * getScheduleByPetId
     * @param petId
     * @return list schedule
     */
    public List<Schedule> getScheduleByPetId(Long petId) {
        Pet pet = petRepository.getOne(petId);
        if (Objects.nonNull(pet)) {
            return scheduleRepository.findScheduleByPet(pet);
        }
        return null;
    }
}
