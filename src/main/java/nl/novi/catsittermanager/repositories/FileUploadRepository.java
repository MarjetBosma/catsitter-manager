package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.ImageUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<ImageUpload, String > {
    Optional<ImageUpload> findByFileName(String fileName);
}
