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

    public CatDto transferToDto(Cat cat) {

        CatDto catDto = new CatDto();

        catDto.id = cat.getId();
        catDto.name = cat.getName();
        catDto.dateOfBirth = cat.getDateOfBirth();
        catDto.breed = cat.getBreed();
        catDto.generalInfo = cat.getGeneralInfo();
        catDto.veterinarianName = cat.getVeterinarianName();
        catDto.phoneVet = cat.getPhoneVet();
        catDto.medicationName = cat.getMedicationName();
        catDto.medicationDose = cat.getMedicationDose();
        catDto.ownerName = cat.getOwnerName();

        return catDto;
    }

    public Cat transferToCat(CatInputDto catDto) {

        Cat cat = new Cat();

        cat.setName(catDto.name);
        cat.setDateOfBirth(catDto.dateOfBirth);
        cat.setBreed(catDto.breed);
        cat.setGeneralInfo(catDto.generalInfo);
        cat.setVeterinarianName(catDto.veterinarianName);
        cat.setPhoneVet(catDto.phoneVet);
        cat.setMedicationName(catDto.medicationName);
        cat.setMedicationDose(catDto.medicationDose);
        cat.setOwnerName(catDto.ownerName);

        return cat;
    }
}

