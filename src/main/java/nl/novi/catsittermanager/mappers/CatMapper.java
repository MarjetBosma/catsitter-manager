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
        return Cat.builder()
                .id(catInputDto.id())
                .name(catInputDto.name())
                .dateOfBirth(catInputDto.dateOfBirth())
                .breed(catInputDto.breed())
                .generalInfo(catInputDto.generalInfo())
                .spayedOrNeutered(catInputDto.spayedOrNeutered())
                .vaccinated(catInputDto.vaccinated())
                .veterinarianName(catInputDto.veterinarianName())
                .phoneVet(catInputDto.phoneVet())
                .medicationDose(catInputDto.medicationDose())
                .ownerName(catInputDto.ownerName())
                .build(); // In een latere fase deze hier niet meegeven, maar automatisch via database
    }
}

