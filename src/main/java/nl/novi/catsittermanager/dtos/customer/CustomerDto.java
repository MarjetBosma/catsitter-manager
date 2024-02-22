package nl.novi.catsittermanager.dtos.customer;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.CatSitter;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CustomerDto (

        Long id, //Dummy, alleen voor testen in Postman zonder gebruik van superclass (User levert normaalgesproken de id)

        int numberOfCats,

//    Order orderList = new ArrayList<>(),

        String orderList, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Cat catListByName = new ArrayList<>(),

        String catListByName, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    CatSitter catSitterList = new ArrayList<>()

        String catSitterList //Dummy, alleen voor los testen van de klasse in Postman zonder database
) {}