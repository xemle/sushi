/**
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oneandone.sushi.metadata.xml;

import java.io.IOException;

public abstract class Tree {
    public abstract Object done() throws IOException;
    
    public abstract void ref(String name, int idref) throws IOException;
    public abstract void begin(String name, int id, String typeAttribute, boolean withEnd) throws IOException;
    public abstract void end(String name) throws IOException;
    public abstract void text(String name, String typeAttribute, String text) throws IOException;
}