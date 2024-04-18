package nl.novi.catsittermanager.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(name="images")
public class FileUploadResponse {

    @Id
    String fileName;
    String contentType;
    String url;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cat_id")
    Cat cat;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="catsitter_id")
    Catsitter catsitter;

    public FileUploadResponse(String fileName, String contentType, String url) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.url = url;
    }

    public FileUploadResponse() {}
}
