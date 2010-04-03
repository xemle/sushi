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

package de.ui.sushi.fs.memory;

import de.ui.sushi.fs.Features;
import de.ui.sushi.fs.Filesystem;
import de.ui.sushi.fs.IO;
import de.ui.sushi.fs.NodeInstantiationException;

import java.net.URI;
import java.util.WeakHashMap;

public class MemoryFilesystem extends Filesystem {
    private final WeakHashMap<Integer, MemoryRoot> roots;

    public int maxInMemorySize;

    public MemoryFilesystem(IO io, String name) {
        super(io, '/', new Features(true, false, false, false, false, false), name);

        this.roots = new WeakHashMap<Integer, MemoryRoot>();
        this.maxInMemorySize = 32 * 1024;
    }

    @Override
    public MemoryNode node(URI uri, Object extra) throws NodeInstantiationException {
        MemoryRoot result;

        if (extra != null) {
            throw new NodeInstantiationException(uri, "unexpected extra argument: " + extra);
        }
        checkHierarchical(uri);
        try {
            result = root(Integer.parseInt(uri.getAuthority()));
        } catch (NumberFormatException e) {
            throw new NodeInstantiationException(uri, "invalid root: " + uri.getAuthority(), e);
        }
        return result.node(getCheckedPath(uri));
    }

    public MemoryRoot root(int id) {
        MemoryRoot root;

        root = roots.get(id);
        if (root == null) {
            root = new MemoryRoot(this, id);
            roots.put(id, root);
        }
        return root;
    }

    public MemoryRoot root() {
        MemoryRoot root;

        for (int id = 0; true; id++) {
            if (!roots.containsKey(id)) {
                root = new MemoryRoot(this, id);
                roots.put(id, root);
                return root;
            }
        }
    }
}
