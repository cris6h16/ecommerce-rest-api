package org.cris6h16.Products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cris6h16.Main;
import org.cris6h16.facades.CategoryDTO;
import org.cris6h16.facades.CreateCategoryDTO;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SignupDTO;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final SignupDTO userDTO = SignupDTO.builder()
            .firstname("Cristian")
            .lastname("Herrera")
            .email("cristianmherrera21@gmail.com")
            .password("12345678")
            .build();

    private static final SignupDTO sellerDto = SignupDTO.builder()
            .firstname("cris6h16")
            .lastname("inGithub")
            .email("fake@gmail.com")
            .password("123456ytghfdsetr")
            .build();

    private static final SignupDTO adminDto = SignupDTO.builder()
            .firstname("admin")
            .lastname("admin1234")
            .email("admin@gmail.com")
            .password("wefwefewfwebg34")
            .build();

    @BeforeAll
    static void beforeAll(
            @Autowired UserComponent userComponent,
            @Autowired SecurityComponent securityComponent,
            @Autowired TransactionTemplate transactionTemplate) {
        transactionTemplate.execute(action -> {
            userComponent.create(CreateUserInput.builder()
                    .firstname(adminDto.getFirstname())
                    .lastname(adminDto.getLastname())
                    .email(adminDto.getEmail())
                    .password(securityComponent.encodePassword(adminDto.getPassword()))
                    .authorities(new HashSet<>(List.of("ROLE_ADMIN")))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .emailVerified(true)
                    .build());

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


            userComponent.create(CreateUserInput.builder()
                    .firstname(userDTO.getFirstname())
                    .lastname(userDTO.getLastname())
                    .email(userDTO.getEmail())
                    .password(securityComponent.encodePassword(userDTO.getPassword()))
                    .authorities(new HashSet<>(List.of("ROLE_USER")))
                    .balance(BigDecimal.ZERO)
                    .enabled(true)
                    .emailVerified(true)
                    .build());
            return null;
        });
    }


    @Test
    void createCategory_successful() throws Exception {
        // Arrange
        LoginOutput output = login(sellerDto);
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

    private LoginOutput login(SignupDTO dto) throws Exception {
        LoginDTO loginDTO = LoginDTO.builder()
                .email(sellerDto.getEmail())
                .password(sellerDto.getPassword())
                .build();

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, LoginOutput.class);
    }

}