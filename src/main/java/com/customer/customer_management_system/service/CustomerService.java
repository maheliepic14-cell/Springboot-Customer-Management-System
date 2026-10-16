package com.customer.customer_management_system.service;

import com.customer.customer_management_system.entity.Customer;
import com.customer.customer_management_system.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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

    private final CustomerImageService  customerImageService;

    public CustomerService(CustomerRepository customerRepository, CustomerImageService customerImageService){
        this.customerRepository = customerRepository;
        this.customerImageService = customerImageService;
    }

    public Customer saveCustomer(Customer customer, MultipartFile image) throws IOException {

        //Check whether the email exists
        if (customerRepository.existsByEmailIgnoreCase(customer.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        //Save the image if its provided
        if (image != null && !image.isEmpty()){
            String imagePath = customerImageService.saveImage(image);
            customer.setImagePath(imagePath);
        }

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer, MultipartFile image) throws IOException {

        //Check whether the customer exists
        Customer existingCustomer =  customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id:" + id));

        //Check another customer already uses this email
        boolean emailExists = customerRepository.existsByEmailIgnoreCaseAndIdNot(customer.getEmail(), id);

        if (emailExists){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        //Keep the existing image if new one is not provided
        String oldImagePath = existingCustomer.getImagePath();
        customer.setImagePath(oldImagePath);

        //Save a new image if one was uploaded
        if (image != null  && !image.isEmpty()){
            String newImagePath = customerImageService.saveImage(image);
            customer.setImagePath(newImagePath);
        }

        //Keep the ID from the URL
        customer.setId(id);
        // Save the customer record
        Customer updatedCustomer = customerRepository.save(customer);


        //Delete the old image after saving the new image
        if (image !=null && !image.isEmpty() && oldImagePath != null && !oldImagePath.isBlank()){
            customerImageService.deleteImage(oldImagePath);
        }

        return updatedCustomer;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    //Optional represents a customer that may or may not exist
    public Optional<Customer> getCustomerById(Long id){
        return customerRepository.findById(id);
    }

    public void deleteCustomer(Long id) throws IOException {

        //Get the customer details
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));

        String imagePath = customer.getImagePath();

        customerRepository.deleteById(id);

        //Delete the image in the upload folder
        if (imagePath != null && !imagePath.isBlank()){
            customerImageService.deleteImage(imagePath);
        }
    }

    public List<Customer> searchCustomers(String keyword){
        return customerRepository.findByNameContainingOrAddressContainingOrPhoneContainingOrEmailContaining(keyword, keyword, keyword, keyword);

    }

    public Page<Customer> searchCustomers(String keyword, Pageable pageable){
        return customerRepository.findByNameContainingOrAddressContainingOrPhoneContainingOrEmailContaining(keyword, keyword, keyword, keyword, pageable);
    }


    public Page<Customer> getCustomers(Pageable pageable){
        return customerRepository.findAll(pageable);

    }

}
