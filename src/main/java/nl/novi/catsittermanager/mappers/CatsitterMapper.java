package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.models.Catsitter;

public class CatsitterMapper {

    public static CatsitterDto transferToDto(Catsitter catsitter) {
        return new CatsitterDto(catsitter.getAbout(),
                                catsitter.getOrderList(),
                                catsitter.getCustomerList()
        );
    }

    public static Catsitter transferFromDto(CatsitterInputDto catsitterInputDto) {
        return new Catsitter(catsitterInputDto.about(),
                             catsitterInputDto.orderList(),
                             catsitterInputDto.customerList()
        );
    }
//    public CatsitterDto transferToDto(Catsitter catsitter) {
//
//        CatsitterDto catsitterDto = new CatsitterDto();
//
//        // catsitterDto.id = catsitter.getId(); // deze komt uit superklasse User
//        catsitterDto.about = catsitter.getAbout();
//        catsitterDto.customers = catsitter.getCustomerList();
//        catsitterDto.orderList = catsitter.getOrderList();
//
//        return catsitterDto;
//    }
//
//    public Catsitter transferToCatsitter(CatsitterInputDto catsitterDto) {
//
//        Catsitter catsitter = new Catsitter();
//
//        catsitter.setAbout(catsitterDto.about);
//        catsitter.setCustomers(catsitterDto.customers);
//        catsitter.setOrderList(catsitterDto.orderList);
//
//        return catsitter;
//    }
}
