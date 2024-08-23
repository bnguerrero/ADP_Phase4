package com.example.phase4Auth.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.phase4Auth.domain.Customer;
import com.example.phase4Auth.domain.Token;
import com.example.phase4Auth.util.JWTUtil;

@RestController
@RequestMapping("/token")
public class AuthController{
	
	// static variable to store the application's JWT token
	private static Token applicationToken;
	
	/**
	 * returns placeholder JWT string for testing.
	 * Method handles GET requests to /token
	 */
	@GetMapping
	public String getAllTokens() {
		return "jwt-sample-token-123".toString();
	}
	
	/**
	 * method for POST requests to /customers
	 * @param customer, customer object containing username and password.
	 * @return a ResponseEntity containing the generated JWT token or an unauthorized status.
	 */
	@PostMapping
	public ResponseEntity<?> createCustomerToken(@RequestBody Customer customer){
		String username = customer.getName();
		String password = customer.getPassword();
		
		// validate the customers username and password
		if(isValidCredentials(username, password)) {
			Token token = generateToken(username);
			return ResponseEntity.ok(token);
		}
		// return unauthorized status if not validated
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	/**
	 * used by createCustomerToken to validate customers username and password
	 * by checking it against a special case or quering customer api.
	 * @param username username to validate
	 * @param password password to validate
	 * @return true if the credentials are valid, false if not.
	 */
	private boolean isValidCredentials(String username, String password) {
		if("ApiClientApp".equals(username)&& "secret".equals(password)) {
			return true;
		}
		Customer customer = getCustomerByUsername(username);
		return customer != null && customer.getName().equals(username)
				&& customer.getPassword().equals(password);
	}
	
	
	/**
	 * gets the application JWT token, creating it if necessary.
	 * @return the application's JWT token.
	 */
	public static Token getApplicationToken() {
		if(applicationToken == null || applicationToken.getToken()==null|| 
				applicationToken.getToken().isEmpty() || applicationToken.getToken().isBlank()) {
			applicationToken = generateToken("ApiClientApp");
		}
		return applicationToken;
	}
	
	/**
	 * generates a JWT token for the given username with correct permissions.
	 * @param username username the token is to be generated.
	 * @return a token object containing the generated JWT
	 */
	private static Token generateToken(String username) {
		String permissions = "com.example.data.apis";
		if("ApiClientApp".equalsIgnoreCase(username)) {
			permissions = "com.example.auth.apis";
		}
		String jwtToken = JWTUtil.createToken(permissions);
		return new Token(jwtToken);
	}
	
	
	/**
	 * get customer details from customer api using the given username
	 * @param username username to lookup
	 * @return a customer object if found, or null if theres an error.
	 */
	private Customer getCustomerByUsername(String username) {
		try {
			URL url = new URL("http://localhost:8080/api/customers/byname/" + username);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			
			Token token = getApplicationToken();
			connection.setRequestProperty("Authorization", "Bearer" + token.getToken());
			
			if (connection.getResponseCode() != HttpStatus.OK.value()) {
				return null;
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder responseBuilder = new StringBuilder();
			String line;
			
			while((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			
			connection.disconnect();
			return CustomerFactory.createCustomer(responseBuilder.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}