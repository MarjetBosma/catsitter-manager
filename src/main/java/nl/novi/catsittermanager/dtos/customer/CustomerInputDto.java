package nl.novi.catsittermanager.dtos.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatSitter;
import nl.novi.catsittermanager.models.Order;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Validated
public record CustomerInputDto (

    @Positive
    int numberOfCats,

//    Order orderList = new ArrayList<>(),

    String orderList, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Cat catListByName = new ArrayList<>(),

    String catListByName, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Catsitter catsitterList = new ArrayList<>()

    String catsitterList //Dummy, alleen voor los testen van de klasse in Postman zonder database

) {}
