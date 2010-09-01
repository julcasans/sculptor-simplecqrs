package org.sample.simplecqrs.command.serviceapi;

import java.util.Set;

import org.fornax.cartridges.sculptor.framework.accessimpl.mongodb.DbManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.context.ContextConfiguration;

/**
 * Spring based test with MongoDB.
 */
@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@Ignore
public class InventoryItemCommandEventSourcingProcessorTest extends AbstractDependencyInjectionSpringContextTests
        implements InventoryItemCommandEventSourcingProcessorTestBase {
    @Autowired
    private DbManager dbManager;
    @Autowired
    private InventoryItemCommandEventSourcingProcessor inventoryItemCommandEventSourcingProcessor;

    @Before
    public void initTestData() {
    }

    @Before
    public void initDbManagerThreadInstance() throws Exception {
        // to be able to do lazy loading of associations inside test class
        DbManager.setThreadInstance(dbManager);
    }

    @After
    public void dropDatabase() {
        Set<String> names = dbManager.getDB().getCollectionNames();
        for (String each : names) {
            if (!each.startsWith("system")) {
                dbManager.getDB().getCollection(each).drop();
            }
        }

        // dbManager.getDB().dropDatabase();
    }

    private int countRowsInDBCollection(String name) {
        return (int) dbManager.getDBCollection(name).getCount();
    }

    @Test
    public void testReceive() throws Exception {
        // TODO Auto-generated method stub
    }

    @Test
    public void testApply() throws Exception {
        // TODO Auto-generated method stub
    }

    @Test
    public void testGetAllEvents() throws Exception {
        // TODO Auto-generated method stub
    }

    @Test
    public void testSave() throws Exception {
        // TODO Auto-generated method stub
    }
}
