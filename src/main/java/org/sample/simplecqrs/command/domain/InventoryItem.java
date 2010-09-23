package org.sample.simplecqrs.command.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.event.DynamicMethodDispatcher;

/**
 * Entity representing InventoryItem.
 * <p>
 * This class is responsible for the domain object related business logic for
 * InventoryItem. Properties and associations are implemented in the generated
 * base class {@link org.sample.simplecqrs.command.domain.InventoryItemBase}.
 */
public class InventoryItem extends InventoryItemBase {
    private static final long serialVersionUID = 1L;

    protected InventoryItem() {
    }

    public InventoryItem(String itemId) {
        this(itemId, "", false);
    }

    // TODO ugly with this isNew flag, but when obj are created from mapper no
    // event should be created
    public InventoryItem(String itemId, String name, boolean isNew) {
        super(itemId);
        if (isNew) {
            applyChange(new InventoryItemCreated(new Date(), itemId, name));
        }
    }

    public void rename(String newName) {
        if (StringUtils.isEmpty(newName))
            throw new IllegalArgumentException("newName");
        applyChange(new InventoryItemRenamed(new Date(), getItemId(), newName));
    }

    public void deactivate() {
        if (!isActivated())
            throw new IllegalStateException("already deactivated");
        applyChange(new InventoryItemDeactivated(new Date(), getItemId()));
    }

    public void checkIn(int some) {
        if (some <= 0)
            throw new IllegalArgumentException("must have a count greater than 0 to add to inventory");
        applyChange(new ItemsCheckedInToInventory(new Date(), getItemId(), some));
    }

    public void remove(int some) {
        if (some <= 0)
            throw new IllegalArgumentException("cant remove negative count from inventory");
        applyChange(new ItemsRemovedFromInventory(new Date(), getItemId(), some));
    }

    public void apply(InventoryItemCreated event) {
        setActivated(true);
    }

    public void apply(InventoryItemDeactivated event) {
        setActivated(false);
    }

    public void apply(Object other) {
        // ignore
    }

    private final List<InventoryItemEvent> changes = new ArrayList<InventoryItemEvent>();

    public List<InventoryItemEvent> getUncommittedChanges() {
        return changes;
    }

    public void markChangesAsCommitted() {
        changes.clear();
    }

    private void applyChange(InventoryItemEvent event, boolean isNew) {
        DynamicMethodDispatcher.dispatch(this, event, "apply");
        if (isNew) {
            changes.add(event);
        } else {
            setVersion(event.getVersion());
        }
    }

    private void applyChange(InventoryItemEvent event) {
        applyChange(event, true);
    }

    public void loadFromHistory(List<InventoryItemEvent> history) {
        for (InventoryItemEvent each : history) {
            applyChange(each, false);
        }
    }

    public void applySnapshot(InventoryItemSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        setActivated(snapshot.isActivated());
        setVersion(snapshot.getVersion());
    }

    public InventoryItemSnapshot createSnapshot() {
        return new InventoryItemSnapshot(getItemId(), isActivated(), getVersion());
    }

}
