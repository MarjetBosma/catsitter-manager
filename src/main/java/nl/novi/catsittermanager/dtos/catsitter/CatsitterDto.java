package nl.novi.catsittermanager.dtos.catsitter;

public record CatsitterDto(
        Long id,

        String about,

//    Order orderList = new ArrayList<>(),
        String orderList, //Dummy, alleen voor los testen van de klasse in Postman zonder database

//    Customer customerList = new ArrayList<>()
        String customerList //Dummy, alleen voor los testen van de klasse in Postman zonder database
) {}
