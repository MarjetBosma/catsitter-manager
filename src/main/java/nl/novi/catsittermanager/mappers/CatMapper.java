package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CatMapper {

    public static CatResponse CatToCatResponse(Cat cat) {
        return new CatResponse(
                cat.getId(),
                cat.getName(),
                cat.getDateOfBirth().toString(),
                cat.getGender(),
                cat.getBreed(),
                cat.getGeneralInfo(),
                cat.getSpayedOrNeutered(),
                cat.getVaccinated(),
                cat.getVeterinarianName(),
                cat.getPhoneVet(),
                cat.getMedicationName(),
                cat.getMedicationDose(),
                cat.getOwner().getUsername(),
                cat.getImage()
        );
    }

    public static Cat CatRequestToCat(CatRequest catRequest) {

        Customer owner = new Customer();
        owner.setUsername(catRequest.ownerUsername());

        return Cat.builder()
                .name(catRequest.name())
                .dateOfBirth(LocalDate.parse(catRequest.dateOfBirth()))
                .gender(catRequest.gender())
                .breed(catRequest.breed())
                .generalInfo(catRequest.generalInfo())
                .spayedOrNeutered(catRequest.spayedOrNeutered())
                .vaccinated(catRequest.vaccinated())
                .veterinarianName(catRequest.veterinarianName())
                .phoneVet(catRequest.phoneVet())
                .medicationName(catRequest.medicationName())
                .medicationDose(catRequest.medicationDose())
                .owner(owner)
                .image(catRequest.image())
                .build();
    }
}

