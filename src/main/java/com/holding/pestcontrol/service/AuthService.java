package com.holding.pestcontrol.service;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.entity.Role;
import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.UserRepository;
import com.holding.pestcontrol.util.EmailUtil;
import com.holding.pestcontrol.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ValidationService validationService;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    public ReqResUser register(ReqResUser registration){
        validationService.validate(registration);

        ReqResUser response = new ReqResUser();

        if (userRepository.existsByEmail(registration.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Registered");
        }

        String otp = otpUtil.generatorOtp();
        try{
            emailUtil.sendOtpEmail(registration.getEmail(), otp);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp please try again");
        }

        try {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setEmail(registration.getEmail());
            user.setPassword(passwordEncoder.encode(registration.getPassword()));
            user.setRole(Role.valueOf(registration.getRole()));
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            User userResult = userRepository.save(user);
            response.setUser(userResult);
            response.setMessage("User Success Successfully");
            response.setStatusCode(200);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqResUser login(ReqResUser login){
        ReqResUser response = new ReqResUser();
        var user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (!BCrypt.checkpw(login.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password wrong !!!");
        }else if (!user.isActive()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you have not been verified");
        } else if (!user.isStatus()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your account is not active");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hours");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(String.valueOf(user));
        }

        return response;
    }

    public ReqResUser refreshToken(ReqResUser refreshToken){
        ReqResUser response = new ReqResUser();
        String username = jwtUtils.extractUsername(refreshToken.getToken());
        User user = userRepository.findByEmail(username).orElseThrow();
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

    public String verifikasiEmail(RequestVerifikasiEmail requestVerifikasiEmail){
        User user = userRepository.findByEmail(requestVerifikasiEmail.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (user.isActive()){
            return "Your account has been verified, please log in";
        }else if (user.getOtp().equals(requestVerifikasiEmail.getOtp()) && Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (60)){
            user.setActive(true);
            userRepository.save(user);
            return "you have been verified, please log in";
        }else if (!user.getOtp().equals(requestVerifikasiEmail.getOtp())) {
            return "OTP salah";
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please send verification again because the OTP has expired");
    }

    public String regenerateOtp(RequestGenerateOtp request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        String otp = otpUtil.generatorOtp();
        try{
            emailUtil.sendOtpEmail(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error, please try again");
        }

        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        return "OTP has been sent to " + request.getEmail() + ", Please verify within 1 minute";
    }

    public void forgotpassword(RequestForgotPassword request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        String otp = otpUtil.generatorOtp();
        try{
            emailUtil.setOtpForgotPassword(request.getEmail(), otp);
        }catch (MessagingException exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }

        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
    }

    public String setPassword(RequestSetPassword request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (user.getOtp().equals(request.getOtp()) && Duration.between(user.getOtpGeneratedTime(),LocalDateTime.now()).getSeconds() < (60)){
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return "Set new password success, please login again";
        }else if (!user.getOtp().equals(request.getOtp())){
            return "wrong otp";
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please regenerate otp because otp expired and try again");
    }

}
