package com.charityconnector.service;

import com.charityconnector.entity.UKRecordCharity;

public interface UKRecordCharityService {
    UKRecordCharity findByEmail(String email);
}
