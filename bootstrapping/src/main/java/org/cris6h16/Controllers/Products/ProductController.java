package org.cris6h16.Controllers.Products;


import lombok.extern.slf4j.Slf4j;
import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.ProductDTO;
import org.cris6h16.facades.ProductFacade;
import org.cris6h16.product.ProductOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;
import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;

@RestController
@RequestMapping(ProductController.PRODUCT_PATH)
@Slf4j
public class ProductController {

    protected final ProductFacade productFacade;
    static final String PRODUCT_PATH = PATH_PREFIX + "/products";

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }


    @PostMapping(
            path = "/create-product",
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
            path = "/my-products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<org.cris6h16.facades.ProductDTO>> findMyProducts(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.findMyProducts(pageable));
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<org.cris6h16.facades.ProductDTO>> findAllProducts(
            @RequestParam(required = false) Map<String, String> filters,
            Pageable pageable) {

        filters = filters == null ? new HashMap<>() : filters;
        Map<String, String> cleanedFilters = new HashMap<>(filters);
        cleanedFilters.remove("page");
        cleanedFilters.remove("size");
        cleanedFilters.remove("sort");
        filters = cleanedFilters;

        log.debug("filters: {}", filters);
        log.debug("pageable: {}", pageable);

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


}