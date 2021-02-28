package br.net.comexport.api.core.entity;

import br.net.comexport.api.core.controller.enums.CanalDeVenda;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import com.fasterxml.jackson.annotation.*;
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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static java.lang.String.format;
import static java.lang.String.join;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.format.annotation.NumberFormat.Style.CURRENCY;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @ManyToOne(optional = false, fetch = LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                      property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user.id")
    @NotNull
    private User user;

    @ManyToOne(optional = false, fetch = LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                      property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("product.id")
    @NotNull
    private Product product;

    @NotNull
    private Status status;

    @NumberFormat(style = CURRENCY)
    @NotNull
    private Double price;

    @CreatedDate
    @Column(updatable = false)
    @JsonInclude(NON_NULL)
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public enum Status {
        AGUARDANDO_ENTREGA,
        ENTREGUE,
        AGUARDANDO_RETIRADA_PARCEIRO
    }

    @Data
    @NoArgsConstructor
    public static class CreationDTO {

        private static final String FMT_USER_NOT_EXISTS = "User ID %s does not exists.";
        private static final String FMT_PRODUCT_NOT_EXISTS = "Product ID %s does not exists.";

        @NotNull Long userId;
        @NotNull Long productId;
        @NotNull CanalDeVenda canalDeVenda;

        public Order toEntity(@NotNull final UserRepository userRepository,
                              @NotNull final ProductRepository productRepository)
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

        public void throwExceptionIfNotValid(@NotNull final UserRepository userRepository,
                                             @NotNull final ProductRepository productRepository)
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