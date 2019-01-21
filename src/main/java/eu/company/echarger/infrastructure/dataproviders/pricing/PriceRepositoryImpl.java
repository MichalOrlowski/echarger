package eu.company.echarger.infrastructure.dataproviders.pricing;

import eu.company.echarger.core.pricing.PriceDefinition;
import eu.company.echarger.core.pricing.PriceRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PriceRepositoryImpl implements PriceRepository {

    private final Map<Integer, PriceDefinition> priceDefinitions = new HashMap<>();
    private Integer sequence = 0;

    @Override
    public Integer save(PriceDefinition priceDefinition) {
        priceDefinitions.put(++sequence, priceDefinition);
        return sequence;
    }

    @Override
    public List<PriceDefinition> findAll() {
        return new ArrayList<>(priceDefinitions.values());
    }

    @Override
    public Optional<PriceDefinition> findById(Integer id) {
        return Optional.ofNullable(priceDefinitions.get(id));
    }

    @Override
    public void clearAll() {
        priceDefinitions.clear();
        sequence = 0;
    }
}
