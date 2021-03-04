package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class CreatelessBaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Test
    void findById_shouldReturn404_whenEntityNotFound() throws Exception {

        final User user =
                User.builder().id(1L).name("name").birthdate(Date.from(Instant.now())).email("email@email.com").build();

        this.mockMvc.perform(get("/user/" + user.getId())).andExpect(status().isNotFound());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/user/" + user.getId())).andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void update_shouldReturn_updatedEntity() throws Exception {

        final User currentUser =
                User.builder()
                    .id(1L)
                    .name("name")
                    .birthdate(new SimpleDateFormat(User.DATE_PATTERN).parse("01/01/1991"))
                    .email("email@email.com")
                    .build();

        final User updatedUser = User.builder()
                                     .id(currentUser.getId())
                                     .name("more " + currentUser.getName())
                                     .birthdate(currentUser.getBirthdate())
                                     .email(currentUser.getEmail())
                                     .build();

        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));

        objectMapper.setSerializationInclusion(NON_NULL);

        this.mockMvc.perform(put("/user/" + currentUser.getId())
                                     .contentType(APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(updatedUser)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }
}