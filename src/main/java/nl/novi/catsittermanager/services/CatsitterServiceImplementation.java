package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.repositories.CatsitterRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatsitterServiceImplementation implements CatsitterService {

    private final CatsitterRepository catsitterRepos;

    public CatsitterServiceImplementation(CatsitterRepository catsitterRepos) {
        this.catsitterRepos = catsitterRepos;
    }

    @Override
    public List<CatsitterDto> getAllCatsitters() {
        return catsitterRepos.findAll()
                .stream()
                .map(CatsitterMapper::transferToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CatsitterDto getCatsitter(UUID idToFind) {
        return catsitterRepos.findById(idToFind)
                .map(CatsitterMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id."));
    }

    @Override
    public CatsitterDto createCatsitter(CatsitterInputDto catsitterInputDto) {
        Catsitter catsitter = Catsitter.builder()
                .about(catsitterInputDto.about())
                .order(catsitterInputDto.order())
                .customer(catsitterInputDto.customer())
                .build();
        catsitterRepos.save(catsitter);
        return CatsitterMapper.transferToDto(catsitter);
    }

    @Override
    public CatsitterDto editCatsitter(UUID idToEdit, CatsitterInputDto catsitterInputDto) {
        Optional<Catsitter> optionalCatsitter = catsitterRepos.findById(idToEdit);

        if (optionalCatsitter.isPresent()) {
            Catsitter catsitter = optionalCatsitter.get();
            if (catsitterInputDto.about() != null) {
                catsitter.setAbout(catsitterInputDto.about());
            }
            if (catsitterInputDto.order() != null) {
                catsitter.setOrder(catsitterInputDto.order());
            }
            if (catsitterInputDto.customer() != null) {
                catsitter.setCustomer(catsitterInputDto.customer());
            }
            catsitterRepos.save(catsitter);
            return CatsitterMapper.transferToDto(catsitter);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id.");
        }
    }

    @Override
    public UUID deleteCatsitter(UUID idToDelete) {
        catsitterRepos.deleteById(idToDelete);
        return idToDelete;
    }

//    @Override
//    public CatsitterDto assignOrderToCatsitter(long catsitterId, long orderNo) {
//        Optional<Catsitter> optionalCatsitter = catsitterRepos.findById(catsitterId);
//        Optional<Order> optionalOrder = orderRepos.findById(orderNo);
//
//        if (optionalCatsitter.isPresent() && optionalOrder.isPresent()) {
//            Catsitter catsitter = optionalCatsitter.get();
//            Order order = optionalOrder.get();
//
//            catsitter.setOrder(order);
//            catsitterRepos.save(catsitter);
//            return CatsitterMapper.transferToDto(catsitter);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer or car found with this id");
//        }
//    }
}





