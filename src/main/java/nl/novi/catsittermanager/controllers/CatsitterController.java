package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatsitterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/catsitter")
public class CatsitterController {

    private final CatsitterService catsitterService;

    public CatsitterController(CatsitterService catsitterService) {
        this.catsitterService = catsitterService;
    }

    @GetMapping
    public ResponseEntity<List<CatsitterDto>> getAllCatSitters() {
        return ResponseEntity.ok(catsitterService.getAllCatsitters());
    }

    @GetMapping("/{username}")
    public ResponseEntity<CatsitterDto> getCatSitter(@PathVariable("username") final String username) {
        CatsitterDto catsitterDto = catsitterService.getCatsitter(username);
        return ResponseEntity.ok(catsitterDto);
    }

    @PostMapping
    public ResponseEntity<CatsitterDto> addCatSitter(@RequestBody final CatsitterInputDto catsitterInputDto, final BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatsitterDto savedCatsitter;
            savedCatsitter = catsitterService.createCatsitter(catsitterInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCatsitter).toUriString());
            return ResponseEntity.created(uri).body(savedCatsitter);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<CatsitterDto> editCatsitter(@PathVariable("username") final String username, @RequestBody final CatsitterInputDto catsitter) {
        CatsitterDto editedCatsitter = catsitterService.editCatsitter(username, catsitter);

        return ResponseEntity.ok().body(editedCatsitter);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteCatsitter(@PathVariable("username") final String username) {
        catsitterService.deleteCatsitter(username);
        return ResponseEntity.ok().body("Catsitter " + username + " removed from database");
    }

}
