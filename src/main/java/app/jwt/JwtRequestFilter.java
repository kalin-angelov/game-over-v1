package app.jwt;

import app.security.AuthenticationMetadata;
import app.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public JwtRequestFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try {

                email = jwtService.extractEmail(token);
            } catch (IllegalArgumentException exception) {
                logger.info("Illegal Argument while fetching the username !!");
                exception.printStackTrace();
            } catch (ExpiredJwtException exception) {
                logger.info("Given jwt token is expired !!");
                exception.printStackTrace();
            } catch (MalformedJwtException exception) {
                logger.info("Some changed has done in token !! Invalid Token");
                exception.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            logger.info("Invalid Header Value !! ");
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            AuthenticationMetadata metadata = (AuthenticationMetadata) userService.loadUserByUsername(email);

            if(jwtService.validateToken(token, metadata)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(metadata, null, metadata.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else  {
                logger.info("Validation fails !!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
