package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<Example<User>> exampleUserArgumentCaptor;

    @Test
    public void shouldAllow_listingByAnyNamePart() throws Exception {

        final User user =
                User.builder()
                    .name("full user name")
                    .email("email@email.com")
                    .birthdate(java.util.Date.from(Instant.now()))
                    .build();

        final Page<User> page = new PageImpl<>(singletonList(user));

        when(userRepository.findAll(any(), any(Pageable.class))).thenReturn(page);

        this.mockMvc.perform(get("/user?name=user")).andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(page)));

        verify(userRepository).findAll(exampleUserArgumentCaptor.capture(), any(Pageable.class));

        assertThat(exampleUserArgumentCaptor.getValue()
                                            .getMatcher()
                                            .getPropertySpecifiers()
                                            .getForPath(User.NAME)
                                            .getStringMatcher()).isEqualTo(ExampleMatcher.StringMatcher.CONTAINING);
    }

    @Test
    public void shouldValidate_emailFormat_onCreation() throws Exception {

        final String invalidEmailFormat = "invalid_email_format";
        final User user =
                User.builder().name("name").email(invalidEmailFormat).birthdate(Date.from(Instant.now())).build();

        this.mockMvc.perform(
                put("/user").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("email"))
                    .andExpect(jsonPath("$[0].rejectedValue").value(invalidEmailFormat));

        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldIgnore_emailChange_onUpdate() throws Exception {

        final User currentUser =
                User.builder().id(1L).name("name").email("email@email.com").birthdate(Date.from(Instant.now())).build();

        final User updatedUsed =
                User.builder().id(currentUser.getId()).name(currentUser.getName()).email("another@email.com").birthdate(
                        currentUser.getBirthdate()).build();

        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));

        this.mockMvc.perform(
                put("/user/" + currentUser.getId()).contentType(APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(updatedUsed)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(currentUser)));
    }
}