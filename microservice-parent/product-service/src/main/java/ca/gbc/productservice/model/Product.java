package ca.gbc.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data // This automates creating getters and setters
@AllArgsConstructor // Creates a constructor with all attributes
@NoArgsConstructor // Creates a default constructor
@Builder // discussed later, instantiates and sets the attributes automatically
@Document(value="product") // to define this class as a document inside the database
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;

}
