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
import jmredfern.taskservice.entities.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Transactional Dao for {@code Invoice} objects which uses JPA entityManager.
 * 
 * @author Jim Redfern
 */
@Service
public class InvoiceDao {
   
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceDao.class);
    
    private static final String METHOD_GET_ALL = "getAll";
    private static final String METHOD_GET = "get";
    private static final String METHOD_ADD = "add";
    private static final String METHOD_UPDATE = "update";
    
    @PersistenceContext(type=PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
    
    /**
     * Returns all {@code Invoice}s in the data source.
     * 
     * @return {@code List<Invoice>}
     */
    public List<Invoice> getAll() {
        LOG.trace(METHOD_GET_ALL + "()");
        
        TypedQuery<Invoice> typedQuery = entityManager.createQuery("SELECT i FROM Invoice i", Invoice.class);
        return typedQuery.getResultList();
    }

    /**
     * Returns the {@code Invoice} with {@code invoiceId} from in the data source.
     * 
     * @param invoiceId
     * @return {@code Invoice}
     */
    public Invoice get(Long invoiceId) {
        LOG.trace(METHOD_GET + "(" + invoiceId + ")");
        
        return entityManager.find(Invoice.class, invoiceId);
    }  
    
    /**
     * Adds the {@code Invoice} to the data source.
     * 
     * @param invoice 
     */
    @Transactional
    public void add(Invoice invoice) {
        LOG.trace(METHOD_ADD + "(" + invoice + ")");
        
        entityManager.persist(invoice);
        entityManager.flush();
    }
    
    /**
     * Updates the {@code Invoice} in the data source.
     * 
     * @param invoice 
     */
    @Transactional
    public void update(Invoice invoice) {
        LOG.trace(METHOD_UPDATE + "(" + invoice + ")");
        
        entityManager.merge(invoice);
        entityManager.flush();
    }

}
