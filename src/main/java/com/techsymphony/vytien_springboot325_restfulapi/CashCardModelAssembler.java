package com.techsymphony.vytien_springboot325_restfulapi;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
class CashCardModelAssembler implements RepresentationModelAssembler<CashCard, EntityModel<CashCard>> {

    private Principal principal;

    void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    @Override
    public EntityModel<CashCard> toModel(CashCard cashCard) {

        return EntityModel.of(cashCard, // with principal
                linkTo(methodOn(CashCardController.class).findById(cashCard.id(), principal)).withSelfRel(),
                linkTo(methodOn(CashCardController.class).findAll(PageRequest.of(0, 10), principal))
                        .withRel("cashcards"));
    }
}
