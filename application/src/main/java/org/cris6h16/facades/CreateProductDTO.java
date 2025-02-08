package org.cris6h16.facades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private BigDecimal weightPounds;
    private Integer widthCM;
    private Integer heightCM;
    private Integer lengthCM;
    private List<MultipartFile> images;
    private Long categoryId;
}
