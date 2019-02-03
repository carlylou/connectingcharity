package com.charityconnector.serviceImpl;

import com.charityconnector.dao.UKRecordCharityRepository;
import com.charityconnector.entity.UKRecordCharity;
import com.charityconnector.service.UKRecordCharityService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class UKRecordCharityServiceImpl implements UKRecordCharityService {

    @Resource
    UKRecordCharityRepository ukRecordCharityRepository;

    @Override
    public UKRecordCharity findByEmail(String email) {
        return ukRecordCharityRepository.findByEmail(email);
    }
}
