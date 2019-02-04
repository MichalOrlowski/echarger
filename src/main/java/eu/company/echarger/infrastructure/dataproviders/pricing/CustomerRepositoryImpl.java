package eu.company.echarger.infrastructure.dataproviders.pricing;

import eu.company.echarger.core.pricing.CustomerRepository;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public boolean isVip(Integer customerId) {
        return customerId % 2 == 0;
    }
}
