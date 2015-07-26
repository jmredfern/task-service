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
package jmredfern.taskservice.util;

/**
 * Utility class for ToString building.
 * 
 * @author Jim Redfern
 */
public class ToStringUtil {
    
    private ToStringUtil() {
        
    }
    
    /**
     * Adds an attribute to the StringBuilder {@code text}. If the {@attributeValue} is {@code null} the attribute will
     * not be added
     * 
     * @param text
     * @param attributeName
     * @param attributeValue 
     */
    public static void addAttribute(StringBuilder text, String attributeName, Object attributeValue) {
        if (attributeValue != null) {
            text.append(attributeName).append(attributeValue.toString());
        }
    }   
   
}
