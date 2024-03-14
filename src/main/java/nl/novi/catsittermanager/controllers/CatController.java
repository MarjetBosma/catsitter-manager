package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/cat")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping
    public ResponseEntity<List<CatDto>> getAllCats() {
        return ResponseEntity.ok(catService.getAllCats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCat(@PathVariable("id") final UUID idToFind) {
            CatDto catDto = catService.getCat(idToFind);
            return ResponseEntity.ok(catDto);
    }

    @PostMapping
    public ResponseEntity<CatDto> createCat(@RequestBody final CatInputDto catInputDto, final BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatDto savedCat = catService.createCat(catInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCat).toUriString());
            return ResponseEntity.created(uri).body(savedCat);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatDto> editCat(@PathVariable("id") final UUID idToEdit, @RequestBody final CatInputDto cat) {
        CatDto editedCat = catService.editCat(idToEdit, cat);
        return ResponseEntity.ok().body(editedCat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCat(@PathVariable("id") final UUID idToDelete) {
        catService.deleteCat(idToDelete);
        return ResponseEntity.ok().body("Cat with id " + idToDelete +  " removed from database");
    }

//    @PutMapping("/{id}/customer")
//    public ResponseEntity<Object> assignCustomerToCat(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        catService.assignCustomerToCat(id, input.id);
//        return ResponseEntity.noContent().build();
//    }

}
