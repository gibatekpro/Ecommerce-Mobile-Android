package com.gibatekpro.ecommerceandroid.cart.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gibatekpro.ecommerceandroid.cart.dao.CartDao;
import com.gibatekpro.ecommerceandroid.cart.model.CartItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CartItem.class}, version = 2)
@TypeConverters(BigDecimalTypeConverter.class)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDao cartDao();

    private static volatile CartDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    //Migration
    //https://developer.android.com/training/data-storage/room/migrating-db-versions
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE cart_table "
                    + " ADD COLUMN name TEXT");
        }
    };

    public static CartDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CartDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CartDatabase.class, "cart_database")
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * To delete all content and populate the database when the app is installed,
     * you create a RoomDatabase.Callback and override onCreate().
     * <p>
     * Here is the code for creating the callback within the WordRoomDatabase class.
     * Because you cannot do Room database operations on the UI thread, onCreate()
     * uses the previously defined databaseWriteExecutor to execute a lambda on a
     * background thread. The lambda deletes the contents of the database, then populates
     * it with the two words "Hello" and "World". Feel free to add more words!
     */

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                WordDao dao = INSTANCE.wordDao();
//                dao.deleteAll();
//
//                Word word = new Word("Hello");
//                dao.insert(word);
//                word = new Word("World");
//                dao.insert(word);
//            });
        }
    };


}
