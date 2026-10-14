package com.customer.customer_management_system.repository;

import com.customer.customer_management_system.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//Interface - shows only the methods not the implementation
// and this is interface because jpaRepo also an interface
//JpaRepository - its provide methods more that crudRepository

//Customer - which entity this repository manage
//Long - type of the entity's primary key
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //Spring Data JPA reads the method name and generates the query implementation
    List<Customer> findByNameContainingOrAddressContainingOrPhoneContainingOrEmailContaining(String name, String address, String phone, String email);

    Page<Customer> findByNameContainingOrAddressContainingOrPhoneContainingOrEmailContaining(String name, String address, String phone, String email, Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

}
