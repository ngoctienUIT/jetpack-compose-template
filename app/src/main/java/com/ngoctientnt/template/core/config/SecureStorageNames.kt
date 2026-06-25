package com.ngoctientnt.template.core.config

// Keep in sync with res/xml/backup_rules.xml and res/xml/data_extraction_rules.xml
object SecureStorageNames {
    /** Legacy EncryptedSharedPreferences — migration source only. */
    const val AUTH_PREFS = "auth_secure_prefs"

    /** Legacy EncryptedSharedPreferences — migration source only. */
    const val DATABASE_PASSPHRASE_PREFS = "database_passphrase_prefs"

    /** Tink keyset backing encrypted auth DataStore. */
    const val AUTH_KEYSET = "auth_keyset"

    /** Tink keyset backing encrypted database passphrase DataStore. */
    const val DATABASE_PASSPHRASE_KEYSET = "database_passphrase_keyset"
}
