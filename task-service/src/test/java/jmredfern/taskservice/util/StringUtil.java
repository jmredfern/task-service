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

import java.io.IOException;
import java.nio.file.Files;
import org.springframework.core.io.Resource;

/**
 *
 * @author Jim Redfern
 */
public class StringUtil {
    
    private StringUtil() {
        
    }    
    
    public static String resourceToString(Resource resource) throws IOException {
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }
   
}
