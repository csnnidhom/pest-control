package com.holding.pestcontrol.service;

import com.holding.pestcontrol.dto.ReqResUser;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ValidationService validationService;

    public ReqResUser register(ReqResUser registration){
        validationService.validate(registration);

        ReqResUser response = new ReqResUser();

        if (userRepository.existsByUsername(registration.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Registered");
        }

        try {
            User user = new User();
            user.setUsername(registration.getUsername());
            user.setPassword(passwordEncoder.encode(registration.getPassword()));
            user.setRole(registration.getRole());
            User userResult = userRepository.save(user);
            if (userResult != null && userResult.getId()>0) {
                response.setUser(userResult);
                response.setMessage("User Success Successfully");
                response.setStatusCode(200);
            }
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqResUser login(ReqResUser login){
        ReqResUser response = new ReqResUser();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            var user = userRepository.findByUsername(login.getUsername()).orElseThrow();
            System.out.println("User is " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hours");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }

    public ReqResUser refreshToken(ReqResUser refreshToken){
        ReqResUser response = new ReqResUser();
        String username = jwtUtils.extractUsername(refreshToken.getToken());
        User user = userRepository.findByUsername(username).orElseThrow();
        if (jwtUtils.isTokenValid(refreshToken.getToken(), user)){
            var jwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken.getToken());
            response.setExpirationTime("24Hours");
            response.setMessage("Successfully Refresh Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
