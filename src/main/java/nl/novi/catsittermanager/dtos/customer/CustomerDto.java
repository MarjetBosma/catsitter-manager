package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CustomerDto (

        Long id,

        int numberOfCats,

//    Order orderList = new ArrayList<>(),

        String orderList, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Cat catListByName = new ArrayList<>(),

        String catListByName, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    CatSitter catsitterList = new ArrayList<>()

        String catsitterList //Dummy, alleen voor los testen van de klasse in Postman zonder database
) {}