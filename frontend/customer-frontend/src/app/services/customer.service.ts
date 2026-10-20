import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

//@injectable enables dependency injection
//inject constructor arguments through dependency injection
//allows to create instance of the service
@Injectable({
  //can create a single instance of customer service and can ber shared across the entire app
  providedIn: 'root'
})
export class CustomerService {

  //stored the customer api address
  private apiUrl = "http://localhost:8080/api/customers";

//inject the http through constructor
  //here inject the httpclient instance
  //http is property name
  //HttpClient is instance of the httpclient

  //inject http client service through the constructor
  constructor( private http: HttpClient ) { }

  //get method
  //Observable is return type
  //its represent the future http response
  getCustomers() : Observable<any>{

    //return http get call request
    //define the type of the response as any
    //send a request
    return this.http.get<any>(this.apiUrl)
    //basically this is sending GET http://localhost:8080/api/customers
  }

  searchCustomers(keyword: string): Observable<any>{
    //handles spaces and special characters in the search text
    //sends the request to the backend
    return this.http.get<any>(`${this.apiUrl}/search?keyword=${encodeURIComponent(keyword)}`);

  }


  //getCustomerByPage(0,5)
  getCustomersByPage(page: number, size: number): Observable<any> {

    //this returns customers data as observable
    return this.http.get<any>(
      //GET http://localhost:8080/api/customers/page?page=0&size5
      `${this.apiUrl}/page?page=${page}&size=${size}`
    );
  }

  searchCustomersByPage(keyword: string, page: number, size: number): Observable<any> {
    return this.http.get<any>(
      `${this.apiUrl}/search/page?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`
    );
  }

  //here we need to create a post request with the data
  addCustomer(customer: object, image: File | null): Observable<any>{

    //creates a formcata object
    const formData = new FormData();

    //add customer data

    //creates a new blob object - represent piece of data
    //convert javascript object into json string
    //sets the content type to json
    formData.append(
      'customer',
      new Blob([JSON.stringify(customer)], {
        type: 'application/json'
      })
    );

    if (image) {
      formData.append('image', image);
    }

    //Http post request
    return this.http.post(this.apiUrl, formData);


  }

}
