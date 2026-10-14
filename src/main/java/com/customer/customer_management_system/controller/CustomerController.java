package com.customer.customer_management_system.controller;

import com.customer.customer_management_system.entity.Customer;
import com.customer.customer_management_system.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//tells spring this class handles web requests and return response
@RestController
//set the url path
@RequestMapping("/api/customers")
public class CustomerController {

    //get the customer bean
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    //Map http get requests to this method
    @GetMapping
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    //tells java to read the incoming json and convert it into a java customer object
    //@Valid tells spring to check validation annotation before calling the method
    //if validation fails spring will reject the request
    @PostMapping
    public Customer addCustomer(@Valid @RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    //@Path variable reads the id from the url and assign it to this id
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id:" + id));
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@Valid @PathVariable Long id, @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }

    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String keyword){
        return customerService.searchCustomers(keyword);
    }

    @GetMapping("/page")
    //Page - stores which page the user requests
    //Size - stores how many customers should appear on the page
    public Page<Customer> getCustomerByPage(@RequestParam int page, @RequestParam int size){

        //Pageable is an interface and describe pagination instruction
        //PageRequest is a class that can create an object containing pagination instructions
        Pageable pageable = PageRequest.of(page, size);
        return customerService.getCustomers(pageable);

    }

    @GetMapping("/search/page")
    public Page<Customer> searchCustomersByPage(@RequestParam String keyword, @RequestParam(defaultValue= "0") int page, @RequestParam(defaultValue= "5") int size){

        Pageable pageable = PageRequest.of(page, size);

        return customerService.searchCustomers(keyword, pageable);

    }


}
