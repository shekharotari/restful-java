package com.example.shopping.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.shopping.model.CustomerDO;
import com.example.shopping.model.CustomerDOList;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
	private Map<Integer, CustomerDO> customerDB = new ConcurrentHashMap<Integer, CustomerDO>();
	private AtomicInteger idCounter = new AtomicInteger(0);
	
	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CustomerDOList> getCustomers() {
		List<CustomerDO> customers = new ArrayList<CustomerDO>();
		customerDB.values().forEach((CustomerDO customerDO) -> customers.add(customerDO));
		
		CustomerDOList customerDOList = new CustomerDOList();
		customerDOList.setCustomers(customers);
		
		ResponseEntity<CustomerDOList> response = new ResponseEntity<CustomerDOList>(customerDOList, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CustomerDO> getCustomer(@PathVariable(value = "id") int id) {
		CustomerDO customer = customerDB.get(id);
		if (customer == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + id + " not found");
		}
		
		ResponseEntity<CustomerDO> response = new ResponseEntity<CustomerDO>(customer, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(value = "/byNameAndCity/{name}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CustomerDO> getCustomer(@PathVariable(value = "name") String name, 
			@MatrixVariable(value = "city") String city) {
		CustomerDO customer = null;
		
		for (CustomerDO customerDO : customerDB.values()) {
			if (customerDO.getFirstName().equalsIgnoreCase(name) && customerDO.getCity().equalsIgnoreCase(city)) {
				customer = customerDO;
				break;
			}
		}
		
		if (customer == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with name " + name + " and city " + city + " not found");
		}
		
		ResponseEntity<CustomerDO> response = new ResponseEntity<CustomerDO>(customer, HttpStatus.OK);
		return response;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CustomerDO> createCustomer(@RequestBody CustomerDO customerDO) {
		customerDO.setId(idCounter.incrementAndGet());
		customerDB.put(customerDO.getId(), customerDO);
		
		System.out.println("Created customer id " + customerDO.getId());
		
		ResponseEntity<CustomerDO> response = new ResponseEntity<CustomerDO>(customerDO, HttpStatus.CREATED);
		return response;
	}
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_XML_VALUE, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CustomerDO> updateCustomer(@PathVariable(value = "id") int id, @RequestBody CustomerDO customerDO) {
		CustomerDO customer = customerDB.get(id);
		if (customer == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + id + " not found");
		}
		
		customer.setCity(customerDO.getCity());
		customer.setCountry(customerDO.getCountry());
		
		System.out.println("Updated customer id " + customerDO.getId());
		
		ResponseEntity<CustomerDO> response = new ResponseEntity<CustomerDO>(customer, HttpStatus.OK);
		return response;
	}
	
	@DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CustomerDO> deleteCustomer(@PathVariable(value = "id") int id) {
		CustomerDO customer = customerDB.get(id);
		if (customer == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + id + " not found");
		}
		
		customerDB.remove(id);
		
		System.out.println("Removed customer id " + customer.getId());
		
		ResponseEntity<CustomerDO> response = new ResponseEntity<CustomerDO>(customer, HttpStatus.OK);
		return response;
	}
	
}
