package com.customer.customer_management_system.controller;

import com.customer.customer_management_system.entity.Customer;
import com.customer.customer_management_system.service.CustomerImageService;
import com.customer.customer_management_system.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

//Add cors because frontend and backend running on different ports
//So this tells java allows requests from port 4200
@CrossOrigin(origins = "http://localhost:4200")
//tells spring this class handles web requests and return response
@RestController
//set the url path
@RequestMapping("/api/customers")
public class CustomerController {

    //get the customer bean
    private final CustomerService customerService;
    private final CustomerImageService customerImageService;

    public CustomerController(CustomerService customerService, CustomerImageService customerImageService){
        this.customerService = customerService;
        this.customerImageService = customerImageService;
    }

    //Map http get requests to this method
    @GetMapping
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    //tells java to read the incoming json and convert it into a java customer object
    //@Valid tells spring to check validation annotation before calling the method
    //if validation fails spring will reject the request
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Customer addCustomer(@Valid @RequestPart("customer") Customer customer, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return customerService.saveCustomer(customer, image);
    }

    //@Path variable reads the id from the url and assign it to this id
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id:" + id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Customer updateCustomer(@Valid @PathVariable Long id, @RequestPart("customer") Customer customer, @RequestPart (value = "image", required = false) MultipartFile image) throws IOException {
        return customerService.updateCustomer(id, customer, image);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) throws IOException{
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

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getCustomerImage(@PathVariable Long id) throws IOException{

        Customer customer = customerService.getCustomerById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id:" + id));

        if (customer.getImagePath() == null || customer.getImagePath().isBlank()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This customer has no profile image");
        }

        byte[] image = customerImageService.getImage(customer.getImagePath());

        String contentType = customerImageService.getImageContentType(customer.getImagePath());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).body(image);

    }


}
