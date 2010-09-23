package org.sample.simplecqrs.command.serviceapi;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.event.Event;
import org.fornax.cartridges.sculptor.framework.event.EventBus;
import org.fornax.cartridges.sculptor.framework.event.EventSubscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sample.simplecqrs.command.domain.InventoryItemRenamed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.context.ContextConfiguration;

/**
 * Spring based test with MongoDB.
 */
@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class InventoryItemEventPublisherTest extends AbstractDependencyInjectionSpringContextTests implements
        InventoryItemEventPublisherTestBase, EventSubscriber {
    @Autowired
    private InventoryItemEventPublisher inventoryItemEventPublisher;
    @Autowired
    @Qualifier("eventBus")
    private EventBus eventBus;
    private Event eventReceived;

    @Before
    public void setUpSubscriber() {
        eventReceived = null;
        eventBus.subscribe("inventoryItemTopic", this);
    }

    @After
    public void tearDownSubscriber() {
        eventReceived = null;
        eventBus.unsubscribe("inventoryItemTopic", this);
    }

    @Test
    public void testPublishEvent() throws Exception {

        InventoryItemRenamed event = new InventoryItemRenamed(new Date(), "6677", "Something else");
        inventoryItemEventPublisher.publishEvent(event);
        assertEquals(event, eventReceived);
    }

    public void receive(Event event) {
        eventReceived = event;
    }
}
