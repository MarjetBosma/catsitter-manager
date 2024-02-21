package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CatServiceImplementation implements CatService {

    private final CatRepository catRepos;

    private final CustomerRepository customerRepos;

    private final CustomerServiceImplementation customerService;


    public CatServiceImplementation(CatRepository catRepos, CustomerRepository customerRepos, CustomerServiceImplementation customerService) {
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


