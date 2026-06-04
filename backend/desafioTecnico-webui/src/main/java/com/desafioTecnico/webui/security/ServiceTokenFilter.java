package com.desafioTecnico.webui.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// Valida que toda requisição à API vem do BFF via service token JWT
@Component
public class ServiceTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ServiceTokenFilter.class);
    private static final String HEADER = "X-Service-Token";

    private final SecretKey secretKey;

    public ServiceTokenFilter(
            @Value("${service.token.secret:changeme-service-secret-32-chars-min}") String secret
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // Actuator e OPTIONS passam sem validação
        String uri = request.getRequestURI();
        if (uri.startsWith("/actuator") || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HEADER);
        if (token == null || token.isBlank()) {
            log.warn("[BACKEND] Requisição sem service token para {}", uri);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Service token obrigatório");
            return;
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Garante que o token foi emitido pelo BFF
            if (!"bff-service".equals(claims.getSubject())) {
                log.warn("[BACKEND] Service token com subject inválido: {}", claims.getSubject());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Service token inválido");
                return;
            }

            chain.doFilter(request, response);

        } catch (JwtException ex) {
            log.warn("[BACKEND] Service token inválido ou expirado: {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Service token inválido ou expirado");
        }
    }
}
