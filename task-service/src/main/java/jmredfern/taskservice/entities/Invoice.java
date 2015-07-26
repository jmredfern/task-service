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

import java.io.Serializable;
import javax.persistence.Entity;
import jmredfern.taskservice.util.ToStringUtil;

/**
 * Entity for Invoice, which is a Platform Object. Invoices have an id and a purchase order.
 * 
 * @author Jim Redfern
 */
@Entity
public class Invoice extends PlatformObject implements Serializable {
    private static final long serialVersionUID = 1109023542628949923L;
    
    private String purchaseOrder;

    public Invoice() {
    }   
    
    /**
     * Creates an Invoice with PlatformObject id {@code id}
     * 
     * @param id 
     */
    public Invoice(Long id) {
        super(id);
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }     
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }        
        if (!(this.getClass() == object.getClass())) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.getId() == null && other.getId() != null) 
                || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        if (this.getId() == null && other.getId() == null) {
            return this == other;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("Invoice[");
        ToStringUtil.addAttribute(text, " id=", getId());
        ToStringUtil.addAttribute(text, " po=", getPurchaseOrder());
        text.append("]");

        return text.toString();        
    }
    
}
