package name.arche.commonandroidapplication.models;

import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by arche on 2016/4/1.
 */
public class BaseModel extends Model {

    /**
     * 将Model转换为Json字符串
     */
    public String toJSONString() {
        Gson gson = new GsonBuilder().setExclusionStrategies(new SpecificClassExclusionStrategy(null, com.activeandroid.Model.class)).create();
        return gson.toJson(this);
    }

    /**
     * 将json解析为实体类
     */
    public static <T extends Model> T parseObject(final String json, Class<T> clazz) {
        // Json字符串为空，则返回null
        Gson gson = new Gson();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }


    /**
     * 批量插入数据
     */
    public static <T extends Model> void bulkSave(List<T> listObj) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < listObj.size(); i++) {
                listObj.get(i).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            ActiveAndroid.endTransaction();
        }
    }

    /**
     * 清空数据表
     */
    public static <T extends Model> void clearTable(Class<T> clazz) {
        new Delete().from(clazz).execute();
    }


    /**
     * 查询表中所有数据
     */
    public static <T extends Model> List<T> listAll(Class<T> clazz) {
        return new Select().all().from(clazz).execute();
    }

    /**
     * 根据Id查询
     */
    public static <T extends Model> T listById(Class<T> clazz, Object id) {
        return new Select().from(clazz).where("Id = ?", id).executeSingle();
    }

    /**
     * 根据条件查询
     */
    public static <T extends Model> List<T> listByField(Class<T> clazz, String field, Object condition) {
        return new Select().from(clazz).where(field, condition).execute();
    }

    /**
     * 根据Id删除
     */
    public static <T extends Model> T deleteById(Class<T> clazz, Object id) {
        return new Delete().from(clazz).where("Id = ?", id).executeSingle();
    }

    /**
     * 根据条件删除
     */
    public static <T extends Model> List<T> deleteByField(Class<T> clazz, String field, Object condition) {
        return new Delete().from(clazz).where(field, condition).execute();
    }



    public class SpecificClassExclusionStrategy implements ExclusionStrategy {
        private final Class<?> excludedThisClass;
        private final Class<?> excludedThisClassFields;

        /***
         * 过滤器初始化
         *
         * @param excludedThisClass      该类和继承自该类的对象实例将被忽略
         * @param excluedThisClassFields 该类的属性将不被序列化
         */
        public SpecificClassExclusionStrategy(Class<?> excludedThisClass, Class<?> excluedThisClassFields) {
            this.excludedThisClass = excludedThisClass;
            this.excludedThisClassFields = excluedThisClassFields;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            if (clazz == null) return false;
            if (clazz.equals(excludedThisClass)) return true;
            return shouldSkipClass(clazz.getSuperclass());
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(excludedThisClassFields);
        }
    }


}
