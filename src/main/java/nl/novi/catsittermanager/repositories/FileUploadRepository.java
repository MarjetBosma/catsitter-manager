package nl.novi.catsittermanager.repositories;

import nl.novi.catsittermanager.models.FileUploadResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadResponse, String > {
    Optional<FileUploadResponse> findByFileName(String fileName);
}
