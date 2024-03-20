package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.models.Cat;

public class CatMapper {


    public static CatDto transferToDto(Cat cat) {
        return new CatDto(cat.getId(),
                          cat.getName(),
                          cat.getDateOfBirth(),
                          cat.getGender(),
                          cat.getBreed(),
                          cat.getGeneralInfo(),
                          cat.getSpayedOrNeutered(),
                          cat.getVaccinated(),
                          cat.getVeterinarianName(),
                          cat.getPhoneVet(),
                          cat.getMedicationName(),
                          cat.getMedicationDose(),
                          cat.getOwner().getUsername()
        );
    }

    public static Cat transferFromDto(CatInputDto catInputDto) {
        return Cat.builder()
                .name(catInputDto.name())
                .dateOfBirth(catInputDto.dateOfBirth())
                .gender(catInputDto.gender())
                .breed(catInputDto.breed())
                .generalInfo(catInputDto.generalInfo())
                .spayedOrNeutered(catInputDto.spayedOrNeutered())
                .vaccinated(catInputDto.vaccinated())
                .veterinarianName(catInputDto.veterinarianName())
                .phoneVet(catInputDto.phoneVet())
                .medicationName(catInputDto.medicationName())
                .medicationDose(catInputDto.medicationDose())
                .build();
    }
}

