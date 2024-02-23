package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;
import nl.novi.catsittermanager.models.CatSitter;

public class CatSitterMapper {

    public static CatSitterDto transferToDto(CatSitter catSitter) {
        return new CatSitterDto(catSitter.getId(), //Dummy, alleen voor testen met Postman, komt normaalgesproken uit superklasse User
                                catSitter.getAbout(),
                                catSitter.getOrderList(),
                                catSitter.getCustomerList()
        );
    }

    public static CatSitter transferFromDto(CatSitterInputDto catSitterInputDto) {
        return new CatSitter(catSitterInputDto.id(), // In een latere fase deze hier niet meegeven, maar automatisch via database
                             catSitterInputDto.about(),
                             catSitterInputDto.orderList(),
                             catSitterInputDto.customerList()
        );
    }
//    public CatSitterDto transferToDto(CatSitter catSitter) {
//
//        CatSitterDto catSitterDto = new CatSitterDto();
//
//        // catSitterDto.id = catSitter.getId(); // deze komt uit superklasse User
//        catSitterDto.about = catSitter.getAbout();
//        catSitterDto.customers = catSitter.getCustomerList();
//        catSitterDto.orderList = catSitter.getOrderList();
//
//        return catSitterDto;
//    }
//
//    public CatSitter transferToCatSitter(CatSitterInputDto catSitterDto) {
//
//        CatSitter catSitter = new CatSitter();
//
//        catSitter.setAbout(catSitterDto.about);
//        catSitter.setCustomers(catSitterDto.customers);
//        catSitter.setOrderList(catSitterDto.orderList);
//
//        return catSitter;
//    }
}
