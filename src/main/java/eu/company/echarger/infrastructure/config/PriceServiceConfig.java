package eu.company.echarger.infrastructure.config;

import eu.company.echarger.core.pricing.CustomerRepository;
import eu.company.echarger.core.pricing.PriceRepository;
import eu.company.echarger.core.pricing.PriceService;
import eu.company.echarger.infrastructure.dataproviders.pricing.CustomerRepositoryImpl;
import eu.company.echarger.infrastructure.dataproviders.pricing.PriceRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PriceServiceConfig {

    @Bean
    PriceRepository priceRepository() {
        return new PriceRepositoryImpl();
    }

    @Bean
    CustomerRepository customerRepository() {
        return new CustomerRepositoryImpl();
    }

    @Bean
    PriceService priceService(PriceRepository priceRepository, CustomerRepository customerRepository) {
        return new PriceService(priceRepository, customerRepository);
    }

}
