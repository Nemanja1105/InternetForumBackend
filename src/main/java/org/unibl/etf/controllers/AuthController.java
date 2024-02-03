package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.services.AuthService;
import org.unibl.etf.services.WAFService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final WAFService wafService;

    public AuthController(AuthService authService, WAFService wafService) {
        this.authService = authService;
        this.wafService = wafService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RegisterUserDTO registerUserDTO, BindingResult bindingResult){
         this.wafService.checkRequest(bindingResult);
         this.authService.registerClient(registerUserDTO);
    }

    @PostMapping("/check-details")
    public boolean checkDetails(@RequestBody CheckDetailsDTO checkDetailsDTO){
        return this.authService.checkDetails(checkDetailsDTO);
    }

    @PostMapping("/login")
    public Long loginFirstStep(@Valid @RequestBody LoginRequestDTO requestDTO,BindingResult bindingResult){
        this.wafService.checkRequest(bindingResult);
        return this.authService.login(requestDTO);
    }

    @PostMapping("/finish-login")
    public UserDTO finishLogin(@Valid @RequestBody VerificationRequestDTO requestDTO,BindingResult bindingResult){
        this.wafService.checkRequest(bindingResult);
        return this.authService.finishLogin(requestDTO);
    }

    @GetMapping("/login/github")
    public UserDTO loginWithGithub(@RequestParam String code)throws Exception{
       return this.authService.loginWithGithub(code);
    }
}
