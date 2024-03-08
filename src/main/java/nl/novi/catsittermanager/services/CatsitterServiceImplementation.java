package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.repositories.CatsitterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatsitterServiceImplementation implements CatsitterService {

    private final CatsitterRepository catsitterRepository;

    public CatsitterServiceImplementation(final CatsitterRepository catsitterRepository) {
        this.catsitterRepository = catsitterRepository;
    }

    @Override
    public List<CatsitterDto> getAllCatsitters() {
        return catsitterRepository.findAll()
                .stream()
                .map(CatsitterMapper::transferToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CatsitterDto getCatsitter(UUID idToFind) {
        return catsitterRepository.findById(idToFind)
                .map(CatsitterMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id."));
    }

    @Override
    public CatsitterDto createCatsitter(CatsitterInputDto catsitterInputDto) {
        Catsitter newCatsitter = new Catsitter();
        newCatsitter.setAbout(catsitterInputDto.about());
//        newCatsitter.setOrderList(catsitterInputDto.orderList());
//        newCatsitter.setCustomerList(catsitterInputDto.customerList());
        catsitterRepository.save(newCatsitter);
        return CatsitterMapper.transferToDto(newCatsitter);
    }

    @Override
    public CatsitterDto editCatsitter(UUID idToEdit, CatsitterInputDto catsitterInputDto) {
        Optional<Catsitter> optionalCatsitter = catsitterRepository.findById(idToEdit);

        if (optionalCatsitter.isPresent()) {
            Catsitter catsitter = optionalCatsitter.get();
            if (catsitterInputDto.about() != null) {
                catsitter.setAbout(catsitterInputDto.about());
            }
//            if (catsitterInputDto.orderList() != null) {
//                catsitter.setOrderList(catsitterInputDto.orderList());
//            }
//            if (catsitterInputDto.customerList() != null) {
//                catsitter.setCustomerList(catsitterInputDto.customerList());
//            }
            catsitterRepository.save(catsitter);
            return CatsitterMapper.transferToDto(catsitter);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id.");
        }
    }

    @Override
    public String deleteCatsitter(UUID idToDelete) {
        Optional<Catsitter> optionalCatsitter = catsitterRepository.findById(idToDelete);
        if (optionalCatsitter.isPresent()) {
            catsitterRepository.deleteById(idToDelete);
            return "Catsitter with id " + idToDelete + " removed from database";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
        }
    }
}
// Toevoegen: assignOrderToCatSitter, assignCustomerToCatSitter



