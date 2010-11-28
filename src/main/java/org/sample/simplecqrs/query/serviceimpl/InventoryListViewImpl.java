package org.sample.simplecqrs.query.serviceimpl;

import org.fornax.cartridges.sculptor.framework.event.DynamicMethodDispatcher;
import org.fornax.cartridges.sculptor.framework.event.Event;
import org.sample.simplecqrs.command.domain.InventoryItemCreated;
import org.sample.simplecqrs.command.domain.InventoryItemDeactivated;
import org.sample.simplecqrs.command.domain.InventoryItemEvent;
import org.sample.simplecqrs.command.domain.InventoryItemRenamed;
import org.sample.simplecqrs.query.domain.InventoryItemList;
import org.sample.simplecqrs.query.exception.InventoryItemListNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of InventoryListView.
 */
@Service("inventoryListView")
public class InventoryListViewImpl extends InventoryListViewImplBase {
    public InventoryListViewImpl() {
    }

    @Override
    public void receive(Event event) {
        DynamicMethodDispatcher.dispatch(this, event, "handle");
    }

    public void handle(InventoryItemCreated event) {
        getInventoryItemListRepository().save(
                new InventoryItemList(event.getItemId(), event.getName()));
    }

    public void handle(InventoryItemRenamed event) {
        InventoryItemList item = tryGetItem(event.getItemId());
        item.setName(event.getNewName());
        getInventoryItemListRepository().save(item);
    }

    public void handle(InventoryItemDeactivated event) {
        InventoryItemList item = tryGetItem(event.getItemId());
        getInventoryItemListRepository().delete(item);
    }

    public void handle(InventoryItemEvent other) {
        // not interested
    }

    private InventoryItemList tryGetItem(String itemId) {
        try {
            return getInventoryItemListRepository().findByKey(itemId);
        } catch (InventoryItemListNotFoundException e) {
            throw new IllegalStateException("Unknown item: " + itemId);
        }
    }
}
