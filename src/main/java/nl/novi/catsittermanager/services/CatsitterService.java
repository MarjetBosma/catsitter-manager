package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;

import java.util.List;

public interface CatsitterService {
    List<CatsitterDto> getAllCatsitters();

    CatsitterDto getCatsitter(long idToFind);

    CatsitterDto createCatsitter(CatsitterInputDto catSitterInputDto);

    CatsitterDto editCatsitter(long idToEdit, CatsitterInputDto catSitterInputDto);

    String deleteCatsitter(long idToDelete);
}
