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
public class W9Test {
    private static final long serialVersionUID = 1109023542628949923L;
        
    W9 testW9;
    
    @Test 
    public void emptyConstructor() {
        testW9 = new W9();
    }
    
    @Test 
    public void constructor() {
        Long id = new Random().nextLong();
        testW9 = new W9(id);
        Long returnedId = testW9.getId();
        Assert.assertEquals(id, returnedId);
    }
    
    @Test
    public void setGetTaxPayerIdentificationNumber() {
        testW9 = new W9();
        String TPIN = UUID.randomUUID().toString();
        testW9.setTaxPayerIdentificationNumber(TPIN);
        String returnedTPIN = testW9.getTaxPayerIdentificationNumber();
        Assert.assertEquals(TPIN, returnedTPIN);
    }      
    
    @Test
    public void getType() {
        testW9 = new W9();
        Assert.assertEquals("W9", testW9.getType());
    }
    
}
