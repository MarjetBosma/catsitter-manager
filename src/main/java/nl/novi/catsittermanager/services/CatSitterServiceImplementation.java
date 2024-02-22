package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;
import nl.novi.catsittermanager.mappers.CatSitterMapper;
import nl.novi.catsittermanager.models.CatSitter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static nl.novi.catsittermanager.mappers.CatMapper.transferToDto;

@Service
public class CatSitterServiceImplementation implements CatSitterService {

//    private final CatSitterRepository catSitterRepos;
//
//    private final CustomerServiceImplementation customerService;
//
//    private final OrderServiceImplementation orderService;

    private List<CatSitter> catSitters = new ArrayList<>(); // voor testen zonder database

//    public CatSitterServiceImplementation(CustomerServiceImplementation customerService, OrderServiceImplementation orderService) {
//        this.catRepos = catRepos;
//        this.customerService = customerService;
//        this.orderService = orderService;
//    }

    public CatSitterServiceImplementation() { // Alleen voor testen zonder database
        catSitters.add(new CatSitter(1L,"Kattenoppas introduceert zichzelf", "Lijst met orders", "Lijst met klanten"));
        catSitters.add(new CatSitter(2L,"Nog een introductieverhaaltje", "Lijst met orders", "Lijst met klanten"));
    }

    @Override
    public List<CatSitterDto> getAllCatSitters() {
        //        List<CatSitter> catSitterList = catSitterRepos.findAll(); // Deze is voor als de database gevuld is
        List<CatSitterDto> catSitterDtoList = new ArrayList<>();

        for (CatSitter catSitter : catSitters) {
            CatSitterDto catSitterDto = CatSitterMapper.transferToDto(catSitter);
            catSitterDtoList.add(catSitterDto);
        }
        return catSitterDtoList;
    }

    @Override
    public CatSitterDto getCatSitter(long idToFind) {
        for (CatSitter catSitter : catSitters) {
            if (catSitter.getId() == idToFind) {
                return CatSitterMapper.transferToDto(catSitter);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id.");
    }

    @Override
    public CatSitterDto createCatSitter(CatSitterInputDto catSitterInputDto) {
        CatSitter newCatSitter = new CatSitter(
                catSitters.get(catSitters.size() - 1).getId(),
                catSitterInputDto.about(),
                catSitterInputDto.orderList(),
                catSitterInputDto.customerList()
        );
        catSitters.add(newCatSitter);
        return CatSitterMapper.transferToDto(newCatSitter);
    }

    @Override
    public CatSitterDto editCatSitter(long idToEdit, CatSitterInputDto catSitterInputDto) {
        for (CatSitter catSitter : catSitters) {
            if (catSitter.getId() == idToEdit) {
                if (catSitterInputDto.about() != null) {
                    catSitter.setAbout(catSitterInputDto.about());
                }
                if (catSitterInputDto.orderList() != null) {
                    catSitter.setOrderList(catSitterInputDto.orderList());
                }
                if (catSitterInputDto.customerList() != null) {
                    catSitter.setCustomerList(catSitterInputDto.customerList());
                }
                return CatSitterMapper.transferToDto(catSitter);
            }

        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id.");
    }
    @Override
    public void deleteCatSitter(long idToRemove) {
            for (CatSitter catSitter : catSitters) {
                if (catSitter.getId() == idToRemove) {
                    catSitters.remove(catSitter);
                    return;
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
        }
}



