package org.cris6h16.Controllers;


import org.cris6h16.facades.CategoryDTO;
import org.cris6h16.facades.CreateCategoryDTO;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.ProductFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;

@RestController
@RequestMapping(ProductController.BASE_PATH)
public class ProductController {

    private final ProductFacade productFacade;
    static final String BASE_PATH = PATH_PREFIX + "/products";

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping(
            path = "/create-category",
            consumes = "application/json"
    )
    public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryDTO createCategoryDTO) {
        Long id = productFacade.createCategory(createCategoryDTO);
        return ResponseEntity.created(
                        URI.create(BASE_PATH + "/categories/" + id)) // todo: pass to YAML
                .build();
    }


    @GetMapping(
            path = "/categories",
            produces = "application/json"
    )
    public ResponseEntity<Set<CategoryDTO>> getCategories() { // todo: pageable
        return ResponseEntity.ok(productFacade.getCategories());
    }


    @PostMapping(
            path = "/create-product",
            consumes = "application/json"
    )
    public ResponseEntity<Void> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        Long id = productFacade.createProduct(createProductDTO);
        return ResponseEntity.created(
                        URI.create(BASE_PATH + "/products/" + id))
                .build();
    }

}
