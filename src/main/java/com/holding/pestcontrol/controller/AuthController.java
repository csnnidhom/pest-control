package com.holding.pestcontrol.controller;

import com.holding.pestcontrol.dto.*;
import com.holding.pestcontrol.service.AuthService;
import com.holding.pestcontrol.service.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final JWTUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<ReqResRegister> register(@RequestBody ReqResRegister reqResRegister){
        return ResponseEntity.ok(authService.register(reqResRegister));
    }

    @PostMapping(
        path = "/login"
    )
    public ResponseEntity<ReqResLogin> login(@RequestBody ReqResLogin reqResLogin){
        return ResponseEntity.ok(authService.login(reqResLogin));
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<ReqResUser> refresh(@RequestBody ReqResUser refreshTokenRequest){
//        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
//    }

    @PutMapping("/verifikasi")
    public ResponseSucces<String> verifikasiEmail(@RequestBody RequestVerifikasiEmail request){
        String response = authService.verifikasiEmail(request);
        return ResponseSucces.<String>builder().data(response).build();
    }

    @PutMapping("/regenerate-otp")
    public RequestGenerateOtp regenerateOtp(@RequestBody RequestGenerateOtp request){
        return authService.regenerateOtp(request);
    }

    @PutMapping("/forgot-password")
    public ResponseSucces<String> forgotPassword(@RequestBody RequestForgotPassword request){
        authService.forgotpassword(request);
        return ResponseSucces.<String>builder().data("Otp sudah terkirim, silahkan lakukan verifikasi otp untuk melakukan lupa password").build();
    }

    @PutMapping("/set-password")
    public ResponseSucces<String> setPassword(@RequestBody ReqResSetPassword request){
        String response = authService.setPassword(request);
        return ResponseSucces.<String>builder().data(response).build();
    }

    @PatchMapping("/edit-password")
    public ReqResEditPassword editPassword(@RequestBody ReqResEditPassword reqResEditPassword){
        return authService.editPassword(reqResEditPassword);
    }

}
