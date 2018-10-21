package com.tsl.creditcircle.utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by Manu on 9/1/17.
 */

public class Writer {

    /**
     * Some objects must be persisted with a custom implementation. These objects must implements this interface
     */
    public interface CustomPersistable<T extends RealmModel> {
        T persist(Realm realm);
    }

    public interface Task {
        void run(Realm realm);
    }

    public interface ParametizedTask<T> {
        T run(Realm realm);
    }

    public static <T> T execute(ParametizedTask<T> task) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try {
            T result = task.run(realm);
            realm.commitTransaction();
            return result;
        }

        catch (Exception e) {
            realm.cancelTransaction();
            throw e;
        }

        finally {
            realm.close();
        }
    }

    public static void execute(final Task task) {
        execute(new ParametizedTask<Object>() {
            @Override
            public Object run(Realm realm) {
                task.run(realm);
                return null;
            }
        });
    }

    // Persist
    public static <T extends RealmModel> T persist(final T object) {
        return execute(new ParametizedTask<T>() {
            @Override
            public T run(Realm realm) {
                return performPersist(object, realm);
            }
        });
    }

    public static <T extends RealmModel> List<T> persist(final Iterable<T> objects) {
        return execute(new ParametizedTask<List<T>>() {
            @Override
            public List<T> run(Realm realm) {
                ArrayList<T> result = new ArrayList<>();
                for (T obj: objects) {
                    result.add(performPersist(obj, realm));
                }
                return result;
            }
        });
    }

    public static List<RealmModel> persist(final RealmModel... objects) {
        return execute(new ParametizedTask<List<RealmModel>>() {
            @Override
            public List<RealmModel> run(Realm realm) {
                ArrayList<RealmModel> result = new ArrayList<>();
                for (RealmModel obj: objects) {
                    result.add(performPersist(obj, realm));
                }
                return result;
            }
        });
    }

    private static <T extends RealmModel> T performPersist(final T object, Realm realm) {
        if (object instanceof CustomPersistable) {
            return (T) ((CustomPersistable) object).persist(realm);
        }
        else {
            return realm.copyToRealmOrUpdate(object);
        }
    }
}
