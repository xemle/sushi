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

package net.sf.beezle.sushi.fs;

import net.sf.beezle.sushi.io.Buffer;
import net.sf.beezle.sushi.io.OS;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public abstract class NodeReadOnlyTest<T extends Node> {
    protected static final World WORLD = new World(OS.CURRENT, new Settings(), new Buffer(), "**/.svn/**/*").addStandardFilesystems();

    /** creates a new empty directory */
    protected abstract T createWork() throws IOException;

    protected T work;
    protected String sep;

    @Before
    public void setUp() throws Exception {
        work = createWork();
        sep = work.getRoot().getFilesystem().getSeparator();
    }

    @Test
    public void locator() throws Exception {
        URI locator;
        Node again;
        Filesystem fs;

        fs = work.getRoot().getFilesystem();
        locator = work.getURI();
        assertEquals(locator, work.getWorld().node(fs.getScheme() + ":" + work.getRoot().getId() + work.getPath()).getURI());
        again = WORLD.node(locator);
        assertEquals(work, again);
        assertEquals(locator, again.getURI());
    }

    //@Test(expected=NodeInstantiationException.class)
    @Ignore
    public void headingSlash() throws Exception {
        Filesystem fs;
        Root root;

        root = work.getRoot();
        fs = root.getFilesystem();
        fs.getWorld().node(fs.getScheme() + ":" + root.getId() + fs.getSeparator() + work.getPath());
    }
}