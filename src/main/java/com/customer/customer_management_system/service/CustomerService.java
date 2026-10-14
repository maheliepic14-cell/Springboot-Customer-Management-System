package com.customer.customer_management_system.service;

import com.customer.customer_management_system.entity.Customer;
import com.customer.customer_management_system.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//Tells Spring to detect and manage CustomerService as a service Bean.
//Declares a field where the service will store the repository reference.
//The constructor - Receives the repository object and assigns it to the service's field.
//Spring's dependency injection - Supplies the repository object when it creates the service object.


//this tells customerService is a service component
//because of this it creates a spring bean
@Service
public class CustomerService {

    //calling the bean class
    //creat a variable
    //holds customer repository dependency
    //this is dependency because service need this to work
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    //Optional represents a customer that may or may not exist
    public Optional<Customer> getCustomerById(Long id){
        return customerRepository.findById(id);
    }

    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }

    public List<Customer>searchCustomers(String keyword){
        return customerRepository.findByNameContainingOrAddressContainingOrPhoneContainingOrEmailContaining(keyword, keyword, keyword, keyword);

    }

}
