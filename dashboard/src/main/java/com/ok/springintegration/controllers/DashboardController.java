package com.ok.springintegration.controllers;

import com.ok.springintegration.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("status", dashboardService.getDashboardStatus());
        return "dashboard";
    }


    @RequestMapping(value = "/api")
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
