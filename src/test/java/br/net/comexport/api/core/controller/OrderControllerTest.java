package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.controller.enums.CanalDeVenda;
import br.net.comexport.api.core.entity.Order;
import br.net.comexport.api.core.entity.Product;
import br.net.comexport.api.core.entity.User;
import br.net.comexport.api.core.repository.OrderRepository;
import br.net.comexport.api.core.repository.ProductRepository;
import br.net.comexport.api.core.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static br.net.comexport.api.core.controller.enums.CanalDeVenda.*;
import static br.net.comexport.api.core.entity.Order.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    private static Stream<Arguments> orderCreationValidaitonProvider() {
        return Stream.of(
                Arguments.of(false, false),
                Arguments.of(false, true),
                Arguments.of(true, false)
        );
    }

    private static Stream<Arguments> canalDeVendaMappingProvider() {
        return Stream.of(
                Arguments.of(LOJA_FISICA, ENTREGUE, 17.89, 17.89),
                Arguments.of(PARCEIROS, AGUARDANDO_RETIRADA_PARCEIRO, 8.49, 8.49 * 1.0878 + 10.34),
                Arguments.of(E_COMMERCE, AGUARDANDO_ENTREGA, 10.99, 10.99 * 1.0537)
        );
    }

    @ParameterizedTest
    @MethodSource("orderCreationValidaitonProvider")
    void shouldValidate_orderCreation(final boolean userExistsById, final boolean productExistsById) throws Exception {

        final Order.CreationDTO creationDTO = Order.CreationDTO.builder().userId(1L).productId(1L).canalDeVenda(
                E_COMMERCE).build();

        when(userRepository.existsById(creationDTO.getUserId())).thenReturn(userExistsById);
        when(productRepository.existsById(creationDTO.getProductId())).thenReturn(productExistsById);

        this.mockMvc.perform(put("/order").contentType(APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(creationDTO)))
                    .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("canalDeVendaMappingProvider")
    void shouldApply_canalDeVendaStatusAndOrderPriceMapping_onCreation(final CanalDeVenda canalDeVenda,
                                                                       final Order.Status status,
                                                                       final Double productPrice,
                                                                       final Double orderPrice) throws Exception {

        final Order.CreationDTO creationDTO = Order.CreationDTO.builder().userId(1L).productId(1L).canalDeVenda(
                canalDeVenda).build();

        final User user = User.builder().id(creationDTO.getUserId()).build();
        final Product product = Product.builder().id(creationDTO.getProductId()).price(productPrice).build();

        final Order order = Order.builder()
                                 .user(user)
                                 .product(product)
                                 .status(status)
                                 .price(orderPrice)
                                 .build();

        when(userRepository.existsById(creationDTO.getUserId())).thenReturn(true);
        when(userRepository.getOne(creationDTO.getUserId())).thenReturn(user);
        when(productRepository.existsById(creationDTO.getProductId())).thenReturn(true);
        when(productRepository.getOne(creationDTO.getProductId())).thenReturn(product);

        this.mockMvc.perform(put("/order").contentType(APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(creationDTO)))
                    .andExpect(status().isCreated());

        verify(orderRepository).save(orderArgumentCaptor.capture());

        assertThat(orderArgumentCaptor.getValue()).isEqualTo(order);
    }
}