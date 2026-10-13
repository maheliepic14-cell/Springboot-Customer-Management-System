package com.customer.customer_management_system.repository;

import com.customer.customer_management_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


//Interface - shows only the methods not the implementation
// and this is interface because jpaRepo also an interface
//JpaRepository - its provide methods more that crudRepository

//Customer - which entity this repository manage
//Long - type of the entity's primary key
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
