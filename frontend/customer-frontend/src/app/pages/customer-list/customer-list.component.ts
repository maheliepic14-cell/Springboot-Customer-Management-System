import {Component, OnInit} from '@angular/core';
import {CustomerService} from '../../services/customer.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    FormsModule
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
  //spring calls ngOnInit when the component initializes
  ngOnInit(): void{
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getCustomersByPage(this.currentPage, this.pageSize).subscribe({
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

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadCustomers();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadCustomers();
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
    if (this.keyword.trim() === ''){
      this.loadCustomers();
      return;
    }

    this.customerService.searchCustomers(this.keyword.trim()).subscribe({
      next: (data) =>{
        this.customers = data;
        console.log("search results", data);
      },
      error: (error) => {
        console.log("Error searching customers:", error)

      }

    })

  }

}
