package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RegisterUserDTO registerUserDTO){
         this.authService.registerClient(registerUserDTO);
    }

    @PostMapping("/check-details")
    public boolean checkDetails(@RequestBody CheckDetailsDTO checkDetailsDTO){
        return this.authService.checkDetails(checkDetailsDTO);
    }

    @PostMapping("/login")
    public Long loginFirstStep(@Valid @RequestBody LoginRequestDTO requestDTO){
        return this.authService.login(requestDTO);
    }

    @PostMapping("/finish-login")
    public UserDTO finishLogin(@Valid @RequestBody VerificationRequestDTO requestDTO){
        return this.authService.finishLogin(requestDTO);
    }
}
