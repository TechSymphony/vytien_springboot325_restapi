package com.techsymphony.vytien_springboot325_restfulapi;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    // @GetMapping()
    // private ResponseEntity<Iterable<CashCard>> findAll() {
    // return ResponseEntity.ok(cashCardRepository.findAll());
    // }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cashCardRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping(path = "/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {

        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);

        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createNewCashCard(@RequestBody CashCard newCashCardRequest,
            UriComponentsBuilder uriComponentsBuilder) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);

        URI locationOfNewCashCard = uriComponentsBuilder
                .path("/cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();

        return ResponseEntity.created(locationOfNewCashCard).build();
    }
}
