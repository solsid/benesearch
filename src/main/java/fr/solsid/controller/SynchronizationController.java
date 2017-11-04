package fr.solsid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/synchronization")
public class SynchronizationController {

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public void start() {

    }
}
