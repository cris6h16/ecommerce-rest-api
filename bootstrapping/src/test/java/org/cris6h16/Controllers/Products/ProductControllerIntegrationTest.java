package org.cris6h16.Controllers.Products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cris6h16.Main;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.file.FirebaseFileComponentImpl;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserComponent;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.cris6h16.Controllers.Common.login;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

    private ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private FirebaseFileComponentImpl firebaseFileComponent;
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
    void createProduct() throws Exception {
        // Arrange
        LoginOutput output = login(sellerDto, mockMvc);

        // crear categoria
        String cL = mockMvc.perform(post("/api/v1/products/categories/create-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + output.getAccessToken())
                        .content("{\"name\":\"test-category\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        Long categoryId = Long.valueOf(cL.split(".*/")[1]);

        //
        MockMultipartFile img = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "hola".getBytes()
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
                .image(img)
                .build();

        when(firebaseFileComponent.upload(img)).thenReturn("https://fake.link/etc");

        // Act
        String location = mockMvc.perform(multipart("/api/v1/products/create-product")
                        .file("image", img.getBytes())
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

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://fake.link/etc"));
    }

}
