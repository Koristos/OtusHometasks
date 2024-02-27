package ru.fefelov.summer.market.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.fefelov.summer.market.repositories.specifications.ProductSpecifications;
import ru.fefelov.summer.market.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class SpecHelper {

    public static Specification<Product> buildProductSpecification (List<SpecOption> specList){
        Specification<Product> spec = Specification.where(null);
        if (specList != null) {
            specList.forEach(sp -> addSpecification(spec, sp.getName(), sp.getValue()));
        }
        return spec;
    }

    private static void addSpecification (Specification <Product> spec, String name, String value){
        switch (name){
            case ("min_price"):
                if (NumberUtils.isCreatable(value)){
                    spec.and(ProductSpecifications.priceGreaterOrEqualsThan(new BigDecimal(value)));
                }
                break;
            case ("max_price"):
                if (NumberUtils.isCreatable(value)){
                    spec.and(ProductSpecifications.priceLesserOrEqualsThan(new BigDecimal(value)));
                }
                break;
            case ("title"):
                spec.and(ProductSpecifications.titleLike(value));
                break;
        }

    }
}
