package com.customer.customer_management_system.controller;

import com.customer.customer_management_system.entity.Customer;
import com.customer.customer_management_system.service.CustomerService;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    //@Path variable reads the id from the url and assign it to this id
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        //assigns the URL ID to that object
        customer.setId(id);
        return customerService.saveCustomer(customer);

    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }


}
