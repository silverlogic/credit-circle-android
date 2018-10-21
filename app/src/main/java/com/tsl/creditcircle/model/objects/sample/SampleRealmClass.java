package com.tsl.creditcircle.model.objects.sample;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Manu on 14/3/17.
 */

/**
 * Just a sample class that we need until we create another RealmObject instance.
 * We need at least one RealmClass in order to make RealmSugar launch.
 *
 * After creating our first RealmObject this class must be removed from the project
 */
@RealmClass
public class SampleRealmClass extends RealmObject {
    @PrimaryKey
    Integer id;
}
