package com.hp77.linkstash.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add notes field
        database.execSQL("ALTER TABLE links ADD COLUMN notes TEXT")
        
        // Add HackerNews integration columns
        database.execSQL("ALTER TABLE links ADD COLUMN hackerNewsId TEXT")
        database.execSQL("ALTER TABLE links ADD COLUMN hackerNewsUrl TEXT")
        
        // Add GitHub sync columns
        database.execSQL("ALTER TABLE links ADD COLUMN lastSyncedAt INTEGER")
        database.execSQL("ALTER TABLE links ADD COLUMN syncError TEXT")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add type column with default value OTHER
        database.execSQL("ALTER TABLE links ADD COLUMN type TEXT NOT NULL DEFAULT 'OTHER'")
        
        // Add isCompleted column with default value 0 (false)
        database.execSQL("ALTER TABLE links ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
        
        // Add completedAt column (nullable)
        database.execSQL("ALTER TABLE links ADD COLUMN completedAt INTEGER")
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the tags table if it doesn't exist
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS tags (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                color TEXT,
                createdAt INTEGER NOT NULL
            )
        """)

        // Create the link_tag_cross_ref table if it doesn't exist
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS link_tag_cross_ref (
                linkId TEXT NOT NULL,
                tagId TEXT NOT NULL,
                PRIMARY KEY(linkId, tagId),
                FOREIGN KEY(linkId) REFERENCES links(id) ON DELETE CASCADE,
                FOREIGN KEY(tagId) REFERENCES tags(id) ON DELETE CASCADE
            )
        """)

        // Create a temporary table with the new schema
        database.execSQL("""
            CREATE TABLE links_new (
                id TEXT PRIMARY KEY NOT NULL,
                url TEXT NOT NULL,
                title TEXT,
                description TEXT,
                previewImageUrl TEXT,
                createdAt INTEGER NOT NULL,
                reminderTime INTEGER,
                isArchived INTEGER NOT NULL,
                isFavorite INTEGER NOT NULL
            )
        """)

        // Copy data from old table to new table
        database.execSQL("""
            INSERT INTO links_new (
                id, url, title, description, previewImageUrl,
                createdAt, reminderTime, isArchived, isFavorite
            )
            SELECT 
                id, url, title, description, previewImageUrl,
                createdAt, reminderTime, isArchived, isFavorite
            FROM links
        """)

        // Try to migrate any existing tags from the old links table
        try {
            // Get tags from old links table (assuming they were stored in a 'tags' column)
            val cursor = database.query("SELECT id, tags FROM links")
            if (cursor.moveToFirst()) {
                do {
                    val linkId = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                    val tagsString = cursor.getString(cursor.getColumnIndexOrThrow("tags"))
                    
                    // Parse tags and create entries
                    tagsString?.split(",")?.map { it.trim() }?.forEach { tagName ->
                        if (tagName.isNotEmpty()) {
                            val tagId = UUID.randomUUID().toString()
                            
                            // Insert tag
                            database.execSQL("""
                                INSERT OR IGNORE INTO tags (id, name, createdAt)
                                VALUES (?, ?, ?)
                            """, arrayOf(tagId, tagName, System.currentTimeMillis()))
                            
                            // Create cross reference
                            database.execSQL("""
                                INSERT OR IGNORE INTO link_tag_cross_ref (linkId, tagId)
                                VALUES (?, ?)
                            """, arrayOf(linkId, tagId))
                        }
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            // If anything goes wrong during tag migration, just log it and continue
            android.util.Log.e("Migration", "Failed to migrate tags: ${e.message}")
        }

        // Drop old table
        database.execSQL("DROP TABLE links")

        // Rename new table to original name
        database.execSQL("ALTER TABLE links_new RENAME TO links")
    }
}
