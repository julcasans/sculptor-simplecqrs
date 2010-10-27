package org.sample.simplecqrs.command.serviceimpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fornax.cartridges.sculptor.framework.event.Event;
import org.sample.simplecqrs.command.domain.InventoryItem;
import org.sample.simplecqrs.command.domain.InventoryItemEvent;
import org.sample.simplecqrs.command.domain.InventoryItemSnapshot;
import org.sample.simplecqrs.command.exception.InventoryItemNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryItemSnapshotter.
 */
@Service("inventoryItemSnapshotter")
public class InventoryItemSnapshotterImpl extends
        InventoryItemSnapshotterImplBase {

    private static final int VERSION_DELTA = 100;
    private final Log log = LogFactory.getLog(getClass());

    public InventoryItemSnapshotterImpl() {
    }

    @Override
    public void receive(Event event) {
        if (!(event instanceof InventoryItemEvent)) {
            return;
        }

        InventoryItemEvent inventoryItemEvent = (InventoryItemEvent) event;
        String itemId = inventoryItemEvent.getItemId();

        InventoryItemSnapshot snapshot = getInventoryItemSnapshotRepository()
                .getLatestSnapshot(itemId);
        long snapshotVersion = snapshot == null ? 1 : snapshot.getVersion();
        long eventVersion = inventoryItemEvent.getAggregateVersion() == null ? 1
                : inventoryItemEvent.getAggregateVersion();
        if (eventVersion - snapshotVersion >= VERSION_DELTA) {
            takeSnapshot(itemId);
        }
    }

    private void takeSnapshot(String itemId) {
        InventoryItem item;
        try {
            item = getInventoryItemRepository().findByKey(itemId);
        } catch (InventoryItemNotFoundException e) {
            log.warn("takeSnapshot failed: " + e.getMessage());
            return;
        }

        InventoryItemSnapshot snapshot = item.createSnapshot();
        getInventoryItemSnapshotRepository().save(snapshot);
    }

}
