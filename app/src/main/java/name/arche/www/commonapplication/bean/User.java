package name.arche.www.commonapplication.bean;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;

import name.arche.www.commonapplication.database.DatabaseHelper;

/**
 * Created by Arche on 2015/11/3.
 */

@DatabaseTable(tableName = "accounts")
public class User implements Serializable {

    @DatabaseField
    private int userId;

    @DatabaseField
    private String userName;

    @DatabaseField
    private String token;


    private static Dao<User,String> userDao = null;

    public static Dao<User,String> getDao(DatabaseHelper databaseHelper){
        if (databaseHelper == null)
            return null;
        if (userDao == null){
            try{
                userDao = databaseHelper.getDao(User.class);
            } catch (SQLException e) {
                Log.e(DatabaseHelper.class.getName(), "Can't userDao", e);
                e.printStackTrace();
            }
        }

        return userDao;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
