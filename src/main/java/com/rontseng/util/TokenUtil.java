
package com.rontseng.util;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import com.rontseng.dao.support.PagedQueryVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * @author Ron
 *
 */
public class TokenUtil {
	private static final Key SECRET_KEY = MacProvider.generateKey();
	private static final String ISSUER = "Ron";

	@SuppressWarnings("rawtypes")
	public static String createToken(PagedQueryVO queryVO) {
		String token = null;
		Claims claims = Jwts.claims();

		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 1);
		Date expiration = now.getTime();

		claims.setIssuer(ISSUER);
		claims.setIssuedAt(DateUtil.getCurrentDate());
		claims.setExpiration(expiration);

		token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

		return token;
	}

	public static boolean validateToken(String token, String audience) {
		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
			// check Issuer
			if (!claims.getIssuer().equals(ISSUER))
				return false;
			// check Audience
			if (!claims.getAudience().equals(audience))
				return false;
			// check Expiration
			if (DateUtil.getCurrentDate().after(claims.getExpiration()))
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
