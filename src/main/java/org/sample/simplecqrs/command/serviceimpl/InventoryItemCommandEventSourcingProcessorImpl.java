package org.sample.simplecqrs.command.serviceimpl;

import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryItemCommandEventSourcingProcessor.
 */
@Service("inventoryItemCommandEventSourcingProcessor")
public class InventoryItemCommandEventSourcingProcessorImpl
    extends InventoryItemCommandEventSourcingProcessorImplBase {
    public InventoryItemCommandEventSourcingProcessorImpl() {
    }
}
