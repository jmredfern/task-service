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

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import jmredfern.taskservice.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Transactional Dao for {@code User} objects which uses JPA entityManager.
 * 
 * @author Jim Redfern
 */
@Service
public class UserDao {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

    private static final String METHOD_GET_ALL = "getAll";
    private static final String METHOD_GET = "get";
    private static final String METHOD_ADD = "add";
    private static final String METHOD_UPDATE = "update";
    
    @PersistenceContext(type=PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
    
    /**
     * Returns all {@code User}s in the data source.
     * 
     * @return {@code List<User>}
     */
    public List<User> getAll() {
        LOG.trace(METHOD_GET_ALL + "()");
        
        TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM \"User\" u", User.class);
        return typedQuery.getResultList();
    }
    
    /**
     * Returns the {@code User} with {@code userId} from in the data source.
     * 
     * @param userId
     * @return {@code User}
     */
    public User get(Long userId) {
        LOG.trace(METHOD_GET + "(" + userId + ")");
        
        return entityManager.find(User.class, userId);
    }    
    
    /**
     * Adds the {@code User} to the data source.
     * 
     * @param user
     */
    @Transactional
    public void add(User user) {
        LOG.trace(METHOD_ADD + "(" + user + ")");
        
        entityManager.persist(user);
        entityManager.flush();
    }
        
    /**
     * Updates the {@code User} in the data source.
     * @param user
     */
    @Transactional
    public void update(User user) {
        LOG.trace(METHOD_UPDATE + "(" + user + ")");
        
        entityManager.merge(user);
        entityManager.flush();
    }

}
