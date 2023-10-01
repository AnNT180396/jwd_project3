package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.model.Schedule;
import com.udacity.jdnd.course3.critter.service_layer.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedule.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService schedulesService;

    /**
     * convertToScheduleDTO
     * @param schedule
     * @return schedulesDTO
     */
    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO schedulesDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, schedulesDTO);
        List<Long> employeeIds = schedule.getEmployee().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petIds = schedule.getAllPets().stream().map(Pet::getId).collect(Collectors.toList());
        schedulesDTO.setPetIds(petIds);
        schedulesDTO.setEmployeeIds(employeeIds);
        return schedulesDTO;
    }

    /**
     * createSchedule
     * @param scheduleDTO
     * @return scheduleDTO
     */
    @PostMapping("/save")
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities());;
        ScheduleDTO convertedSchedule;
        try {
            convertedSchedule = convertToScheduleDTO(schedulesService.saveSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule could not be saved", e);
        }
        return convertedSchedule;
    }

    /**
     * getAllSchedule
     * @return list schedule
     */
    @GetMapping("/get_all")
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = schedulesService.getAllSchedules();
        List<ScheduleDTO> schedulesDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(schedules)) {
            schedulesDTOS.addAll(schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList()));
        }
        return schedulesDTOS;
    }

    /**
     * getScheduleForPet
     * @param petId
     * @return list schedule
     */
    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules;
        try {
            schedules = schedulesService.getScheduleByPetId(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet schedule with pet id: " + petId + " not found", e);
        }
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    /**
     * getScheduleForEmployee
     * @param employeeId
     * @return list schedule
     */
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules;
        try {
            schedules = schedulesService.getScheduleByEmployeeId(employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee schedule with employee id: " + employeeId + " not found", e);
        }
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    /**
     * getScheduleForCustomer
     * @param customerId
     * @return list schedule
     */
    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules;
        try {
            schedules = schedulesService.getScheduleByCustomerId(customerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule with owner id " + customerId + " not found", e);
        }
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }
}