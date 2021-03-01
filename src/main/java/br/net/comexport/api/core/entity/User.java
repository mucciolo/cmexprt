package br.net.comexport.api.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class User implements Serializable, Updatable<User> {

    private static final long serialVersionUID = 1L;

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String NAME = "name";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, updatable = false)
    private String email;

    @ApiModelProperty(example = "01/02/4567")
    @Temporal(DATE)
    @JsonFormat(pattern = DATE_PATTERN)
    @Column(nullable = false)
    private Date birthdate;

    @ApiModelProperty(hidden = true)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @ApiModelProperty(hidden = true)
    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    @ApiModelProperty(hidden = true)
    @JsonProperty(access = READ_ONLY)
    @NotNull
    @Column(nullable = false)
    private Boolean enabled = true;

    public User update(final User userToBeUpdated) {
        userToBeUpdated.setName(this.name);
        userToBeUpdated.setBirthdate(this.birthdate);

        return userToBeUpdated;
    }
}