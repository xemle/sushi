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

package de.ui.sushi.metadata.simpletypes;

import de.ui.sushi.cli.ArgumentException;
import de.ui.sushi.fs.IO;
import de.ui.sushi.fs.Node;
import de.ui.sushi.fs.RootPathException;
import de.ui.sushi.metadata.Schema;
import de.ui.sushi.metadata.SimpleType;

import java.net.URISyntaxException;

public class NodeType extends SimpleType {
    private final IO io;

    public NodeType(Schema schema, IO io) {
        super(schema, Node.class, "node");

        this.io = io;
    }

    @Override
    public Object newInstance() {
        return io.getWorking();
    }

    @Override
    public String valueToString(Object obj) {
        return obj.toString();
    }

    @Override
    public Object stringToValue(String str) {
        try {
            return io.node(str);
        } catch (RootPathException e) {
            throw new ArgumentException(str, e);
        } catch (URISyntaxException e) {
            throw new ArgumentException(str, e);
        }
    }
}
