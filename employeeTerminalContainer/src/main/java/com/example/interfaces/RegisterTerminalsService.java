package com.example.interfaces;

import com.example.dto.RegisterTerminalDto;
import com.example.model.Department;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface RegisterTerminalsService {
    Set<Department> registerTerminals(String authorizationHeader, List<RegisterTerminalDto> registerTerminalDtoList) throws JsonProcessingException;
}