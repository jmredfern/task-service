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
import jmredfern.taskservice.entities.W9;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Transactional Dao for {@code W9} objects which uses JPA entityManager.
 * 
 * @author Jim Redfern
 */
@Service
public class W9Dao {
    
    private static final Logger LOG = LoggerFactory.getLogger(W9Dao.class);

    private static final String METHOD_GET_ALL = "getAll";
    private static final String METHOD_GET = "get";
    private static final String METHOD_ADD = "add";
    private static final String METHOD_UPDATE = "update";
    
    @PersistenceContext(type=PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
    
    /**
     * Returns all {@code W9}s in the data source.
     * 
     * @return {@code List<W9>}
     */
    @Transactional
    public List<W9> getAll() {
        LOG.trace(METHOD_GET_ALL + "()");
        
        TypedQuery<W9> typedQuery = entityManager.createQuery("SELECT w FROM W9 w", W9.class);
        return typedQuery.getResultList();
    }
    
    /**
     * Returns the {@code W9} with {@code w9Id} from in the data source.
     * 
     * @param w9Id
     * @return {@code W9}
     */
    public W9 get(Long w9Id) {
        LOG.trace(METHOD_GET + "(" + w9Id + ")");
        
        return entityManager.find(W9.class, w9Id);
    }    
        
    /**
     * Adds the {@code User} to the data source.
     * 
     * @param w9
     */
    @Transactional
    public void add(W9 w9) {
        LOG.trace(METHOD_ADD + "(" + w9 + ")");
        
        entityManager.persist(w9);
        entityManager.flush();
    }
    
    /**
     * Updates the {@code W9} in the data source. 
     * @param w9
     */
    @Transactional
    public void update(W9 w9) {
        LOG.trace(METHOD_UPDATE + "(" + w9 + ")");
        
        entityManager.merge(w9);
        entityManager.flush();
    }
    
}
