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
public class UserDaoTest {
    private static final long serialVersionUID = 1109023542628949923L;
        
    UserDao testUserDao;
    
    @Injectable EntityManager entityManager;
    
    @Before
    public void init() {
        testUserDao = new UserDao();
        
        Deencapsulation.setField(testUserDao, "entityManager", entityManager);
    }
    
    @Test
    public void get() {
        User user = new User();
        Long userId = new Random().nextLong();
        new Expectations() {{
            entityManager.find(User.class, userId); result = user; times = 1;
        }};
        User resultUser = testUserDao.get(userId);
        Assert.assertEquals(user, resultUser);
    }      
    
    @Test
    public void add() {
        User user = new User();
        new Expectations() {{
            entityManager.persist(user); times = 1;
        }};        
        testUserDao.add(user);
    }
    
    @Test
    public void update() {
        User user = new User();
        new Expectations() {{
            entityManager.merge(user); times = 1;
        }};        
        testUserDao.update(user);
    }
    
}
