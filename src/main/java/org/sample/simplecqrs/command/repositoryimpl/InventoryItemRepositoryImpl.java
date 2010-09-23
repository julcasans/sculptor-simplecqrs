package org.sample.simplecqrs.command.repositoryimpl;

import java.util.List;

import org.sample.simplecqrs.command.domain.InventoryItem;
import org.sample.simplecqrs.command.domain.InventoryItemEvent;
import org.sample.simplecqrs.command.domain.InventoryItemSnapshot;
import org.sample.simplecqrs.command.exception.InventoryItemNotFoundException;
import org.sample.simplecqrs.command.serviceapi.InventoryItemEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for InventoryItem
 */
@Repository("inventoryItemRepository")
public class InventoryItemRepositoryImpl extends InventoryItemRepositoryBase {

    @Autowired
    private InventoryItemEventPublisher inventoryItemEventPublisher;

    public InventoryItemRepositoryImpl() {
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        InventoryItem saved = super.save(item);

        List<InventoryItemEvent> changes = item.getUncommittedChanges();
        long version = saved.getVersion();
        long sequence = version * 1000;
        for (InventoryItemEvent each : changes) {
            each.setVersion(version);
            each.setSeq(sequence);
            sequence++;
        }
        getInventoryItemEventRepository().save(changes);

        for (InventoryItemEvent each : changes) {
            inventoryItemEventPublisher.publishEvent(each);
        }

        item.markChangesAsCommitted();

        return item;
    }

    @Override
    public InventoryItem findByKey(String itemId) throws InventoryItemNotFoundException {
        InventoryItem result = super.findByKey(itemId);

        InventoryItemSnapshot snapshot = getInventoryItemSnapshotRepository().getLatestSnapshot(itemId);
        result.applySnapshot(snapshot);
        long snapshotVersion = snapshot == null ? 0 : snapshot.getVersion();

        List<InventoryItemEvent> history = getInventoryItemEventRepository().findAllAfter(itemId, snapshotVersion);
        result.loadFromHistory(history);

        return result;
    }

}
