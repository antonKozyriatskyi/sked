package kozyriatskyi.anton.sked.storage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class StorageManager {

    private static StorageManager manager;

    @NotNull
    public static StorageManager get() {
        if (manager == null) {
            manager = new StorageManager();
        }

        return manager;
    }

    private HashMap<String, Storage> storages = new HashMap<>();

    private StorageManager() {
    }

    @NotNull
    public Storage obtainStorage(@NotNull String key) {
        Storage storage = storages.get(key);

        if (storage == null) {
            storage = new Storage(key);
            registerStorage(key, storage);
        }

        return storage;
    }

    public void releaseStorage(@NotNull String key) {
        storages.remove(key);
    }

    private void registerStorage(String key, Storage storage) {
        storages.put(key, storage);
    }
}
