package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.models.Cat;

public class CatMapper {

    public static CatDto transferToDto(Cat cat) {
        return new CatDto(cat.getId(),
                          cat.getName(),
                          cat.getDateOfBirth(),
                          cat.getBreed(),
                          cat.getGeneralInfo(),
                          cat.getSpayedOrNeutered(),
                          cat.getVaccinated(),
                          cat.getVeterinarianName(),
                          cat.getPhoneVet(),
                          cat.getMedicationName(),
                          cat.getMedicationDose(),
                          cat.getOwnerName()
        );
    }

    public static Cat transferFromDto(CatInputDto catInputDto) {
        return new Cat(catInputDto.id(), // In een latere fase deze hier niet meegeven, maar automatisch via database
                       catInputDto.name(),
                       catInputDto.dateOfBirth(),
                       catInputDto.breed(),
                       catInputDto.generalInfo(),
                       catInputDto.spayedOrNeutered(),
                       catInputDto.vaccinated(),
                       catInputDto.veterinarianName(),
                       catInputDto.phoneVet(),
                       catInputDto.phoneVet(),
                       catInputDto.medicationDose(),
                       catInputDto.ownerName()
        );
    }
//    public CatDto transferToDto(Cat cat) {
//
//        CatDto catDto = new CatDto();
//
//        catDto.id = cat.getId();
//        catDto.name = cat.getName();
//        catDto.dateOfBirth = cat.getDateOfBirth();
//        catDto.breed = cat.getBreed();
//        catDto.generalInfo = cat.getGeneralInfo();
//        catDto.veterinarianName = cat.getVeterinarianName();
//        catDto.phoneVet = cat.getPhoneVet();
//        catDto.medicationName = cat.getMedicationName();
//        catDto.medicationDose = cat.getMedicationDose();
//        catDto.ownerName = cat.getOwnerName();
//
//        return catDto;
//    }
//
//    public Cat transferToCat(CatInputDto catDto) {
//
//        Cat cat = new Cat();
//
//        cat.setName(catDto.name);
//        cat.setDateOfBirth(catDto.dateOfBirth);
//        cat.setBreed(catDto.breed);
//        cat.setGeneralInfo(catDto.generalInfo);
//        cat.setVeterinarianName(catDto.veterinarianName);
//        cat.setPhoneVet(catDto.phoneVet);
//        cat.setMedicationName(catDto.medicationName);
//        cat.setMedicationDose(catDto.medicationDose);
//        cat.setOwnerName(catDto.ownerName);
//
//        return cat;
//    }
}

