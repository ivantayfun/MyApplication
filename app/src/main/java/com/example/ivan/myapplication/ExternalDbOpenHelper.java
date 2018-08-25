package com.example.ivan.myapplication;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ExternalDbOpenHelper extends SQLiteOpenHelper {
    public static  String DB_PATH;
    public static  String DB_NAME;
    public static  String DB_FOLDER;
    public static  String packageName;
    public SQLiteDatabase database;
    public Context context;
    public static int DB_FILES_COPY_BUFFER_SIZE;
    private static  int DB_VERSION;
    public SQLiteDatabase getDb() {
        return database;
    }
    public ExternalDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.context = context;
        packageName = context.getPackageName();
        DB_FOLDER = "/data/data/"+ packageName + "/databases/";
        DB_NAME = databaseName;
        DB_PATH = DB_FOLDER + DB_NAME;
        DB_FILES_COPY_BUFFER_SIZE = 1024;
        DB_VERSION = 1;
               }
   private static boolean isInitialized() {
        SQLiteDatabase checkDB = null;
        Boolean correctVersion = false;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,SQLiteDatabase.OPEN_READONLY);
            correctVersion = checkDB.getVersion() == DB_VERSION;
        } catch (SQLiteException e) {
        } finally {
            if (checkDB != null)
                checkDB.close();
        }
        return checkDB != null && correctVersion;
    }
    public  void createDataBase() {
        if (!isInitialized() ) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
            this.close();
        }
    }
    private void copyDataBase() throws IOException {
        // Открываем поток для чтения из уже созданной нами БД
        //источник в assets
        try {
            InputStream externalDbStream = context.getAssets().open(DB_NAME);
            // Путь к уже созданной пустой базе в андроиде
            // Теперь создадим поток для записи в эту БД побайтно
            OutputStream localDbStream = new FileOutputStream(DB_PATH);
            // Собственно, копирование
            byte[] buffer = new byte[DB_FILES_COPY_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = externalDbStream.read(buffer)) > 0) {
                localDbStream.write(buffer, 0, bytesRead);
            }
            // Мы будем хорошими мальчиками (девочками) и закроем потоки
            localDbStream.close();
            externalDbStream.close();

        }catch (IOException ex) {
        } finally {
        }
    }
    public  SQLiteDatabase openDataBase() throws SQLException {
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(DB_PATH, null,SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }
    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
