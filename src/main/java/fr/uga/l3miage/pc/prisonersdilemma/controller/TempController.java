package fr.uga.l3miage.pc.prisonersdilemma.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    @GetMapping("/number")
    public int getNumber() {
        return 1;
    }
}
