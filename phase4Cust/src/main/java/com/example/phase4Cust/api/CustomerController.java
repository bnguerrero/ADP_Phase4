package com.example.phase4Cust.api;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.domain.Customer;
import com.example.demo.repository.CustomerRepository;

@RestController
@RequestMapping
public class CustomerController{
	
	@Autowired
	CustomerRepository repo;
	
	// get customer by ID
	@GetMapping("/customer/{id}")
	public Optional<Customer> getCustomerById(@PathVariable Long id) {
		return repo.findById(id);
	}
	
	// get all customers
	@GetMapping("/customers")
	public Iterable<Customer> getAllCustomers(){
		return repo.findAll();
	}
	
	// create a new customer
	@PostMapping
	public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer){
		if(newCustomer.getName()== null
				|| newCustomer.getEmail()== null) {
			return ResponseEntity.badRequest().build();
		}
		newCustomer=repo.save(newCustomer);
		
		URI location =
				ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(newCustomer.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	// update or create a customer by email 
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCustomerDetails(
	        @RequestBody Customer customerDetails,
	        @PathVariable long id)
	{
	    // check to see if name section is filled out correctly
	    if (customerDetails.getName().isBlank()) {
	        return ResponseEntity.badRequest().body("please input a valid name");
	    }
	    // check to see if email section is filled out correctly
	    if (customerDetails.getEmail().isBlank()) {
	        return ResponseEntity.badRequest().body("invalid email");
	    }
	    // check if ID matches
	    if (customerDetails.getId() != id) {
	        return ResponseEntity.badRequest().body("Customer ID in the path does not match");
	    }
	    //updated or create customer details
	    customerDetails = repo.save(customerDetails);
	    return ResponseEntity.ok().build();
	}

	
	
	// delete a customer by id
	@DeleteMapping("/customer/{id}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable Long id){
		if(repo.existsById(id) ) {
			repo.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	// delete all customers
	@DeleteMapping("/customers")
	public ResponseEntity<?> deleteAllCustomers(){
		repo.deleteAll();
		return ResponseEntity.noContent().build();
	}
	
}