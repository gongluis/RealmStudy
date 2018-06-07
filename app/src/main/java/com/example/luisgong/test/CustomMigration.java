package com.example.luisgong.test;

import java.nio.file.attribute.FileAttribute;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class CustomMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema =realm.getSchema();
        if (oldVersion==0&&newVersion==1){
            RealmObjectSchema personSchema  = schema.get("User");
            //新增    @required的id
            personSchema.addField("tell",String.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("tell","15900876104");//为tell设置值

                        }
                    }).removeField("age");//移除age属性
            oldVersion++;
        }
    }
}
