package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.service.AuthService;
import com.holding.pestcontrol.service.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final JWTUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<ReqResUser> register(@RequestBody ReqResUser registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping(
        path = "/login"
    )
    public ResponseEntity<ReqResUser> login(@RequestBody ReqResUser loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ReqResUser> refresh(@RequestBody ReqResUser refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PutMapping("/verifikasi")
    public ResponseSucces<String> verifikasiEmail(@RequestBody RequestVerifikasiEmail request){
        String response = authService.verifikasiEmail(request);
        return ResponseSucces.<String>builder().data(response).build();
    }

    @PutMapping("/regenerate-otp")
    public ResponseSucces<String> regenerateOtp(@RequestBody RequestGenerateOtp request){
        String response = authService.regenerateOtp(request);
        return ResponseSucces.<String>builder().data(response).build();
    }

    @PutMapping("/forgot-password")
    public ResponseSucces<String> forgotPassword(@RequestBody RequestForgotPassword request){
        authService.forgotpassword(request);
        return ResponseSucces.<String>builder().data("Otp sudah terkirim, silahkan lakukan verifikasi otp untuk melakukan lupa password").build();
    }

    @PutMapping("/set-password")
    public ResponseSucces<String> setPassword(@RequestBody RequestSetPassword request){
        String response = authService.setPassword(request);
        return ResponseSucces.<String>builder().data(response).build();
    }

}
