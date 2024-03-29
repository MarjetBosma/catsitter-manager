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

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatsitterService {

    private final CatsitterRepository catsitterRepos;

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
        Catsitter newCatsitter = CatsitterMapper.transferFromDto((catsitterInputDto));
        newCatsitter.setEnabled(true);
        newCatsitter.setAbout(catsitterInputDto.about());
        newCatsitter.setOrders(new ArrayList<Order>());
        catsitterRepos.save(newCatsitter);
        return CatsitterMapper.transferToDto(newCatsitter);
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

}






