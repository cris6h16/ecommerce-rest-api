package org.cris6h16.Controllers.Products;

// todo: para todo el proyecto: ESCAPAR ENTRADAS
import lombok.extern.slf4j.Slf4j;
import org.cris6h16.facades.CategoryDTO;
import org.cris6h16.facades.CreateCategoryDTO;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.ProductDTO;
import org.cris6h16.facades.ProductFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;

@RestController
@RequestMapping(ProductController.PRODUCT_PATH)
@Slf4j
public class ProductController {

    protected final ProductFacade productFacade;
    static final String PRODUCT_PATH = "api/v1/products";
    public static final String CATEGORY_PATH = ProductController.PRODUCT_PATH + "/categories";

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }


    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<Void> createProduct(@ModelAttribute CreateProductDTO createProductDTO) {
        Long id = productFacade.createProduct(createProductDTO);
        return ResponseEntity.created(
                        URI.create(PRODUCT_PATH + "/" + id))
                .build();
    }
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<org.cris6h16.facades.ProductDTO>> findAllProducts(
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(required = false, name = "price") List<String> prices,
            Pageable pageable) {

        filters = filters == null ? new HashMap<>() : filters;
        Map<String, String> cleanedFilters = new HashMap<>(filters);
        cleanedFilters.remove("page");
        cleanedFilters.remove("size");
        cleanedFilters.remove("sort");
        filters = cleanedFilters;

        if (prices != null && !prices.isEmpty()) {
            filters.put("price", String.join(",", prices));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.findAllProducts(pageable, filters));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.getProductById(id));
    }


    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @ModelAttribute CreateProductDTO createProductDTO) {
        productFacade.putProduct(id, createProductDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productFacade.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            path = CATEGORY_PATH + "/create-category",
            consumes = "application/json"
    )
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryDTO createCategoryDTO) {
        Long id = productFacade.createCategory(createCategoryDTO);
        ResponseEntity<Long> res = ResponseEntity.created(URI.create(CATEGORY_PATH + "/" + id)).body(id);
        log.debug("Category created: {}", res);
        return res;
    }

    @GetMapping(
            path = CATEGORY_PATH,
            produces = "application/json"
    )
    public ResponseEntity<Set<CategoryDTO>> getCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.getCategories());
    }

}