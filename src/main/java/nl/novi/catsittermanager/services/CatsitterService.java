package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.mappers.CatsitterMapper;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.Order;
import nl.novi.catsittermanager.repositories.CatsitterRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatsitterService {

    private final CatsitterRepository catsitterRepos;

    public CatsitterService(CatsitterRepository catsitterRepos) {
        this.catsitterRepos = catsitterRepos;
    }

    public List<CatsitterDto> getAllCatsitters() {
        return catsitterRepos.findAll()
                .stream()
                .map(CatsitterMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public CatsitterDto getCatsitter(String username) {
        return catsitterRepos.findById(username)
                .map(CatsitterMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id."));
    }

    public CatsitterDto createCatsitter(final CatsitterInputDto catsitterInputDto) {
        Catsitter catsitter = CatsitterMapper.transferFromDto((catsitterInputDto));
        catsitter.setEnabled(true);
        catsitter.setAbout(catsitterInputDto.about());
        catsitter.setOrders(new ArrayList<Order>());
        catsitterRepos.save(catsitter);
        return CatsitterMapper.transferToDto(catsitter);
    }

    public CatsitterDto editCatsitter(String username, CatsitterInputDto catsitterInputDto) {
        Optional<Catsitter> optionalCatsitter = catsitterRepos.findById(username);

        if (optionalCatsitter.isPresent()) {
            Catsitter catsitter = optionalCatsitter.get();
            if (catsitterInputDto.username() != null) {
                catsitter.setUsername(catsitterInputDto.username());
            }
            if (catsitterInputDto.password() != null) {
                catsitter.setPassword(catsitterInputDto.password());
            }
            if (catsitterInputDto.name() != null) {
                catsitter.setName(catsitterInputDto.name());
            }
            if (catsitterInputDto.address() != null) {
                catsitter.setAddress(catsitterInputDto.address());
            }
            if (catsitterInputDto.email() != null) {
                catsitter.setEmail(catsitterInputDto.email());
            }
            if (catsitterInputDto.about() != null) {
                catsitter.setAbout(catsitterInputDto.about());
            }
            if (catsitterInputDto.orders() != null) {
                catsitter.setOrders(catsitterInputDto.orders());
            }
//            if (catsitterInputDto.customers() != null) {
//                catsitter.setCustomers(catsitterInputDto.customers());
//            }
            catsitterRepos.save(catsitter);
            return CatsitterMapper.transferToDto(catsitter);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No catsitter found with this id.");
            }
        }

        public String deleteCatsitter (String username) {
            catsitterRepos.deleteById(username);
            return username;
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






