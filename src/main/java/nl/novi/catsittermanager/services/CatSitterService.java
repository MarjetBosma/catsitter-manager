package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;

import java.util.List;

public interface CatSitterService {
    List<CatSitterDto> getAllCatSitters();

    CatSitterDto getCatSitter(long idToFind);

    CatSitterDto createCatSitter(CatSitterInputDto catSitterInputDto);

    CatSitterDto editCatSitter(long idToEdit, CatSitterInputDto catSitterInputDto);

    void deleteCatSitter(long idToDelete);
}
