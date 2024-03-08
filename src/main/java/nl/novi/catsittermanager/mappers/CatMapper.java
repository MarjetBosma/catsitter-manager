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
}

