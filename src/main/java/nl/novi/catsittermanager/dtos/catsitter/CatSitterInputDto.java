package nl.novi.catsittermanager.dtos.catsitter;

import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.models.Order;

import java.util.ArrayList;
import java.util.List;

public record CatSitterInputDto (

    Long id,  //Dummy, alleen voor testen in Postman zonder gebruik van superclass (User levert normaalgesproken de id), en moet sowieso niet in inputDto bij toekennning id door database
    String about,

//    Order orderList = new ArrayList<>();,
    String orderList, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Customer customerList = new ArrayList<>();
    String customerList //Dummy, alleen voor los testen van de klasse in Postman zonder database

) {}

