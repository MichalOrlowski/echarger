package eu.company.echarger.core.pricing;

import eu.company.echarger.common.ApplicationException;

public class PriceDefinitionNotFoundException extends ApplicationException {

    public PriceDefinitionNotFoundException() {
        super("Price Definition not found.");
    }
}
