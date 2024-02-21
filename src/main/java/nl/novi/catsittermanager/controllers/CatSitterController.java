package nl.novi.catsittermanager.controllers;

import jakarta.validation.Valid;
import nl.novi.catsittermanager.dtos.IdInputDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatSitterServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/catsitters")
public class CatSitterController {

    private final CatSitterServiceImplementation catSitterService;

    public CatSitterController(CatSitterServiceImplementation catSitterService) {
        this.catSitterService = catSitterService;
    }

    @GetMapping
    public ResponseEntity<List<CatSitterDto>> getAllCatSitters() {
        return ResponseEntity.ok(catSitterService.getAllCatsitters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatSitterDto> getCatSitter(@PathVariable Long id) {
        if (id > 0) {
            CatSitterDto catSitterDto = CatSitterServiceImplementation.getCatSitterId();
            return ResponseEntity.ok(catSitterDto);
        } else {
            throw new RecordNotFoundException("No cat sitter found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<CatSitterDto> addCat(@RequestBody CatSitterInputDto catSitterInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatSitterDto savedCatSitter;
            savedCatSitter = catSitterService.createCatSitter(catSitterInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCatSitter.id).toUriString());
            return ResponseEntity.created(uri).body(savedCatSitter);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatSitterDto> updateCatSitter(@PathVariable long id, @RequestBody CatSitterInputDto catSitter) {
        CatSitterDto changeCatSitterId = catSitterService.updateCatSitter(id, catSitter);

        return ResponseEntity.ok().body(changeCatSitterId);
    }

    @PutMapping("/{id}/customer")
    public ResponseEntity<Object> assignCustomerToCatSitter(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        catSitterService.assignCustomerToCatSitter(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/order")
    public ResponseEntity<Object> assignOrderToCatSitter(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
        catSitterService.assignOrderToCatSitter(id, input.id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCatSitter(@PathVariable("id") Long id) {
        catSitterService.deleteCatSitter(id);
        return ResponseEntity.noContent().build();
    }
}
