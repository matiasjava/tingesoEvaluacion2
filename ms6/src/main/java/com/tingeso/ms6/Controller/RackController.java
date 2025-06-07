package com.tingeso.ms6.Controller;

import com.tingeso.ms6.Service.RackService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/rack")
@CrossOrigin(origins = "*")
public class RackController {

    private final RackService rackService;

    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @GetMapping("/")
    public List<Map<String, String>> getAllReserves() {
        return rackService.getAllReservesFromMs5();
    }
}

