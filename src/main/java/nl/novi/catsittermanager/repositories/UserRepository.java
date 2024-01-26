package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
