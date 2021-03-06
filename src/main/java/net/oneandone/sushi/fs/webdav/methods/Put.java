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
package net.oneandone.sushi.fs.webdav.methods;

import net.oneandone.sushi.fs.webdav.StatusException;
import net.oneandone.sushi.fs.webdav.WebdavConnection;
import net.oneandone.sushi.fs.webdav.WebdavNode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.io.IOException;

public class Put extends Method<Void> {
    public Put(WebdavNode resource) {
        super("PUT", resource);
    }

    @Override
    protected void setContentHeader() {
        setRequestHeader(HTTP.TRANSFER_ENCODING, HTTP.CHUNK_CODING);
    }
    
    @Override
    public Void processResponse(WebdavConnection connection, HttpResponse response) throws IOException {
    	int status;
    	
    	status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_NO_CONTENT && status != HttpStatus.SC_CREATED) {
        	throw new StatusException(response.getStatusLine());
        }
        return null;
    }
}