package com.oneandone.sushi.fs.ssh;

import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.oneandone.sushi.fs.IO;
import com.oneandone.sushi.fs.Node;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/** Private key with passphrase */
public class Credentials {
    public static Credentials loadDefault(IO io) throws IOException {
        Node dir;
        Node file;
        Node key;
        String passphrase;

        dir = io.getHome().join(".ssh");
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
        return new Credentials(node.getAbsolute(), node.readBytes(), passphrase);
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
            m = clz.getDeclaredMethod("newInstance", new Class<?>[] { String.class, byte[].class, byte[].class, JSch.class });
            m.setAccessible(true);
            return (Identity) m.invoke(null, new Object[] { name, Arrays.copyOf(privateKey, privateKey.length), null, jsch });
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