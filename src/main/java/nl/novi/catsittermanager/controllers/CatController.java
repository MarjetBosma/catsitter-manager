package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.services.CatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CatController {

    private final CatService catService;

    @GetMapping("/cats")
    public ResponseEntity<List<CatResponse>> getAllCats() {
        List<CatResponse> catResponseList = catService.getAllCats().stream()
                .map(CatMapper::CatToCatResponse)
                .toList();
        return ResponseEntity.ok(catResponseList);
    }

    @GetMapping("/cat/{id}")
    public ResponseEntity<CatResponse> getCat(@PathVariable("id") final UUID idToFind) {
        Cat cat = catService.getCat(idToFind);
        return ResponseEntity.ok(CatMapper.CatToCatResponse(cat));
    }
  
    @PostMapping("/cat")
    public ResponseEntity<CatResponse> createCat(@Valid @RequestBody final CatRequest catRequest) throws URISyntaxException {
        Cat cat = catService.createCat(CatMapper.CatRequestToCat(catRequest), catRequest.ownerUsername());
        return ResponseEntity.created(new URI("/cat/" + cat.getId())).body((CatMapper.CatToCatResponse(cat)));
    }

    @PutMapping("/cat/{id}")
    public ResponseEntity<CatResponse> editCat(@PathVariable("id") final UUID idToEdit, @Valid @RequestBody final CatRequest catRequest) {
        Cat cat = catService.editCat(idToEdit, CatMapper.CatRequestToCat(catRequest), catRequest.ownerUsername());
        return ResponseEntity.ok().body(CatMapper.CatToCatResponse(cat));
    }

    @DeleteMapping("/cat/{id}")
    public ResponseEntity<Object> deleteCat(@PathVariable("id") final UUID idToDelete) {
        catService.deleteCat(idToDelete);
        return ResponseEntity.ok().body("Cat with id " + idToDelete + " removed from database.");
    }
}
