import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CustomerService} from '../../services/customer.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-edit-customer',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './edit-customer.component.html',
  styleUrl: './edit-customer.component.css'
})
export class EditCustomerComponent implements OnInit{

  customerId!: number;

  customer = {
    name: '',
    address: '',
    phone: '',
    email: ''
  };

  selectedImage: File | null = null;
  errorMessage = '';
  successMessage = '';
  isLoading = true;
  isSubmitting = false;

  constructor( private route: ActivatedRoute, private customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.customerId = Number(this.route.snapshot.paramMap.get('id'));

    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (data) => {
        this.customer = {
          name: data.name,
          address: data.address,
          phone: data.phone,
          email: data.email
        };

        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading customer:', error);
        this.errorMessage = error.error?.message || 'Failed to load customer details.';
        this.isLoading = false;
      }
    });
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      this.selectedImage = input.files[0];
    } else {
      this.selectedImage = null;
    }
  }


  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (
      !this.customer.name.trim() ||
      !this.customer.address.trim() ||
      !this.customer.phone.trim() ||
      !this.customer.email.trim()
    ) {
      this.errorMessage = 'Please fill in all customer details.';
      return;
    }

    this.isSubmitting = true;

    this.customerService.updateCustomer(this.customerId, this.customer, this.selectedImage).subscribe({
        next: () => {
          this.successMessage = 'Customer updated successfully!';
          this.isSubmitting = false;

          setTimeout(() => {
            this.router.navigate(['/customers']);
          }, 1000);
        },
        error: (error) => {
          console.error('Error updating customer:', error);

          this.errorMessage = error.error?.message || 'Failed to update customer. Please try again.';

          this.isSubmitting = false;
        }
      });
  }


}
