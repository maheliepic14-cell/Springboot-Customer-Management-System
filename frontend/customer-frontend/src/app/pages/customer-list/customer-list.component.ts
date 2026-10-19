import {Component, OnInit} from '@angular/core';
import {CustomerService} from '../../services/customer.service';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})


//implement OnInit to use the ngOnInit method
export class CustomerListComponent implements OnInit{

  //create a local variable with an empty array to hold the customers data
  customers: any[] = [];

  //inject the customer service through constructor
  constructor(private customerService: CustomerService) {}

  //we are calling the method inside ngoninit life cycle hook becuase to display the list of customers
  ngOnInit(): void{
    this.customerService.getCustomers().subscribe({
      //this part runs when the http request succeeds
      //next handles successfully emitted value
      next: (data)=>{
        //store the response in local variable
        this.customers = data;
        console.log("Customer loaded:", data);
      },
      //error is the name of the property in observable subscription option
      //(error) name of the variable
      error: (error) => {
        console.log("Error loading customers:", error);
      }

    });
  }


}
