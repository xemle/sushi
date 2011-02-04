/*
 * Copyright 1&1 Internet AG, http://www.1and1.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oneandone.sushi.fs.webdav.methods;

import com.oneandone.sushi.fs.webdav.StatusException;
import com.oneandone.sushi.fs.webdav.WebdavConnection;
import com.oneandone.sushi.fs.webdav.WebdavNode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.IOException;


public class MkCol extends Method<Void> {
    public MkCol(WebdavNode resource) {
        super("MKCOL", resource);
    }

    @Override
    public Void processResponse(WebdavConnection conection, HttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
        	throw new StatusException(response.getStatusLine());
        }
        return null;
    }
}