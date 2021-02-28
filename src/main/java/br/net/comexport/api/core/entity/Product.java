package br.net.comexport.api.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static javax.persistence.GenerationType.IDENTITY;
import static org.springframework.format.annotation.NumberFormat.Style.CURRENCY;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Product implements Updatable<Product> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    @NumberFormat(style = CURRENCY)
    @PositiveOrZero
    private Double price;

    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @Override
    public Product update(final Product entityToBeUpdate) {
        entityToBeUpdate.setDescription(this.description);
        entityToBeUpdate.setPrice(this.price);

        return entityToBeUpdate;
    }
}