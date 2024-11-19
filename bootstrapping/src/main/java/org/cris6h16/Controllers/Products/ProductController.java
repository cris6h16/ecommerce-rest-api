package org.cris6h16.Controllers.Products;


import org.cris6h16.facades.CreateProductDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;
import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;

@RestController
@RequestMapping(ProductController.PRODUCT_PATH)
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
    public ResponseEntity<Page<ProductOutput>> findMyProducts(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.findMyProducts(pageable));
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )// todo: deberia retornar un DTO, no directamente el output
    public ResponseEntity<Page<ProductOutput>> findAllProducts(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(productFacade.findAllProducts(pageable));
    }

}