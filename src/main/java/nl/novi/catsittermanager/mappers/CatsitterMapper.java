package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterResponse;
import nl.novi.catsittermanager.models.Catsitter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class CatsitterMapper {

    public static CatsitterResponse CatsitterToCatsitterResponse(Catsitter catsitter) {

        return new CatsitterResponse(
                catsitter.getUsername(),
                catsitter.getName(),
                catsitter.getAddress(),
                catsitter.getEmail(),
                catsitter.getAbout(),
                catsitter.getOrders().stream().map(OrderMapper::OrderToOrderResponse).toList(),
                catsitter.getImage()
        );
    }

    public static Catsitter CatsitterRequestToCatsitter(CatsitterRequest catsitterRequest) {

        return Catsitter.CatsitterBuilder()
                .username(catsitterRequest.username())
                .password(catsitterRequest.password())
                .name(catsitterRequest.name())
                .address(catsitterRequest.address())
                .email(catsitterRequest.email())
                .about(catsitterRequest.about())
                .orders(new ArrayList<>())
                .image(catsitterRequest.image())
                .enabled(true)
                .build();
    }
}

