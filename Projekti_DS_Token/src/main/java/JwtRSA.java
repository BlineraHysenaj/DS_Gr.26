import java.awt.List;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtRSA {

//	public static void main(String[] args) {
//
//		System.out.println("generating keys");
//		Map<String, Object> rsaKeys = null;
//
//		try {
//			rsaKeys = getRSAKeys();
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		PublicKey publicKey = (PublicKey) rsaKeys.get("public");
//		PrivateKey privateKey = (PrivateKey) rsaKeys.get("private");
//
//		System.out.println("generated keys");
//
//		String token = generateToken(privateKey);
//		System.out.println("Generated Token:\n" + token);
//
//		verifyToken(token, publicKey);
//
//	}

	public String generateToken(PrivateKey privateKey, String shfrytezuesi) {
		String token = null;
		try {
			Map<String, Object> claims = new HashMap<String, Object>();

			// put your information into claim
			claims.put("Shfrytezuesi: ", shfrytezuesi);
			claims.put("Valid: ", "Po");
			claims.put("Skadimi: ", new Date());

			token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.RS512, privateKey).compact();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	// verify and get claims using public key

	public ArrayList<String> verifyToken(String token, PublicKey publicKey) {
		Claims claims;
		ArrayList<String> list = new ArrayList<String>();
		try {
			claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

			list.add("Shfrytezuesi: " + claims.get("Shfrytezuesi: ").toString());
			list.add("Valid: " + claims.get("Valid: ").toString());
			list.add("Skadimi: " + claims.get("Skadimi: ").toString());
		} catch (Exception e) {

			list = null;
		}

		return list;
	}
}