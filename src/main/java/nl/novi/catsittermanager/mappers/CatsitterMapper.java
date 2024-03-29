package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.models.Catsitter;

public class CatsitterMapper {

    public static CatsitterDto transferToDto(Catsitter catsitter) {
        return new CatsitterDto(catsitter.getUsername(),
                                catsitter.getPassword(),
                                catsitter.getName(),
                                catsitter.getAddress(),
                                catsitter.getEmail(),
                                catsitter.getAbout(),
                                catsitter.getOrders().stream().map(OrderMapper::transferToDto).toList()
        );
    }

    public static Catsitter transferFromDto(CatsitterInputDto catsitterInputDto) {
        return Catsitter.CatsitterBuilder()
                .username(catsitterInputDto.username())
                .password(catsitterInputDto.password())
                .name(catsitterInputDto.name())
                .address(catsitterInputDto.address())
                .email(catsitterInputDto.email())
                .about(catsitterInputDto.about())
                .orders(catsitterInputDto.orders())
                .build();
    }
}

