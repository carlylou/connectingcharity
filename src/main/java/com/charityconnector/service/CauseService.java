package com.charityconnector.service;

import com.charityconnector.entity.Cause;

import java.util.List;

public interface CauseService {
    Cause findById(Long id);

    List<Cause> getAllCauses();

    List<String> getAllCausesName();

    Cause findByName(String name);
}
