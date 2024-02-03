package org.unibl.etf.services;

import org.springframework.scheduling.annotation.Async;
import org.unibl.etf.models.dto.*;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AuthService {
    public void registerClient(RegisterUserDTO request);
    boolean checkDetails(CheckDetailsDTO checkDetailsDTO);

    Long login(LoginRequestDTO requestDTO);
    UserDTO finishLogin(VerificationRequestDTO requestDTO);
    UserDTO loginWithGithub(String code) throws URISyntaxException, IOException;
}
