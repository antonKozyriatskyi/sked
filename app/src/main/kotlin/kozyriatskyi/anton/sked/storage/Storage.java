package kozyriatskyi.anton.sked.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Created by Anton on 19.01.2018.
 */

public class Storage {

    private HashMap<String, Object> objects = new HashMap<>();

    private String storageKey;

    private boolean isReleased = false;

    Storage(@NotNull String key) {
        storageKey = key;
    }

    @NotNull
    public String getStorageKey() {
        return storageKey;
    }

    public void save(String key, Object value) {
        ensureState();

        objects.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T get(String key) {
        ensureState();

        Object object = objects.get(key);

        if (object == null) return null;

        return (T) object;
    }

    public int getInt(String key) {
        ensureState();

        Object object = objects.get(key);

        if (object == null) return 0;

        return (int) object;
    }

    public long getLong(String key) {
        ensureState();

        Object object = objects.get(key);

        if (object == null) return 0;

        return (long) object;
    }

    @Nullable
    public String getString(String key) {
        ensureState();

        Object object = objects.get(key);

        if (object == null) return null;

        return (String) object;
    }

    public double getDouble(String key) {
        ensureState();

        Object object = objects.get(key);

        if (object == null) return 0d;

        return (int) object;
    }

    public float getFloat(String key) {
        ensureState();

        Object object = objects.get(key);

        if (object == null) return 0f;

        return (float) object;
    }

    public boolean contains(String key) {
        ensureState();

        return objects.containsKey(key);
    }

    public void release() {
        StorageManager.get().releaseStorage(storageKey);
        isReleased = true;
    }

    private void ensureState() throws IllegalStateException {
        if (isReleased) {
            throw new IllegalStateException("Couldn't read/write into released storage");
        }
    }
}