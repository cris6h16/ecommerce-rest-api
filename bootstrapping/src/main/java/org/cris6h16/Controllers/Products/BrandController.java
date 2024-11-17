package org.cris6h16.Controllers.Products;

import org.cris6h16.facades.CreateBrandDTO;
import org.cris6h16.facades.ProductFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.cris6h16.Controllers.Products.ProductController.PRODUCT_PATH;

@RestController
@RequestMapping(BrandController.BRAND_PATH)
public class BrandController {
    public static final String BRAND_PATH = PRODUCT_PATH + "/brands";
    private final ProductFacade productFacade;

    public BrandController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBrand(@RequestBody CreateBrandDTO dto) {
        Long id = productFacade.createBrand(dto);
        return ResponseEntity.created(
                URI.create(BRAND_PATH + "/" + id)
        ).build();
    }
}
