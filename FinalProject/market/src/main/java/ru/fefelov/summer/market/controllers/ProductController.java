package ru.fefelov.summer.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.fefelov.summer.market.exceptions.ResourceNotFoundException;
import ru.fefelov.summer.market.dto.ProductDto;
import ru.fefelov.summer.market.model.Product;
import ru.fefelov.summer.market.services.ProductService;
import ru.fefelov.summer.market.utils.SpecHelper;
import ru.fefelov.summer.market.utils.SpecOption;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/{id}")
    public ProductDto findById(@PathVariable Long id) {
        Product p = productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found, id: " + id));
        return new ProductDto(p);
    }

    @GetMapping
    public Page<ProductDto> findAll(
            @RequestParam(name = "p", defaultValue = "1") int pageIndex,
            @RequestParam(name = "spec_options", required = false) List<SpecOption> options
    ) {
        return productService.findPage(pageIndex - 1, 5,
                SpecHelper.buildProductSpecification(options)).map(ProductDto::new);
    }


    @PostMapping
    public ProductDto createNewProduct(@RequestBody ProductDto newProductDto) {
        Product product = new Product();
        product.setPrice(newProductDto.getPrice());
        product.setTitle(newProductDto.getTitle());
        return new ProductDto(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }
}
