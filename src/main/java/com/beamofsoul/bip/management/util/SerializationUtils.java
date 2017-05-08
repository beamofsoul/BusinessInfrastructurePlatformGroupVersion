package com.beamofsoul.bip.management.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.SerializationException;

import lombok.Cleanup;
import lombok.NonNull;

public class SerializationUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T object) {
        return (T) deserialize(serialize(object));
	}
	
	public static void serialize(Serializable obj,@Nonnull OutputStream outputStream) {
        try {
        	@Cleanup ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(obj);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }
	
	public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }
	
	public static Object deserialize(@NonNull InputStream inputStream) {
        try {
        	@Cleanup ObjectInputStream in = new ObjectInputStream(inputStream);
            return in.readObject();
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }
	
	public static Object deserialize(@NonNull byte[] objectData) {
        return deserialize(new ByteArrayInputStream(objectData));
    }
}
