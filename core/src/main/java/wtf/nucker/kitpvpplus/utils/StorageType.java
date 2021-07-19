package wtf.nucker.kitpvpplus.utils;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public enum StorageType {

    FLAT,
    MONGO,
    SQL;

    public enum BankStorageType {
        VAULT,
        FLAT;
    }
}
