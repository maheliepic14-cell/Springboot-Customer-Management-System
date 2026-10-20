import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {CustomerService} from '../../services/customer.service';

@Component({
  selector: 'app-add-customer',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css'
})
export class AddCustomerComponent {

  //creates a property customer
  customer = {
    name: '',
    address: '',
    phone: '',
    email: ''
  };

  //creates a property for selectedImage
  //property can hold file object and also no file
  selectedImage: File | null = null;

  errorMessage = '';
  successMessage = '';
  //Track whether form submission is in progress
  isSubmitting = false;

  //Constructor injection
  constructor(private customerService: CustomerService, private router: Router) {}

  //method runs when html file input triggers
  //declare a parameter event
  onImageSelected(event: Event): void {

    //identify the element that triggered the event and treat it as htmlinput
    const input = event.target as HTMLInputElement;

    //checks user has selected atleast on file
    if (input.files && input.files.length > 0) {

      //stores the file in to the selectedImage property
      this.selectedImage = input.files[0];

  }else {
      this.selectedImage = null;
    }
  }

  onSubmit(): void{

    //clears any older error messages
    this.errorMessage = "";
    this.successMessage = "";

    //validate the required fields
    if (
      !this.customer.name.trim() ||
      !this.customer.address.trim() ||
      !this.customer.phone.trim() ||
      !this.customer.email.trim()
    ) {
      this.errorMessage = 'Please fill in all customer details.';
      //exit the onSubmit method
      return;
    }

    //Updates the state to indicate that the submission is in progress
    this.isSubmitting = true;

    this.customerService.addCustomer(this.customer, this.selectedImage).subscribe({
      next: () =>{
        this.successMessage = "Customer added successfully";
        this.isSubmitting = false;

        setTimeout(() => {
          this.router.navigate(['/customers']);
        }, 1000);
      },

      error: (error)=> {
        console.error('Error adding customer:', error);

        //access the message property inside the error response

        this.errorMessage = error.error?.message || 'Failed to add customer. Please try again.';
        this.isSubmitting = false;

      }

    })

  }

}
