package com.example.phase4Auth.domain;

import org.json.JSONObject;

public class CustomerFactory{
	
	/**
	 * creates customer object from JSON string
	 *
	 * @param jsonString JSON representation of customer
	 * @return new customer object with data from JSON string
	 * @throws IllegalArgumentException if JSON string is missing required fields.
	 */
	public static Customer jsonToCustomer(String jsonString) {
		// parse JSON string to JSONObject
		JSONObject jsonObject = new JSONObject(jsonString);
		
		int id = (int) jsonObject.get("id");
		String name = (String) jsonObject.get("name");
		String email = (String) jsonObject.get("email");
		String password = (String) jsonObject.get("password");
		
		// create customer object
		Customer customer = new Customer();
		customer.setName(name);
		customer.setId((long) id);
		customer.setEmail(email);
		customer.setPassword(password);
		return customer;
	}
	
	/**
	 * Converts a Customer object into a JSON string.
	 *
     * @param customer The Customer object to be converted.
     * @return A JSON string representing the customer.
	 */
	 public static String convertCustomerToJson(Customer customer) {
	    // Create a JSONObject and populate it with Customer data
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("id", customer.getId());
	    jsonObject.put("name", customer.getName());
	    jsonObject.put("email", customer.getEmail());
	    jsonObject.put("password", customer.getPassword());

	    // Return the JSON string representation of the Customer
	    return jsonObject.toString();
	    }
	}