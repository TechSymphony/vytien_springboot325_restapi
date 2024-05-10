package com.techsymphony.vytien_springboot325_restfulapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/cashcards")
public class CashCardController {

    @GetMapping(path = "/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {

        if (requestedId.equals(99L)) {
            CashCard cashcard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(cashcard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
