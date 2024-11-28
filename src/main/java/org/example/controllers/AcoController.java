package org.example.controllers;

import org.example.dto.InputData;
import org.example.dto.OutputData;
import org.example.services.AcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/aco")
public class AcoController {

    @Autowired
    private AcoService acoService;

    @PostMapping("/optimize")
    public ResponseEntity<?> optimizeRoute(@RequestBody InputData inputData) {
        try {
            OutputData outputData = acoService.executeACO(inputData);
            return ResponseEntity.ok(outputData);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}