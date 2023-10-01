package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.model.Customer;
import com.udacity.jdnd.course3.critter.model.Employee;
import com.udacity.jdnd.course3.critter.model.Pet;
import com.udacity.jdnd.course3.critter.service_layer.CustomerService;
import com.udacity.jdnd.course3.critter.service_layer.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employee. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customersService;

    @Autowired
    EmployeeService employeeService;

    /**
     * convertToCustomersDTO
     * @param customer
     * @return customersDTO
     */
    private CustomerDTO convertToCustomersDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Long> petIds = customer.getAllPets().stream().map(Pet::getId).collect(Collectors.toList());
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private EmployeeDTO convertToEmployeesDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    @PostMapping("/customer/save")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        CustomerDTO customersDTO;
        Customer customer = new Customer(customerDTO);
        try {
            customersDTO = convertToCustomersDTO(customersService.saveCustomer(customer, customerDTO.getPetIds()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not save Customer", e);
        }
        return customersDTO;
    }

    @GetMapping("/customer/get_all")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customersService.getAllCustomers();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customers)) {
            customerDTOS.addAll(customers.stream().map(this::convertToCustomersDTO).collect(Collectors.toList()));
        }
        return customerDTOS;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = customersService.getOwnerByPet(petId);
        if (Objects.isNull(customer)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner By PetId: " + petId + " not found!!");
        }
        return convertToCustomersDTO(customer);
    }

    @PostMapping("/employee/save")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO);
        try {
            employeeDTO = convertToEmployeesDTO(employeeService.saveEmployee(employee));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not save Employee", e);
        }
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee;
        try {
            employee = employeeService.getEmployee(employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee By employeeId: " + employeeId + " not found!!", e);
        }
        return convertToEmployeesDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            employeeService.setAvailability(daysAvailable, employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee By employeeId: " + employeeId + " not found. Can not save daysAvailable", e);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        List<Employee> employee = employeeService.findEmployeesForService(employeeDTO);
        if (!CollectionUtils.isEmpty(employee)) {
            return employee.stream().map(this::convertToEmployeesDTO).collect(Collectors.toList());
        }
        return employeeDTOS;
    }

}
