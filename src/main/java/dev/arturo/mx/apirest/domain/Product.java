package dev.arturo.mx.apirest.domain;

import com.mongodb.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "Product")
public class Product implements Serializable {

    @Id
    @NonNull
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String imagePath;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String distributor;
    @Getter
    @Setter
    private Date releaseDate;

    public Product(int id, String imagePath, String title, String description) {
        this.id = id;
        this.imagePath = imagePath;
        this.title = title;
        this.description = description;
    }

}
