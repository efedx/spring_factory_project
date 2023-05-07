package com.example.project.controllers;

import com.example.project.dto.registerDefectDto;
import com.example.project.services.RegisterDefectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController(value = "defectLog")
@RequiredArgsConstructor
public class RegisterDefectsController {
    @Autowired
    RegisterDefectsService registerDefectsService;

    @PostMapping("registerDefects")
    public String logDefects(@RequestPart("registerDefectDto") List<registerDefectDto> registerDefectDtoList, @RequestPart("defectImage") MultipartFile defectImage) throws Exception {


        byte[] defectImageByte;

        try {
            defectImageByte = defectImage.getBytes();
        } catch (IOException e) {
            // handle the exception, e.g. log an error message or return an error response
            return "error";
        }

        return registerDefectsService.logDefects(registerDefectDtoList, defectImageByte);
    }
}