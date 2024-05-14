package com.techsymphony.vytien_springboot325_restfulapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cashcards")
class CashCardController {
    private final CashCardRepository cashCardRepository;
    private final CashCardModelAssembler assembler;

    public CashCardController(CashCardRepository cashCardRepository, CashCardModelAssembler assembler) {
        this.cashCardRepository = cashCardRepository;
        this.assembler = assembler;
    }

    private CashCard findCashCard(Long requestedId, Principal principal) {
        return cashCardRepository.findByIdAndOwner(requestedId, principal.getName())
                .orElseThrow(() -> new CashCardNotFoundException(requestedId));
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<EntityModel<CashCard>> findById(@PathVariable Long requestedId, Principal principal) {
        CashCard cashCard = findCashCard(requestedId, principal);

        assembler.setPrincipal(principal);
        return ResponseEntity.ok(assembler.toModel(cashCard));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CashCard>>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));

        assembler.setPrincipal(principal);

        // Convert page content to List using map and toList.
        List<EntityModel<CashCard>> cashcards = page.getContent().stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(cashcards,
                linkTo(methodOn(CashCardController.class)
                        .findAll(pageable, principal))
                        .withSelfRel()));
    }

    // @GetMapping("/{requestedId}")
    // private ResponseEntity<CashCard> findById(@PathVariable Long requestedId,
    // Principal principal) {
    // CashCard cashCard = findCashCard(requestedId, principal);
    // if (cashCard != null) {
    // return ResponseEntity.ok(cashCard);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }
    //
    // @GetMapping
    // private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal
    // principal) {
    // Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
    // PageRequest.of(
    // pageable.getPageNumber(),
    // pageable.getPageSize(),
    // pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
    // return ResponseEntity.ok(page.getContent());
    // }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate,
            Principal principal) {
        CashCard cashCard = findCashCard(requestedId, principal);
        if (cashCard != null) {
            CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
            cashCardRepository.save(updatedCashCard);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb,
            Principal principal) {
        CashCard savedCashCard = cashCardRepository
                .save(new CashCard(null, newCashCardRequest.amount(), principal.getName()));

        // Get CashCard URI from CashCard ID using EntityModel
        assembler.setPrincipal(principal);
        EntityModel<CashCard> entityModel = assembler.toModel(cashCardRepository.findById(savedCashCard.id()).get());
        URI locationOfNewCashCard2 = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();

        return ResponseEntity.created(locationOfNewCashCard2).build();
    }

    @DeleteMapping("{id}")
    private ResponseEntity<Void> deleteCashCard(
            @PathVariable Long id,
            Principal principal // Add Principal to the parameter list
    ) {
        // Add the following 3 lines:
        if (cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
            cashCardRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}