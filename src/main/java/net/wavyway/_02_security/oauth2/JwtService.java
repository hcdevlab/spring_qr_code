package net.wavyway._02_security.oauth2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;
import static net.wavyway._02_security.oauth2.SecurityConstants.*;

@Service("jwtService")
public class JwtService {

    private final Algorithm algorithm = Algorithm.HMAC512(SECRET.getBytes());

    public String generateJWTToken(String username) {

        try {
            String jwtToken = JWT.create()
                .withIssuer("Main Issuer")
                .withSubject(username)
                .withClaim("numberClaim", 79894511) // -> Un 'claim' arbitrario.
                .sign(algorithm);

            return TOKEN_PREFIX + jwtToken;
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public String getUserAfterVerifyJwtToken(String token) {
        if (isNull(token)) {
            throw new InvalidTokenException("Token has not been provided!");
        }

        try {

            String user = JWT.require(algorithm)
                .withIssuer("Main Issuer")
                .withClaim("numberClaim", 79894511)
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();

            if (!user.isBlank() || (!user.isEmpty()) || (user == null)) {
                return user;
            } else {
                return "empty";
            }
        } catch (JWTVerificationException ex) {
            System.out.println("Verify JWT token fail: " + ex.getMessage());
            System.out.println("JWTVerificationException stacktrace: " + ex.getStackTrace());
            return "empty";
        }
    }

    public String getAuthorizationCookieFromRequest(HttpServletRequest request) {
        String result = null;
        Cookie[] cookies = request.getCookies();

        try {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        result = cookie.getValue();
                    }
                }
            }
            return result;

        } catch (Exception ex) {
            throw new InvalidTokenException("There is no Authorization in Cookie.", ex);
        }

    }

    public String getBearerTokenFromCookie(Cookie[] cookies) {
        String result = null;

        try {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        result = cookie.getValue();
                    }
                }
            } else {
                result = "empty";
            }
            return result;

        } catch (Exception ex) {
            throw new InvalidTokenException("There is no Authorization in Cookie.", ex);
        }

    }
}
