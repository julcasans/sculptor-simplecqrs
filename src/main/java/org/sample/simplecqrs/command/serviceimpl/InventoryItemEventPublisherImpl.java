package org.sample.simplecqrs.command.serviceimpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fornax.cartridges.sculptor.framework.event.annotation.Publish;
import org.sample.simplecqrs.command.domain.InventoryItemEvent;
import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryItemEventPublisher.
 */
@Service("inventoryItemEventPublisher")
public class InventoryItemEventPublisherImpl extends InventoryItemEventPublisherImplBase {
    private final Log log = LogFactory.getLog(getClass());

    public InventoryItemEventPublisherImpl() {
    }

    @Publish(topic = "inventoryItemTopic")
    public void publishEvent(InventoryItemEvent event) {
        log.info("Published: " + event);
    }
}
