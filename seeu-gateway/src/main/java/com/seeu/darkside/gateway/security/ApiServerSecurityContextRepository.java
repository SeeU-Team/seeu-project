package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.seeu.darkside.gateway.security.TokenAuthenticationUtil.TOKEN_PREFIX;

@Component
public class ApiServerSecurityContextRepository implements ServerSecurityContextRepository {

	@Override
	public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
		return null;
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
		final String token = serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		Authentication authentication = ApiAuthenticationToken.getDeniedAuthentication();

		if (null != token) {
			try {
				// parse the token.
				final Claims claims = Jwts.parser()
						.setSigningKey(TokenAuthenticationUtil.getSecretKey())
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody();

				final String userId = claims.getSubject();
//				final User user = claims.get("user", User.class);

				authentication = new ApiAuthenticationToken(Long.parseLong(userId), Collections.singletonList(new SimpleGrantedAuthority("USER")));

				// TODO: Renew expiration time of token ???
			} catch (Exception e) {
				e.printStackTrace();
				authentication = ApiAuthenticationToken.getDeniedAuthentication();
			}
		}

		return Mono.just(new SecurityContextImpl(authentication));
	}
}
