import {Component, OnInit} from '@angular/core';
import {CustomerService} from '../../services/customer.service';
import {FormsModule} from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    FormsModule, RouterLink
  ],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})


//implement OnInit to use the ngOnInit method
export class CustomerListComponent implements OnInit{

  //properties

  //create a local variable with an empty array to hold the customers data
  customers: any[] = [];

  //stores the keyword
  keyword: string = '';

  currentPage: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;
  totalElements: number = 0;

  //inject the customer service through constructor
  constructor(private customerService: CustomerService) {}

  //we are calling the method inside ngoninit life cycle hook becuase to display the list of customers
  //when the component initializes the component lifecycle hook trigger so ngOnInIt runs
  ngOnInit(): void{
    this.loadCustomers();
  }

  loadCustomers(): void {
    //initial values are 0, 5
    //so this calls getcustomerbypage(0,5)
    this.customerService.getCustomersByPage(this.currentPage, this.pageSize).subscribe({
      //gets observable response data
      //if this success then onl
      next: (data) => {
        this.customers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;

        console.log('Paged customers:', data);
      },
      error: (error) => {
        console.log("Error loading customers:", error);
      }
    });
  }

  loadCurrentPage(): void {
    if (this.keyword.trim() === '') {
      this.loadCustomers();
      return;
    }

    this.customerService.searchCustomersByPage(this.keyword.trim(), this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        this.customers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      },
      error: (error) => {
        console.error('Error loading page:', error);
      }
    });
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadCurrentPage();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadCurrentPage();
    }
  }


  onImageError(event: Event): void{

    //event.target identifies the html element that triggered the event
    //and tells java to treat it as an image element
    const image = event.target as HTMLImageElement;

    image.onerror = null;
    image.src = "/default-profile.png";
  }


  searchCustomers(): void {
    this.currentPage = 0;

    if (this.keyword.trim() === '') {
      this.loadCustomers();
      return;
    }

    this.customerService.searchCustomersByPage(this.keyword.trim(), this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        this.customers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;

        console.log('Search results:', data);
      },
      error: (error) => {
        console.error('Error searching customers:', error);
      }
    });
  }


}
