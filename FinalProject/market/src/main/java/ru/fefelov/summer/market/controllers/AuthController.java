package ru.fefelov.summer.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fefelov.summer.market.dto.JwtRequest;
import ru.fefelov.summer.market.dto.JwtResponse;
import ru.fefelov.summer.market.events.EventProducer;
import ru.fefelov.summer.market.exceptions.MarketError;
import ru.fefelov.summer.market.services.UserService;
import ru.fefelov.summer.market.configs.JwtTokenUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final EventProducer eventProducer;

    @PostMapping("/api/v1/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new MarketError("Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        new Thread(() -> eventProducer.sendAuthEvent(userDetails.getUsername())).start();
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
