package name.arche.www.commonapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Arche on 2015/11/3.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "CommonApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
//        try{
//            TableUtils.createTableIfNotExists(connectionSource,User.class);
//
//        } catch (SQLException e) {
//            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        if (i != i1){
//            try {
//                TableUtils.dropTable(connectionSource,User.class,true);
//
//                onCreate(sqLiteDatabase,connectionSource);
//            }catch (SQLException e) {
//                Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//                throw new RuntimeException(e);
//            }
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
