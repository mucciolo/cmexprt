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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void create_shouldReturn201() throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        final User putUser = User.builder()
                                 .name("name")
                                 .birthdate(new SimpleDateFormat("dd/MM/yyy").parse("01/01/2001"))
                                 .email("email@email.com")
                                 .build();

        objectMapper.setSerializationInclusion(NON_NULL);

        this.mockMvc.perform(put("/user").contentType(APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(putUser)))
                    .andExpect(status().isCreated());

        verify(userRepository).save(userArgumentCaptor.capture());
        final User savedUser = userArgumentCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo(putUser.getName());
        assertThat(savedUser.getEmail()).isEqualTo(putUser.getEmail());
        assertThat(savedUser.getBirthdate()).isEqualTo(putUser.getBirthdate());
    }
}