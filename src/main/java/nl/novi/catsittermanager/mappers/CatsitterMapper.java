package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.models.Catsitter;

public class CatsitterMapper {

    public static CatsitterDto transferToDto(Catsitter catsitter) {
        return new CatsitterDto(catsitter.getId(),
                                catsitter.getAbout(),
                                catsitter.getOrder(),
                                catsitter.getCustomer()
        );
    }

    public static Catsitter transferFromDto(CatsitterInputDto catsitterInputDto) {
        return new Catsitter(
                             catsitterInputDto.about(),
                             catsitterInputDto.order(),
                             catsitterInputDto.customer()
        );
    }
}
