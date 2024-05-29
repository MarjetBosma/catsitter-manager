package nl.novi.catsittermanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name="images")
public class ImageUpload {

    @Id
    String filename;
    String contentType;
    String url;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cat_id")
    Cat cat;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="catsitter_id")
    Catsitter catsitter;

    public ImageUpload(String filename, String contentType, String url) {
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
    }

    public ImageUpload() {}
}
