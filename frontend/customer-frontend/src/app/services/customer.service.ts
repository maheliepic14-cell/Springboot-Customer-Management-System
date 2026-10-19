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


/*  getCustomers(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }*/

}
