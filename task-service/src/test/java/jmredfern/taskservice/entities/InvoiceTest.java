/*
 * Copyright 2015 Jim Redfern.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jmredfern.taskservice.entities;

import java.util.Random;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Jim Redfern
 */
public class InvoiceTest {
    private static final long serialVersionUID = 1109023542628949923L;
        
    Invoice testInvoice;
    
    @Test 
    public void emptyConstructor() {
        testInvoice = new Invoice();
    }
    
    @Test 
    public void constructor() {
        Long id = new Random().nextLong();
        testInvoice = new Invoice(id);
        Long returnedId = testInvoice.getId();
        Assert.assertEquals(id, returnedId);
    }
    
    @Test
    public void setGetPurchaseOrder() {
        testInvoice = new Invoice();
        String po = UUID.randomUUID().toString();
        testInvoice.setPurchaseOrder(po);
        String returnedPo = testInvoice.getPurchaseOrder();
        Assert.assertEquals(po, returnedPo);
    }      
    
    @Test
    public void getType() {
        testInvoice = new Invoice();
        Assert.assertEquals("Invoice", testInvoice.getType());
    }
    
}
