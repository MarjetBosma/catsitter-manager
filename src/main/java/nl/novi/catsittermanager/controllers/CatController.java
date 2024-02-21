package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.IdInputDto;
import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/cats")

public class CatController {

    private final CatServiceImplementation catService;

    public CatController(CatServiceImplementation catService) {
        this.catService = catService;
    }

    @GetMapping
    public ResponseEntity<List<CatDto>> getAllCats() {
        return ResponseEntity.ok(catService.getAllCats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCat(@PathVariable Long id) {
        if (id > 0) {
            CatDto catDto = CatServiceImplementation.getCatId();
            return ResponseEntity.ok(catDto);
        } else {
            throw new RecordNotFoundException("No cat found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<CatDto> addCat(@RequestBody CatInputDto catInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatDto savedCat;
            savedCat = catService.createCat(catInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCat.id).toUriString());
            return ResponseEntity.created(uri).body(savedCat);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatDto> updateCat(@PathVariable long id, @RequestBody CatInputDto cat) {
        CatDto changeCIModuleId = catService.updateCat(id, cat);

        return ResponseEntity.ok().body(changeCatId);
    }

    @PutMapping("/{id}/customer")
    public ResponseEntity<Object> assignCustomerToCat(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        catService.assignCustomerToCat(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCat(@PathVariable("id") Long id) {
        catService.deleteCat(id);
        return ResponseEntity.noContent().build();
    }
}
