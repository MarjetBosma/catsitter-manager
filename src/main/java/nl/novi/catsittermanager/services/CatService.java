package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CatService {

    private final CatRepository catRepos;

    private final CustomerRepository customerRepos;

    private final CustomerService customerService;


    public CatService(CatRepository catRepos, CustomerRepository customerRepos, CustomerService customerService) {
        this.catRepos = catRepos;
        this.customerRepos = customerRepos;
        this.customerService = customerService;
    }

//    public List<CatDto> getAllCats() {
//        List<Cat> catList = catRepos.findAll();
//        List<CatDto> catDtoList = new ArrayList<>();
//
//        for (Cat cat : catList) {
//            catDtoList.add(transferToDto(cat));
//        }
//        return catDtoList;
//    }
}


