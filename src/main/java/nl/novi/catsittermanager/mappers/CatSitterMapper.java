package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;
import nl.novi.catsittermanager.models.CatSitter;

public class CatSitterMapper {

    public CatSitterDto transferToDto(CatSitter catSitter) {

        CatSitterDto catSitterDto = new CatSitterDto();

        // catSitterDto.id = catSitter.getId(); // deze komt uit superklasse User
        catSitterDto.about = catSitter.getAbout();
        catSitterDto.customers = catSitter.getCustomers();
        catSitterDto.orderList = catSitter.getOrderList();

        return catSitterDto;
    }

    public CatSitter transferToCatSitter(CatSitterInputDto catSitterDto) {

        CatSitter catSitter = new CatSitter();

        catSitter.setAbout(catSitterDto.about);
        catSitter.setCustomers(catSitterDto.customers);
        catSitter.setOrderList(catSitterDto.orderList);

        return catSitter;
    }
}
