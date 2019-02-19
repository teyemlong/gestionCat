/**
 * 
 */
package org.sid.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

/**
 * @author Robert
 *
 */
public class JWTAuthorisationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Pour chaque méthode envoyé par l'utilisateur cette méthode va dabord s'exécuter
				// filtre à mettre dans chaque microService
				
				response.addHeader("Access-Control-Allow-Origin", "*");// jautorise tous les domaines
				response.addHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,authorization");
				response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credential,authorization");
				if(request.getMethod().equals("OPTIONS")){
					response.setStatus(HttpServletResponse.SC_OK); // si une requet avec OPTION alors je reponds avec OKAY
				} else if(request.getRequestURI().equals("/login")) {
					filterChain.doFilter(request, response);
					return;
				}  else {
				
				String jwtToken= request.getHeader(SecurityParams.JWT_HEADER_NAME);// on recupere le JWT
				System.out.println("Token "+jwtToken);
				if(jwtToken==null || !jwtToken.startsWith(SecurityParams.HEADER_PREFIX)){
					filterChain.doFilter(request, response);
					return;
					}
				//DecodedJWT decodedJWT=JWT.decode(jwt.substring(SecurityParams.HEADER_PREFIX.length(), jwt.length()));
				JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
				String jwt=jwtToken.substring(SecurityParams.HEADER_PREFIX.length());
				System.out.println("jwt "+jwt);
				DecodedJWT decodedJWT=verifier.verify(jwt);
				String username=decodedJWT.getSubject();// recupérer userName
				List<String> roles= decodedJWT.getClaims().get("roles").asList(String.class);
				Collection<GrantedAuthority> authorities=new ArrayList<>();
				System.out.println("username "+username +" ");
				System.out.println("roles "+roles +" ");
				roles.forEach(rn->{
					authorities.add(new SimpleGrantedAuthority(rn));// jai les roles
				});
				// ensuite on demande à Spring d'authentifier l'utilisateur
				UsernamePasswordAuthenticationToken user=new UsernamePasswordAuthenticationToken(username,null, authorities);
				SecurityContextHolder.getContext().setAuthentication(user);
				filterChain.doFilter(request, response);
			}
		
		
	}

	

}
