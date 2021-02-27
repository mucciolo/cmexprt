package br.net.comexport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String NAME = "name";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @Temporal(DATE)
    @JsonFormat(pattern = DATE_PATTERN)
    private Date birthdate;

    @CreatedDate
    @Column(updatable = false)
    @JsonInclude(NON_NULL)
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @NotNull
    private Boolean enabled = true;

    public User(@NotBlank final String name, @Email final String email, final Date birthdate) {
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
    }
}