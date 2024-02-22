package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;

import java.util.List;

public interface CatService {
    List<CatDto> getAllCats();

    CatDto getCat(long idToFind);

    CatDto createCat(CatInputDto catInputDto);

    CatDto editCat(long idToEdit, CatInputDto catInputDto);

    void deleteCat(long idToRemove);
}
