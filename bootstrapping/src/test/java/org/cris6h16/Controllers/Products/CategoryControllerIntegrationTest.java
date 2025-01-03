package org.cris6h16.Controllers.Products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cris6h16.Controllers.Common;
import org.cris6h16.Main;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.CategoryDTO;
import org.cris6h16.facades.CreateCategoryDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.UserComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cris6h16.Controllers.Common.loginOutputInExpectedState;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerIntegrationTest {
    @MockBean
    private JavaMailSender javaMailSender; // avoid sending real emails


    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final SignupDTO sellerDto = SignupDTO.builder()
            .firstname("cris6h16")
            .lastname("inGithub")
            .email("fake@gmail.com")
            .password("123456ytghfdsetr")
            .build();


    @Autowired
    ProductComponent productComponent;
            @Autowired UserComponent userComponent;
            @Autowired SecurityComponent securityComponent;
            @Autowired TransactionTemplate transactionTemplate;

    @BeforeEach
    void beforeEach(
            @Autowired EmailComponent emailComponent,
            @Autowired TransactionTemplate transactionTemplate) {
        Common.removeAll(
                userComponent,
                emailComponent,
                productComponent,
                transactionTemplate
        );
    }


    @Test
    void createCategory_successful() throws Exception {
        // Arrange
        LoginOutput output = loginOutputInExpectedState(userComponent,securityComponent,transactionTemplate, "ROLE_SELLER");
        CreateCategoryDTO createCategoryDTO = CreateCategoryDTO.builder()
                .name("Mobiles")
                .build();

        // Act && Assert
        mockMvc.perform(post("/api/v1/products/categories/create-category")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + output.getAccessToken())
                        .content(objectMapper.writeValueAsString(createCategoryDTO)))
                .andExpect(status().isCreated());

        String categoriesSet = mockMvc.perform(get("/api/v1/products/categories")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TypeReference<Set<CategoryDTO>> ref = new TypeReference<>() {
        };
        Set<CategoryDTO> cats = objectMapper.readValue(categoriesSet, ref);
        assertThat(cats)
                .hasSize(1)
                .anySatisfy(category ->
                        assertThat(category).hasFieldOrPropertyWithValue("name", "Mobiles"));
    }


}