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

package de.ui.sushi.fs.file;

import java.io.File;

import de.ui.sushi.fs.Root;
import de.ui.sushi.io.OS;


public class FileRoot implements Root {
    private final FileFilesystem filesystem;
    private final String id;
    
    public FileRoot(FileFilesystem filesystem, String id) {
        this.filesystem = filesystem;
        this.id = id;
        if (!id.endsWith(filesystem.getSeparator())) {
            throw new IllegalArgumentException();
        }
    }

    public FileFilesystem getFilesystem() {
        return filesystem;
    }

    public String getId() {
        return id;
    }
    
    public FileNode node(String path) {
        return new FileNode(this, new File(id + path));
    }
}
