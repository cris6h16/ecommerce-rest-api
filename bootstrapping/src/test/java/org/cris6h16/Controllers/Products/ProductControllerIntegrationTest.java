package org.cris6h16.Controllers.Products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cris6h16.Main;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.product.CreateBrandInput;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.cris6h16.Controllers.Common.login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final SignupDTO sellerDto = SignupDTO.builder()
            .firstname("Cristian")
            .lastname("Herrera")
            .email("cristianmherrera21@gmail.com")
            .password("12345678")
            .build();
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll(
            @Autowired UserComponent userComponent,
            @Autowired SecurityComponent securityComponent,
            @Autowired TransactionTemplate transactionTemplate) {
        transactionTemplate.execute(action -> {
            userComponent.create(CreateUserInput.builder()
                    .firstname(sellerDto.getFirstname())
                    .lastname(sellerDto.getLastname())
                    .email(sellerDto.getEmail())
                    .password(securityComponent.encodePassword(sellerDto.getPassword()))
                    .authorities(new HashSet<>(List.of("ROLE_SELLER")))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .emailVerified(true)
                    .build());
            return null;
        });
    }

    @Test
    void createBrand(){
        // Arrange
        CreateBrandInput
    }
    @Test
    void createProduct() throws Exception {
        // Arrange
        LoginOutput output = login(sellerDto, mockMvc);
        CreateProductDTO dto = CreateProductDTO.builder()
                .name("Laptop Hp Victu 15")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum")
                .approxHeightCm(30)
                .approxWidthCm(35)
                .approxWeightLb(3)
                .price(BigDecimal.valueOf(1100))
                .brandId()
                .build();

        // Act
        mockMvc.perform(post("api/v1/products/create-product")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + output.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString())
                // Assert
    }

}
