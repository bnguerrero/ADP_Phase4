package com.example.phase4Auth.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;

public class JWTUtil{
	// initialize instance of Algorithm with a secret key
	// used for creation and verification of token
	private static final Algorithm algorithm = Algorithm.HMAC256("SECRET_KEY");
	
	// create JWT Token with permissions
	public static String createToken(String permissions) {
		try {
			long threeHoursInMillis = TimeUnit.HOURS.toMillis(3);
			// return an instance of JWTCreator.Builder class.
			// used to build JWT token
			return JWT.create()
					.withSubject("userPermissions")
					.withIssuer("briIssuer")
					.withClaim("permissions", permissions)
					.withExpiresAt(new Date(System.currentTimeMillis()+ threeHoursInMillis))
					.sign(algorithm);
		} catch (JWTCreationException e) {
			// log the error and throw a runtime exception
			System.err.println("Error: could not create JWTtoken: " + e.getMessage());
			throw new RuntimeException("Failed to create token.");
		}		
	}
	
	// verify JWT Token
	public static DecodedJWT verifyToken(String token) {
		try {
			return JWT.require(algorithm)
					.withIssuer("briIssuer")
					.build()
					.verify(token);
		} catch (JWTVerificationException exception) {
			System.out.println("invalid token: " + exception.getMessage());
			throw new RuntimeException("JWT token not verified", exception);
		}
	}

	public static Map<String, Claim> getClaims(String token) {
		try {
			// Verify JWT token and extract claims from it
			return JWT.require(algorithm)
					.withIssuer("briIssuer")
					.build()
					.verify(token)
					.getClaims();
		} catch(JWTVerificationException exception) {
			System.err.println("Invalid token: " + exception.getMessage());
			return null;
		}
	}
}






