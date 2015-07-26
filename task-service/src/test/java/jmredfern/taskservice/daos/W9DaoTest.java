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
package jmredfern.taskservice.daos;

import jmredfern.taskservice.entities.*;
import java.util.Random;
import javax.persistence.EntityManager;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jim Redfern
 */
public class W9DaoTest {
    private static final long serialVersionUID = 1109023542628949923L;
        
    W9Dao testW9Dao;
    
    @Injectable EntityManager entityManager;
    
    @Before
    public void init() {
        testW9Dao = new W9Dao();
        
        Deencapsulation.setField(testW9Dao, "entityManager", entityManager);
    }
    
    @Test
    public void get() {
        W9 w9 = new W9();
        Long w9Id = new Random().nextLong();
        new Expectations() {{
            entityManager.find(W9.class, w9Id); result = w9; times = 1;
        }};
        W9 resultW9 = testW9Dao.get(w9Id);
        Assert.assertEquals(w9, resultW9);
    }      
    
    @Test
    public void add() {
        W9 w9 = new W9();
        new Expectations() {{
            entityManager.persist(w9); times = 1;
        }};        
        testW9Dao.add(w9);
    }
    
    @Test
    public void update() {
        W9 w9 = new W9();
        new Expectations() {{
            entityManager.merge(w9); times = 1;
        }};        
        testW9Dao.update(w9);
    }
    
}
