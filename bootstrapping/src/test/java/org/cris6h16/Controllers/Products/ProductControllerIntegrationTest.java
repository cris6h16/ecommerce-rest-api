package org.cris6h16.Controllers.Products;

import org.cris6h16.Controllers.Common;
import org.cris6h16.Main;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.product.ProductOutput;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cris6h16.Controllers.Common.loginOutputInExpectedState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @MockBean
    private JavaMailSender mailSender;

    private static final SignupDTO sellerDto = SignupDTO.builder()
            .firstname("Cristian")
            .lastname("Herrera")
            .email("cristianmherrera21@gmail.com")
            .password("12345678")
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private EmailComponent emailComponent;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private SecurityComponent securityComponent;

    @Autowired
    private ProductComponent productComponent;

    @BeforeEach
    void beforeEach(
            @Autowired ProductComponent productComponent) {
        Common.removeAll(
                userComponent,
                emailComponent,
                productComponent,
                transactionTemplate
        );
    }

    @Test
    void createProduct() throws Exception {
        // Arrange
        LoginOutput output = loginOutputInExpectedState(userComponent, securityComponent, transactionTemplate, "ROLE_SELLER");
        Long categoryId = createCategory("category-test", output.getAccessToken());
        MockMultipartFile img = new MockMultipartFile(
                "image",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "hola" .getBytes()
        );

        CreateProductDTO dto = CreateProductDTO.builder()
                .name("Laptop Hp Victu 15")
                .price(BigDecimal.valueOf(1100))
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum")
                .stock(10)
                .approxWeightLb(3)
                .approxHeightCm(30)
                .approxWidthCm(35)
                .categoryId(categoryId)
                .image(null)
                .build();

        // Act
        String location = mockMvc.perform(multipart("/api/v1/products/create-product")
                        .file(img)
                        .param("name", dto.getName())
                        .param("price", String.valueOf(dto.getPrice()))
                        .param("description", dto.getDescription())
                        .param("stock", String.valueOf(dto.getStock()))
                        .param("approxWeightLb", String.valueOf(dto.getApproxWeightLb()))
                        .param("approxHeightCm", String.valueOf(dto.getApproxHeightCm()))
                        .param("approxWidthCm", String.valueOf(dto.getApproxWidthCm()))
                        .param("categoryId", String.valueOf(dto.getCategoryId()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + output.getAccessToken()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        // Assert
        assertNotNull(location);
        assertTrue(location.matches("/api/v1/products/[0-9]+"));
        verifyProduct(location.split(".*/")[1], dto);
    }

    private void verifyProduct(String productId, CreateProductDTO dto) {
        ProductOutput output = productComponent.findProductByIdNoEager(Long.valueOf(productId));
        assertNull(output.getUser());
        assertTrue(output.getId() > 0);
        assertNotNull(output.getCategory());
        assertEquals(0, output.getPrice().compareTo(dto.getPrice()));
        assertTrue(output.getImageUrl().matches("https://firebasestorage\\.googleapis\\.com/v0/b/ecommerce-de918\\.firebasestorage\\.app/o/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.txt\\?alt=media")
        );
        assertThat(output)
                .hasFieldOrPropertyWithValue("name", dto.getName())
                .hasFieldOrPropertyWithValue("stock", dto.getStock())
                .hasFieldOrPropertyWithValue("description", dto.getDescription())
                .hasFieldOrPropertyWithValue("approxWeightLb", dto.getApproxWeightLb())
                .hasFieldOrPropertyWithValue("approxWidthCm", dto.getApproxWidthCm())
                .hasFieldOrPropertyWithValue("approxHeightCm", dto.getApproxHeightCm());
    }

    private Long createCategory(String name, String accessToken) throws Exception {
        String cL = mockMvc.perform(post("/api/v1/products/categories/create-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content("{\"name\":\"@@@\"}" .replace("@@@", name)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        return Long.valueOf(cL.split(".*/")[1]);
    }

}
