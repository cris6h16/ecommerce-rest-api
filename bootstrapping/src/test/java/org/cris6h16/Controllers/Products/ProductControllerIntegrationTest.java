package org.cris6h16.Controllers.Products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cris6h16.Controllers.Common;
import org.cris6h16.Main;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.product.CreateCategoryInput;
import org.cris6h16.product.CreateProductInput;
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
import static org.cris6h16.Controllers.Common.defSignupDTO;
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

    private ObjectMapper objectMapper = new ObjectMapper();


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

    private LoginOutput output;
    private Long userId;
    Long categoryId;


    @BeforeEach
    void beforeEach(
            @Autowired ProductComponent productComponent) {
        Common.removeAll(
                userComponent,
                emailComponent,
                productComponent,
                transactionTemplate
        );

        output = loginOutputInExpectedState(userComponent, securityComponent, transactionTemplate, "ROLE_SELLER");
        userId = userComponent.findByEmailAndEnabled(defSignupDTO.getEmail(), true).orElseThrow().getId();
        categoryId = productComponent.createCategory(CreateCategoryInput.builder().name("category-test").build());
    }


    @Test
    void createProduct() throws Exception {
        // Arrange
        MockMultipartFile img = new MockMultipartFile(
                "image",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "hola".getBytes()
        );

        CreateProductDTO dto = CreateProductDTO.builder()
                .name("Laptop Hp Victu 15")
                .price(BigDecimal.valueOf(1100))
                .description("Lorem Ipsum is simply dummy")
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
                        .content("{\"name\":\"@@@\"}".replace("@@@", name)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        return Long.valueOf(cL.split(".*/")[1]);
    }


    @Test
    void findMyProducts() throws Exception {
        // Arrange
        save10Products(userId, categoryId);

        // Act
        String body = mockMvc.perform(get("/api/v1/products/my-products?size=7&page=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + output.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.size").value(7))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Tablet Lenovo"))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    void findAllProducts_pageable() throws Exception {
        // Arrange
        save10Products(userId, categoryId);

        // Act
        String body = mockMvc.perform(get("/api/v1/products?size=2&page=1&sort=id,asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + output.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Keyboard Razer"))
                .andReturn().getResponse().getContentAsString();


    }

    @Test
    void findAllProducts_sortSpecifications() throws Exception {
        // Arrange
        save10Products(userId, categoryId);

        // Act
        String body = mockMvc.perform(get("/api/v1/products?size=9&page=0&sort=price,desc&name=razer Mousepad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + output.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(9))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.content[0].name").value("Cameramousepad razer Canon"))
                .andExpect(jsonPath("$.content[0].price").value(400.99))
                .andExpect(jsonPath("$.content[1].name").value("Mousepad Razer"))
                .andExpect(jsonPath("$.content[1].price").value(20.99))
                .andReturn().getResponse().getContentAsString();
    }


    private void save10Products(Long userId, Long categoryId) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            productComponent.createProduct(CreateProductInput.builder()
                    .name("Mouse Logitech G502")
                    .price(BigDecimal.valueOf(50.99))
                    .description("The logitech mouse is the best")
                    .stock(17)
                    .approxWeightLb(2)
                    .approxHeightCm(20)
                    .approxWidthCm(10)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "logitech-mouse")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Hp laptop victus 15")
                    .price(BigDecimal.valueOf(1100.99))
                    .description("A laptop for gaming")
                    .stock(3)
                    .approxWeightLb(5)
                    .approxHeightCm(30)
                    .approxWidthCm(35)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "hp-laptop")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Keyboard Razer")
                    .price(BigDecimal.valueOf(100.99))
                    .description("The best keyboard for gaming")
                    .stock(10)
                    .approxWeightLb(3)
                    .approxHeightCm(20)
                    .approxWidthCm(10)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "razer-keyboard")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Monitor LG 24")
                    .price(BigDecimal.valueOf(200.99))
                    .description("A monitor for gaming")
                    .stock(5)
                    .approxWeightLb(10)
                    .approxHeightCm(30)
                    .approxWidthCm(35)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "lg-monitor")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Mousepad Razer")
                    .price(BigDecimal.valueOf(20.99))
                    .description("The best mousepad for gaming")
                    .stock(15)
                    .approxWeightLb(1)
                    .approxHeightCm(20)
                    .approxWidthCm(10)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "razer-mousepad")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Headset HyperX")
                    .price(BigDecimal.valueOf(80.99))
                    .description("The best headset for gaming")
                    .stock(7)
                    .approxWeightLb(2)
                    .approxHeightCm(20)
                    .approxWidthCm(10)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "hyperx-headset")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Mobile phone Samsung A52")
                    .price(BigDecimal.valueOf(300.99))
                    .description("A mobile phone for gaming")
                    .stock(2)
                    .approxWeightLb(1)
                    .approxHeightCm(30)
                    .approxWidthCm(35)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "samsung-a52")
                    .userId(userId)
                    .build());

            productComponent.createProduct(CreateProductInput.builder()
                    .name("Tablet Lenovo")
                    .price(BigDecimal.valueOf(150.99))
                    .description("A tablet for gaming")
                    .stock(4)
                    .approxWeightLb(2)
                    .approxHeightCm(20)
                    .approxWidthCm(10)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "lenovo-tablet")
                    .userId(userId)
                    .build());


            productComponent.createProduct(CreateProductInput.builder()
                    .name("Smartwatch Xiaomi")
                    .price(BigDecimal.valueOf(50.99))
                    .description("A smartwatch for gaming")
                    .stock(6)
                    .approxWeightLb(1)
                    .approxHeightCm(20)
                    .approxWidthCm(10)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "xiaomi-smartwatch")
                    .userId(userId)
                    .build());


            productComponent.createProduct(CreateProductInput.builder()
                    .name("Cameramousepad razer Canon")
                    .price(BigDecimal.valueOf(400.99))
                    .description("A camera for gaming")
                    .stock(1)
                    .approxWeightLb(3)
                    .approxHeightCm(30)
                    .approxWidthCm(35)
                    .categoryId(categoryId)
                    .imageUrl("https://fireba..." + "canon-camera")
                    .userId(userId)
                    .build());
        });
    }
}
