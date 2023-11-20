package br.com.financeiro.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenValidationGatewayFilter implements GatewayFilter {

    private final TokenValidationConfig config;
    private final JwtUtil jwtUtil;

    public TokenValidationGatewayFilter(TokenValidationConfig config, JwtUtil jwtUtil) {
        this.config = config;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String jwt = getTokenHeader(exchange);

        if (jwt != null) {
            return handleValidToken(jwt, exchange, chain);
        } else {
            return handleNullToken(exchange, chain);
        }
    }

    private Mono<Void> handleValidToken(String jwt, ServerWebExchange exchange, GatewayFilterChain chain) {
        if (jwtUtil.validateJwt(jwt)) {
            return chain.filter(exchange);
        } else {
            return setUnauthorizedStatus(exchange);
        }
    }

    private Mono<Void> handleNullToken(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        if ("/api/security/authenticate".equals(requestPath)) {
            return chain.filter(exchange);
        } else {
            return setUnauthorizedStatus(exchange);
        }
    }

    private String getTokenHeader(ServerWebExchange exchange) {
        String headerAuth = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private Mono<Void> setUnauthorizedStatus(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

}