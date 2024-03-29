package com.invoice.papaInvoice.Service;

import com.invoice.papaInvoice.Entity.Customer;
import com.invoice.papaInvoice.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> listAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    public Customer updateCustomer(Customer customer) {
        // Save and return the updated customer
        // For example, if you are using JPA repository:
        return customerRepository.save(customer);
    }

    public List<Customer> searchCustomersByName(String name) {
        // Implementation depends on how you're accessing your database
        // For example, if you're using Spring Data JPA, you could use a method like
        // `findByNameContainingIgnoreCase(String name)` in your CustomerRepository.
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

}
