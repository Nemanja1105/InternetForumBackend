package org.unibl.etf.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.entities.SiemEntity;
import org.unibl.etf.repositories.SiemRepository;
import org.unibl.etf.services.SiemService;

import java.util.Date;

@Service
public class SiemServiceImpl implements SiemService {


    private final SiemRepository siemRepository;

    public SiemServiceImpl( SiemRepository siemRepository) {

        this.siemRepository = siemRepository;
    }


    @Override
    public void log(String message) {
        SiemEntity siemEntity=new SiemEntity();
        siemEntity.setId(null);
        siemEntity.setDescription(message);
        siemEntity.setCreatedAt(new Date());
        this.siemRepository.saveAndFlush(siemEntity);
    }
}
