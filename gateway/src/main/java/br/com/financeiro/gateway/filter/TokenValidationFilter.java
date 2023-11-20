package br.com.financeiro.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class TokenValidationFilter extends AbstractGatewayFilterFactory<TokenValidationConfig> {

    @Autowired
    JwtUtil jwtUtil;

    public TokenValidationFilter() {
        super(TokenValidationConfig.class);
    }

    @Override
    public GatewayFilter apply(TokenValidationConfig config) {
        return new TokenValidationGatewayFilter(config, jwtUtil);
    }

}
