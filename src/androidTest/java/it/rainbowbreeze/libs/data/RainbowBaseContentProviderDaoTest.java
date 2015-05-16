/**
 * Copyright (C) 2012 Alfredo Morresi
 * 
 * This file is part of RainbowLibs project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.rainbowbreeze.libs.data;

import android.content.ContentProvider;
import android.content.Context;
import android.test.ProviderTestCase2;

/**
 * Base class to test a particular {@link RainbowBaseContentProviderDaoTest}
 * implementation.
 * Performs some generic CRUD tests, then can be extended with custom tests.
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public abstract class RainbowBaseContentProviderDaoTest<
        Provider extends ContentProvider, 
        Dao extends RainbowBaseContentProviderDao<Item>,
        Item extends RainbowSettableId>
extends ProviderTestCase2<Provider>
{
    // ------------------------------------------ Private Fields
    /** mock context to user */
    protected Context mContext;
    /** DAO to test*/
    protected Dao mDaoToTest;
    
    // -------------------------------------------- Constructors
    public RainbowBaseContentProviderDaoTest(Class<Provider> ProviderClass, String providerAuthority) {
        super(ProviderClass, providerAuthority);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mContext = getMockContext(getContext());
        assertNotNull("Context is null", mContext);
        if (initEnvironment(mContext)) {
        }
        mDaoToTest = getDaoIntance(mContext);
        mDaoToTest.setContentResolver(getMockContentResolver());
        mDaoToTest.deleteAllItems(mContext);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDaoToTest.deleteAllItems(mContext);
    }
    
    
    // --------------------------------------------------- Tests
    
    /**
     * Basic CRUD test
     */
    public void testInsertGetUpdateAndDeleteItem() {
        Item actualItem;
        int affected;
        
        checkNotExistingItems();
        
        //inserts first item
        Item item1 = createTestItem1();
        long itemId1 = insertAndVerifyItem(item1);
    
        //compares first item
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        compareItem(item1, actualItem);
        checkNotExistingItems();
        
        //inserts second item
        Item item2 = createTestItem2();
        long itemId2 = insertAndVerifyItem(item2);
        //compares first item
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        compareItem(item1, actualItem);
        //compares second item
        actualItem = mDaoToTest.getItem(mContext, itemId2);
        compareItem(item2, actualItem);
        checkNotExistingItems();
        
        //inserts third item
        Item item3 = createTestItem3();
        long itemId3 = insertAndVerifyItem(item3);
        //compares first item
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        compareItem(item1, actualItem);
        //compares second item
        actualItem = mDaoToTest.getItem(mContext, itemId2);
        compareItem(item2, actualItem);
        //compares third item
        actualItem = mDaoToTest.getItem(mContext, itemId3);
        compareItem(item3, actualItem);
        checkNotExistingItems();
        
        //deletes not existing item
        affected = mDaoToTest.deleteItem(mContext, 234234);
        assertEquals("Wrong affected items", 0, affected);
        affected = mDaoToTest.deleteItem(mContext, -3024);
        assertEquals("Wrong affected items", 0, affected);
        affected = mDaoToTest.deleteItem(mContext, RainbowBaseContentProviderDao.NOT_FOUND);
        assertEquals("Wrong affected items", 0, affected);
        
        //deletes first item
        deleteAndVerifyItem(itemId1);
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        assertNull("Item not null", actualItem);
        //compares second item
        actualItem = mDaoToTest.getItem(mContext, itemId2);
        compareItem(item2, actualItem);
        //compares third item
        actualItem = mDaoToTest.getItem(mContext, itemId3);
        compareItem(item3, actualItem);
        checkNotExistingItems();
        
        //deletes second item
        deleteAndVerifyItem(itemId2);
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        assertNull("Item not null", actualItem);
        actualItem = mDaoToTest.getItem(mContext, itemId2);
        assertNull("Item not null", actualItem);
        //compare third item
        actualItem = mDaoToTest.getItem(mContext, itemId3);
        compareItem(item3, actualItem);
        checkNotExistingItems();
        
        //updates not-existing items
        affected = mDaoToTest.updateItem(mContext, item1);
        assertEquals("Wrong affected items", 0, affected);
        affected = mDaoToTest.updateItem(mContext, item2);
        assertEquals("Wrong affected items", 0, affected);
        
        //re-add first item
        itemId1 = insertAndVerifyItem(item1);
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        compareItem(item1, actualItem);
        
        //update first item
        item1 = createTestItem2();
        item1.setId(itemId1);
        updateAndVerifyItem(item1);
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        compareItem(item1, actualItem);
        actualItem = mDaoToTest.getItem(mContext, itemId3);
        compareItem(item3, actualItem);
        
        //update third item
        item3 = createTestItem1();
        item3.setId(itemId3);
        updateAndVerifyItem(item3);
        actualItem = mDaoToTest.getItem(mContext, itemId3);
        compareItem(item3, actualItem);
        actualItem = mDaoToTest.getItem(mContext, itemId1);
        compareItem(item1, actualItem);
    }
    
    /**
     * Test {@link RainbowBaseContentProviderDao#deleteAllItems(Context)} method 
     */
    public void testDeleteAllItems() {
        insertAndVerifyItem(createTestItem1());
        insertAndVerifyItem(createTestItem2());
        insertAndVerifyItem(createTestItem3());
        
        assertEquals("Wrong item count", 3, mDaoToTest.getAllItemsCount(mContext));
        mDaoToTest.deleteAllItems(mContext);
        assertEquals("Wrong item count", 0, mDaoToTest.getAllItemsCount(mContext));
    }
    
    /**
     * Tests {@link RainbowBaseContentProviderDao#getAllItemsCount(Context)} method
     */
    public void testAllItemsCount() {
        assertEquals("Wrong item count", 0, mDaoToTest.getAllItemsCount(mContext));
        long itemId1 = insertAndVerifyItem(createTestItem1());
        assertEquals("Wrong item count", 1, mDaoToTest.getAllItemsCount(mContext));
        insertAndVerifyItem(createTestItem2());
        assertEquals("Wrong item count", 2, mDaoToTest.getAllItemsCount(mContext));
        long itemId3 = insertAndVerifyItem(createTestItem3());
        assertEquals("Wrong item count", 3, mDaoToTest.getAllItemsCount(mContext));
        deleteAndVerifyItem(itemId3);
        assertEquals("Wrong item count", 2, mDaoToTest.getAllItemsCount(mContext));
        Item updatedItem1 = createTestItem3();
        updatedItem1.setId(itemId1);
        updateAndVerifyItem(updatedItem1);
        assertEquals("Wrong item count", 2, mDaoToTest.getAllItemsCount(mContext));
    }
    
    // ----------------------------------------- Private Methods
    /**
     * Returns a mock contect to use as content provider context
     * @param context
     * @return
     */
    protected abstract Context getMockContext(Context context);

    /**
     * Initializes the environment (load objects, set particular data etc).
     * Called during the {@link #setUp()} method
     * @param context
     * @return true if the environment has been initialized, false if
     *         initialization wasn't necessary
     */
    protected abstract boolean initEnvironment(Context context);

    /**
     * Returns the DAO to use during tests
     */
    protected abstract Dao getDaoIntance(Context context);
    
    /**
     * Returns a first item used for tests
     */
    protected abstract Item createTestItem1(); 
    
    /**
     * Returns a second item used for tests
     */
    protected abstract Item createTestItem2(); 
    
    /**
     * Returns a third item used for tests
     */
    protected abstract Item createTestItem3();
    
    /**
     * Compare two items each other
     * @param expected
     * @param actual
     */
    protected abstract void compareItem(Item expected, Item actual);
    
    /**
     * Adds and verifies a new pot statistics
     * 
     * @return id of the new pot
     */
    protected long insertAndVerifyItem(Item itemToAdd) {
        assertNotNull("Null item", itemToAdd);
        long itemId = mDaoToTest.insertItem(mContext, itemToAdd);
        assertTrue("Wrong id", itemId > 0);
        return itemId;
    }
    
    protected void updateAndVerifyItem(Item itemToUpdate) {
        assertNotNull("Null item", itemToUpdate);
        int affected = mDaoToTest.updateItem(mContext, itemToUpdate);
        assertEquals("Wrong affected during delete", 1, affected);
    }
    
    protected void deleteAndVerifyItem(long itemId) {
        assertTrue("Invalid item id", itemId > 0);
        int affected = mDaoToTest.deleteItem(mContext, itemId);
        assertEquals("Wrong affected during delete", 1, affected);
    }
    
    /**
     * Gets not existing values
     */
    protected void checkNotExistingItems() {
        Item actualItem;
        //non existing items
        actualItem = mDaoToTest.getItem(mContext, -123423);
        assertNull("Item not null", actualItem);
        actualItem = mDaoToTest.getItem(mContext, RainbowBaseContentProviderDao.NOT_FOUND);
        assertNull("Item not null", actualItem);
        actualItem = mDaoToTest.getItem(mContext, 345823);
        assertNull("Item not null", actualItem);
    }

}