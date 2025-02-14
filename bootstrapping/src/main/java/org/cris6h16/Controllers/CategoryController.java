package org.cris6h16.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.facades.CategoryDTO;
import org.cris6h16.facades.CreateCategoryDTO;
import org.cris6h16.facades.ProductFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.Controllers.CategoryController.CATEGORY_PATH;

@RestController
@RequestMapping(CATEGORY_PATH)
@Slf4j
public class CategoryController {
    public static final String CATEGORY_PATH = ProductController.PRODUCT_PATH + "/categories";
    protected final ProductFacade productFacade;

    public CategoryController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping(
            path = "/create-category",
            consumes = "application/json"
    )
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryDTO createCategoryDTO) {
        Long id = productFacade.createCategory(createCategoryDTO);
        ResponseEntity<Long> res = ResponseEntity.created(URI.create(CATEGORY_PATH + "/" + id)).body(id);
        log.debug("Category created: {}", res);
        return res;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Set<CategoryDTO>> getCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.getCategories());
    }
}