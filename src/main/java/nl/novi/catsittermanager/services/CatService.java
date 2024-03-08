package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;

import java.util.List;
import java.util.UUID;

public interface CatService {
    List<CatDto> getAllCats();

    CatDto getCat(UUID idToFind);

    CatDto createCat(CatInputDto catInputDto);

    CatDto editCat(UUID idToEdit, CatInputDto catInputDto);

    UUID deleteCat(UUID idToDelete);
}
