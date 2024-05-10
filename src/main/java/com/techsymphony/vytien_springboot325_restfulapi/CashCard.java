package com.techsymphony.vytien_springboot325_restfulapi;

import org.springframework.data.annotation.Id;

public record CashCard(@Id Long id, Double amount) {

}
