package com.example.api.controller;

import com.example.api.model.entity.Vehicle;
import com.example.api.service.VehicleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
public class VehicleController {

    @Resource
    private VehicleService vehicleService;

    @PostMapping("")
    public Vehicle save(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }

    @GetMapping("")
    public List<Vehicle> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roles = auth.getName();
        System.out.println(auth.getName());
        System.out.println(auth.getAuthorities());
        System.out.println(auth.getPrincipal());
        System.out.println(auth.getName());
        System.out.println(1);
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle findById(@PathVariable String id) {
        return vehicleService.findById(id);
    }

    @DeleteMapping("")
    public void delete(String id) {
        vehicleService.delete(id);
    }

}
