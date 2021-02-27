package br.net.comexport.controller.enums;

import br.net.comexport.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static java.util.function.Function.identity;

@Getter
@AllArgsConstructor
public enum CanalDeVenda {

    E_COMMERCE(Order.Status.AGUARDANDO_ENTREGA,
               productPrice -> productPrice * Constants.E_COMMERCE_MULTIPLIER),

    LOJA_FISICA(Order.Status.ENTREGUE,
                identity()),

    PARCEIROS(Order.Status.AGUARDANDO_RETIRADA_PARCEIRO,
              productPrice -> productPrice * Constants.PARCEIROS_MULTIPLIER + Constants.PARCEIROS_TAX);

    @JsonIgnore
    private final Order.Status correspondingOrderStatus;

    @JsonIgnore
    private final Function<Double, Double> productFinalPriceFunction;

    private static class Constants {
        private static final double E_COMMERCE_PERCENTUAL_INCREASE = 5.37 / 100.;
        private static final double E_COMMERCE_MULTIPLIER = (1 + E_COMMERCE_PERCENTUAL_INCREASE);
        private static final double PARCEIROS_PERCENTUAL_INCREASE = 8.78 / 100.;
        private static final double PARCEIROS_MULTIPLIER = (1 + PARCEIROS_PERCENTUAL_INCREASE);
        private static final double PARCEIROS_TAX = 10.34;
    }
}