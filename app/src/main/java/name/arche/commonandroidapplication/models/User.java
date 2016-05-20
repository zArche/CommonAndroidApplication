package name.arche.commonandroidapplication.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by arche on 2016/4/7.
 */
@Table(name = "users")
public class User extends BaseModel {
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "user_name")
    private String user_name;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
