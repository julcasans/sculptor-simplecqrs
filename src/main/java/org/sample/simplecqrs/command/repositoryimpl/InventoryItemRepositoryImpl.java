package org.sample.simplecqrs.command.repositoryimpl;

import java.util.List;

import org.sample.simplecqrs.command.domain.InventoryItem;
import org.sample.simplecqrs.command.domain.InventoryItemEvent;
import org.sample.simplecqrs.command.exception.InventoryItemNotFoundException;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for InventoryItem
 */
@Repository("inventoryItemRepository")
public class InventoryItemRepositoryImpl extends InventoryItemRepositoryBase {

    public InventoryItemRepositoryImpl() {
    }

    @Override
    public InventoryItem save(InventoryItem entity) {
        InventoryItem saved = super.save(entity);

        List<InventoryItemEvent> changes = entity.getUncommittedChanges();
        assignVersionToChanges(changes, saved.getVersion());
        for (InventoryItemEvent each : changes) {
            getInventoryItemEventRepository().save(each);
        }
        entity.markChangesAsCommitted();

        return saved;
    }

    private void assignVersionToChanges(List<InventoryItemEvent> changes, long version) {
        long sequence = version * 1000;
        for (InventoryItemEvent each : changes) {
            each.setVersion(version);
            each.setSeq(sequence);
            sequence++;
        }
    }

    @Override
    public InventoryItem findByKey(String itemId) throws InventoryItemNotFoundException {
        InventoryItem result = super.findByKey(itemId);

        loadFromHistory(result);

        return result;
    }

    private void loadFromHistory(InventoryItem entity) {
        List<InventoryItemEvent> history = getInventoryItemEventRepository().findAllForItem(entity.getItemId());
        entity.loadFromHistory(history);
    }

}
