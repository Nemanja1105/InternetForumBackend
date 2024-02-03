package org.unibl.etf.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.unibl.etf.exceptions.BadRequestException;
import org.unibl.etf.services.SiemService;
import org.unibl.etf.services.WAFService;

@Service
public class WAFServiceImpl implements WAFService {

    private final SiemService siemService;
    private final HttpServletRequest request;

    private final JwtBlackListServiceImpl jwtBlackListService;

    public WAFServiceImpl(SiemService siemService, HttpServletRequest request, JwtBlackListServiceImpl jwtBlackListService) {
        this.siemService = siemService;
        this.request = request;
        this.jwtBlackListService = jwtBlackListService;
    }



    @Override
    public void checkRequest(BindingResult result) {
        if(!result.hasErrors())
            return;
        StringBuilder builder=new StringBuilder();
        builder.append("Bad request from client:").append(request.getRemoteAddr()).append(":");
        for (FieldError error : result.getFieldErrors()) {
            builder.append(error.getDefaultMessage()).append("|");
        }
        String message=builder.toString();
        this.siemService.log(message);
        var token=this.getToken();
        if(token!=null)
            this.jwtBlackListService.blackList(token);
        throw new BadRequestException();
    }

    private String getToken(){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null || authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
