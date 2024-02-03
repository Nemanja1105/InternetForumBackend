package org.unibl.etf.services;

import org.springframework.validation.BindingResult;

public interface WAFService {
    void checkRequest(BindingResult result);
}
