package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.dtos.order.OrderDto;
import nl.novi.catsittermanager.models.Catsitter;

import java.util.List;
import java.util.UUID;

public interface CatsitterService {
    List<CatsitterDto> getAllCatsitters();

    CatsitterDto getCatsitter(UUID idToFind);

    CatsitterDto createCatsitter(CatsitterInputDto catSitterInputDto);

    CatsitterDto editCatsitter(UUID idToEdit, CatsitterInputDto catSitterInputDto);

    UUID deleteCatsitter(UUID idToDelete);

}
