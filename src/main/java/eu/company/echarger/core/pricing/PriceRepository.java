package eu.company.echarger.core.pricing;

import java.util.List;
import java.util.Optional;

public interface PriceRepository {

    Integer save(PriceDefinition priceDefinition);

    List<PriceDefinition> findAll();

    Optional<PriceDefinition> findById(Integer id);

    void clearAll();
}
