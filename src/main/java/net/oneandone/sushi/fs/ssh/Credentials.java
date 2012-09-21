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
package net.oneandone.sushi.fs.ssh;

import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import net.oneandone.sushi.fs.Node;
import net.oneandone.sushi.fs.World;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/** Private key with passphrase */
public class Credentials {
    public static Credentials loadDefault(World world) throws IOException {
        Node dir;
        Node file;
        Node key;
        String passphrase;

        dir = world.getHome().join(".ssh");
        file = dir.join("passphrase");
        if (file.exists()) {
            passphrase = file.readString().trim();
        } else {
            passphrase = "";
        }
        key = dir.join("id_dsa");
        if (!key.exists()) {
            key = dir.join("id_rsa");
            if (!key.exists()) {
                key = dir.join("identity");
            }
        }
        if (!key.isFile()) {
            throw new IOException("private key not found: " + key);
        }
        return load(key, passphrase);
    }

    public static Credentials load(Node node) throws IOException {
        return load(node, "");
    }

    public static Credentials load(Node node, String passphrase) throws IOException {
        return new Credentials(node.toString(), node.readBytes(), passphrase);
    }

    public final String name;
    public final byte[] privateKey;
    public final String passphrase;

    public Credentials(String name, byte[] privateKey) {
        this(name, privateKey, "");
    }

    public Credentials(String name, byte[] privateKey, String passphrase) {
        if (passphrase == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
    }

    public Identity loadIdentity(JSch jsch) throws JSchException {
        Throwable te;
        Class<?> clz;
        Method m;

        try {
            clz = Class.forName("com.jcraft.jsch.IdentityFile");
            m = clz.getDeclaredMethod("newInstance", String.class, byte[].class, byte[].class, JSch.class);
            m.setAccessible(true);
            return (Identity) m.invoke(null, name, Arrays.copyOf(privateKey, privateKey.length), null, jsch);
        } catch (InvocationTargetException e) {
            te = e.getTargetException();
            if (te instanceof JSchException) {
                throw (JSchException) te;
            } else {
                throw new IllegalStateException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException("TODO", e);
        }
    }
}