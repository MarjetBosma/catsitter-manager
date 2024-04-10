package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import org.springframework.stereotype.Component;

@Component
public class CatMapper {

    public static CatResponse CatToCatResponse(Cat cat) {
        return new CatResponse(
                cat.getId(),
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

    public static Cat CatRequestToCat(CatRequest catRequest) {
        return Cat.builder()
                .name(catRequest.name())
                .dateOfBirth(catRequest.dateOfBirth())
                .gender(catRequest.gender())
                .breed(catRequest.breed())
                .generalInfo(catRequest.generalInfo())
                .spayedOrNeutered(catRequest.spayedOrNeutered())
                .vaccinated(catRequest.vaccinated())
                .veterinarianName(catRequest.veterinarianName())
                .phoneVet(catRequest.phoneVet())
                .medicationName(catRequest.medicationName())
                .medicationDose(catRequest.medicationDose())
                .build();
    }
}

