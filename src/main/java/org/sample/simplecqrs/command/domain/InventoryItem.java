package org.sample.simplecqrs.command.domain;

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
        super(itemId);
        setActivated(true);
    }

    public void deactivate() {
        setActivated(false);
    }

    public void checkIn(int some) {
        setCount(getCount() + some);
    }

    public void remove(int some) {
        setCount(getCount() - some);
    }
}
