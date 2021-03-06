package br.net.comexport.api.core.entity;

import br.net.comexport.api.core.controller.enums.CanalDeVenda;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.format;
import static java.lang.String.join;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.format.annotation.NumberFormat.Style.CURRENCY;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class Order implements Updatable<Order> {

    protected static final String JSON_PROPERTY_USER_ID = "user.id";
    protected static final String JSON_PROPERTY_PRODUCT_ID = "product.id";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ApiModelProperty(dataType = "long", example = "1")
    @ManyToOne(optional = false, fetch = LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                      property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(JSON_PROPERTY_USER_ID)
    @NotNull
    private User user;

    @ApiModelProperty(dataType = "long", example = "2")
    @ManyToOne(optional = false, fetch = LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                      property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(JSON_PROPERTY_PRODUCT_ID)
    @NotNull
    private Product product;

    @NotNull
    private Status status;

    @ApiModelProperty(example = "10.99")
    @NumberFormat(style = CURRENCY)
    @NotNull
    private Double price;

    @ApiModelProperty(hidden = true)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @ApiModelProperty(hidden = true)
    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    @Override
    public Order update(final Order entityToBeUpdate) {
        entityToBeUpdate.setUser(this.user);
        entityToBeUpdate.setProduct(this.product);
        entityToBeUpdate.setStatus(this.status);
        entityToBeUpdate.setPrice(this.price);

        return entityToBeUpdate;
    }

    public enum Status {
        AGUARDANDO_ENTREGA,
        ENTREGUE,
        AGUARDANDO_RETIRADA_PARCEIRO
    }

    @Data
    @NoArgsConstructor(access = PACKAGE)
    @AllArgsConstructor(access = PRIVATE)
    @Builder
    public static class CreationDTO {

        private static final String FMT_USER_NOT_EXISTS = "User ID %s does not exists.";
        private static final String FMT_PRODUCT_NOT_EXISTS = "Product ID %s does not exists.";

        @NotNull
        @JsonProperty(JSON_PROPERTY_USER_ID)
        private Long userId;

        @NotNull
        @JsonProperty(JSON_PROPERTY_PRODUCT_ID)
        private Long productId;

        @NotNull
        private CanalDeVenda canalDeVenda;

        public Order toEntity(final UserRepository userRepository,
                              final ProductRepository productRepository)
                throws ConstraintViolationException {

            throwExceptionIfNotValid(userRepository, productRepository);

            final Product product = productRepository.getOne(productId);

            return Order.builder()
                        .user(userRepository.getOne(userId))
                        .product(product)
                        .status(canalDeVenda.getCorrespondingOrderStatus())
                        .price(canalDeVenda.getProductFinalPriceFunction().apply(product.getPrice()))
                        .build();
        }

        public void throwExceptionIfNotValid(final UserRepository userRepository,
                                             final ProductRepository productRepository)
                throws ConstraintViolationException {

            final ArrayList<String> validationMessages = new ArrayList<>(2);

            if (!userRepository.existsById(userId))
                validationMessages.add(format(FMT_USER_NOT_EXISTS, userId));

            if (!productRepository.existsById(productId))
                validationMessages.add(format(FMT_PRODUCT_NOT_EXISTS, productId));

            if (!validationMessages.isEmpty())
                throw new ConstraintViolationException(join("\n", validationMessages), null);
        }
    }
}