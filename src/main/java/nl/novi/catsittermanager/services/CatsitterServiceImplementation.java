package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.repositories.CatsitterRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatsitterServiceImplementation implements CatsitterService {

    private final CatsitterRepository catsitterRepos;

//    private final OrderServiceImplementation orderService;

    public CatsitterServiceImplementation(CatsitterRepository catsitterRepos
//            , CustomerServiceImplementation customerService, OrderServiceImplementation orderService
    ) {
        this.catsitterRepos = catsitterRepos;
//        this.customerService = customerService;
//        this.orderService = orderService;
    }

    @Override
    public List<CatsitterDto> getAllCatsitters() {
        List<CatsitterDto> catsitterDtoList = new ArrayList<>();
        List<Catsitter> catSitterList = catsitterRepos.findAll();
        for (Catsitter catsitter : catSitterList) {
            CatsitterDto catsitterDto = CatsitterMapper.transferToDto(catsitter);
            catsitterDtoList.add(catsitterDto);
        }
        return catsitterDtoList;
    }

    @Override
    public CatsitterDto getCatsitter(long idToFind) {
        Optional<Catsitter> catsitterOptional = catsitterRepos.findById(idToFind);
        if (catsitterOptional.isPresent()) {
                return CatsitterMapper.transferToDto(catsitterOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id.");
        }
    }

    @Override
    public CatsitterDto createCatsitter(CatsitterInputDto catsitterInputDto) {
        Catsitter newCatsitter = new Catsitter();
        newCatsitter.setAbout(catsitterInputDto.about());
        newCatsitter.setOrderList(catsitterInputDto.orderList());
        newCatsitter.setCustomerList(catsitterInputDto.customerList());
        catsitterRepos.save(newCatsitter);
        return CatsitterMapper.transferToDto(newCatsitter);
    }

    @Override
    public CatsitterDto editCatsitter(long idToEdit, CatsitterInputDto catsitterInputDto) {
        Optional<Catsitter> optionalCatsitter = catsitterRepos.findById(idToEdit);

        if (optionalCatsitter.isPresent()) {
            Catsitter catsitter = optionalCatsitter.get();
            if (catsitterInputDto.about() != null) {
                catsitter.setAbout(catsitterInputDto.about());
            }
            if (catsitterInputDto.orderList() != null) {
                catsitter.setOrderList(catsitterInputDto.orderList());
            }
            if (catsitterInputDto.customerList() != null) {
                catsitter.setCustomerList(catsitterInputDto.customerList());
            }
            catsitterRepos.save(catsitter);
            return CatsitterMapper.transferToDto(catsitter);
        } else {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id.");
        }
    }

    @Override
    public String deleteCatsitter(long idToDelete) {
        Optional<Catsitter> optionalCatsitter = catsitterRepos.findById(idToDelete);
        if (optionalCatsitter.isPresent()) {
            catsitterRepos.deleteById(idToDelete);
            return "Catsitter with id " + idToDelete + " removed from database";
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
        }
    }
}
// Toevoegen: assignOrderToCatSitter, assignCustomerToCatSitter


