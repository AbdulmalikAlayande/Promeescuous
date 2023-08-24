package africa.semicolon.promeescuous.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

public class AppUtil {
	
	private static final String TEMPLATE_FILE_PATH  = "file:///C:/Users/USER/IdeaProjects/promeescuous/promiscuous/src/main/resources/templates/index.html";
	
	public static String generateValidationToken(String email){
		JWTCreator.Builder tokenCreator = JWT.create()
				.withClaim("user mail", email)
				.withExpiresAt(Instant.now().plusSeconds(3600))
				.withIssuer("Promiscuous Incorporation")
				.withIssuedAt(Instant.now());
		return tokenCreator.sign(Algorithm.HMAC512("secret"));
	}
	
	public static String getMailTemplate() throws URISyntaxException, IOException {
		Path filePath = Paths.get(new URI(TEMPLATE_FILE_PATH));
		List<String> templateFileLines = Files.readAllLines(filePath);
		return String.join("", templateFileLines);
	}
	
	public static String generateActivationLink(String email){
		// validationToken
		String baseUrl = "https:localhost:8080";
		String activationPath = "/activate";
		char queryStringPrefix = '?';
		String queryStringKey = "code=";
		String validationToken = generateValidationToken(email);
		return new StringBuilder().append(baseUrl)
								  .append(activationPath)
								  .append(queryStringPrefix)
				                  .append(queryStringKey)
								  .append(validationToken).toString();
	}
	
	public static String extractEmailFromToken(String token){
		Claim claim = JWT.decode(token).getClaim("user mail");
		return claim.asMap().get("user mail").toString();
	}
	
	public static boolean isValidToken(String token){
		JWTVerifier verifier = JWT.require(Algorithm.HMAC512("secret"))
				                       .withIssuer("Promiscuous Incorporation")
				                       .withClaimPresence("user")
				                       .build();
		return verifier.verify(token)!=null;
	}
	
	public static boolean isValidJwt(String token){
		JWTVerifier verifier = JWT.require(Algorithm.HMAC512("secret"))
				                       .withIssuer("Promiscuous Incorporation")
				                       .withClaimPresence("user")
				                       .build();
		return verifier.verify(token)!=null;
	}
	
	public static String extractEmailFrom(String token){
		var claim = JWT.decode(token).getClaim("user");
		return (String) claim.asMap().get("user");
	}
}
