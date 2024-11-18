package org.cris6h16.Controllers.Products;


import org.cris6h16.facades.CreateProductDTO;
import org.cris6h16.facades.ProductFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;

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
                        URI.create(PRODUCT_PATH + id))
                .build();
    }

}