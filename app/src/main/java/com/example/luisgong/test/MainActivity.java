package com.example.luisgong.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private RealmAsyncTask transaction;
    private Realm realm;
    private String TAG ="tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        //增加一个表
        addForm(realm);
        //查询数据
        findData();
        //改数据  修改第一条数据年龄为30
        changeAge();
        //删数据
        deletefirstData();

    }

    private void deletefirstData() {
        final RealmResults<User> alllist = realm.where(User.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
//                alllist.get(2).
                alllist.deleteFromRealm(0);

            }
        });

        /*userList.deleteFirstFromRealm(); //删除user表的第一条数据
        userList.deleteLastFromRealm();//删除user表的最后一条数据
        results.deleteAllFromRealm();//删除user表的全部数据*/
    }

    private void changeAge() {
        //先查找得到user对象然后修改
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User first = realm.where(User.class).findFirst();
                first.setAge(30);
            }
        });
    }

    private void findData() {
        RealmResults<User> userList = realm.where(User.class).findAll();
        for (int i = 0; i < userList.size(); i++) {
            Log.e("person", "user表中所有数据"+userList.get(i).getName());
        }

        //当数据量较大，采用异步查询
        RealmResults<User> userlist = realm.where(User.class).equalTo("name", "lisi").findAllAsync();

        //这里并不会马上查到数据，是有一定延时的。也就是说，你马上使用userList的时候，里面是没有数据的。可以注册RealmChangeListener监听器，或者使用isLoaded()方法，判断是否查询完成
        if (userList.isLoaded()) {
            Log.i(TAG, "完成查询");
            for (int i = 0; i < userList.size(); i++) {
                Log.i(TAG, userList.get(i).getName());
            }
        }
        //查询第一条
        User first = realm.where(User.class).findFirst();
        Log.i(TAG, "findfirst"+first.getName());
        //equalTo查询
        RealmResults<User> all = realm.where(User.class).equalTo("name", "zhaoliu").findAll();
        Log.i(TAG, "equalTo查询赵六"+all.toString());
        //equalTo多条件查询
        /*RealmResults<User> userList = mRealm.where(User.class)
                .equalTo("name", "Gavin")
                .equalTo("dogs.name", "二哈")
                .findAll();*/
        //查询name为“Gavin”和“Eric”的用户  or()的使用
        /*RealmResults<User> userList = mRealm.where(User.class)
                .equalTo("name", "Gavin")
                .or().equalTo("name", "Eric")
                .findAll();*/
        //查询结果排序
        RealmResults<User> userListorder = realm.where(User.class) .findAll();
        RealmResults<User> ageOrderList = userListorder.sort("age");
//        RealmResults<User> ageOrderList = userListorder.sort("age", Sort.DESCENDING);//   逆序排序
        //年龄和，最小，最大年龄平均年龄

       /* long   sum     = results.sum("age").longValue();
        long   min     = results.min("age").longValue();
        long   max     = results.max("age").longValue();
        double average = results.average("age");
        long   matches = results.size();*/


    }

    private void addForm(Realm realm) {
        //第一种方法插入
       /* realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.createObject(User.class,1);
                user.setName("zhangsan");
                user.setAge(10);
                user.setEnlishname("luis");
            }
        });*/
        //第二种方法插入
        final User user = new User();
        user.setId(2);
        user.setName("lisi");
        user.setAge(11);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //如果没有主键，就使用copytoRealm,否则将抛出异常
                realm.copyToRealmOrUpdate(user);
            }
        });
/*        //第三种插入数据方法
        realm.beginTransaction();
        User user1 =realm.createObject(user.getClass(),3);
        user1.setAge(12);
        user1.setName("wangwu");
        realm.commitTransaction();*/

        //插入数据监听
        //成功回调
//失败回调
        final User user2 = new User();
        user2.setId(4);
        user2.setAge(13);
        user2.setName("zhaoliu");
        transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user2);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //成功回调
                Log.e("person", "onSuccess: 插入zhaoliu成功" );
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //失败回调
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        //如果当Acitivity或Fragment被销毁时，在OnSuccess或OnError中执行UI操作，将导致程序奔溃 。用RealmAsyncTask .cancel();可以取消事务 在onStop中调用，避免crash
        if (transaction!=null&&!transaction.isCancelled()) {
            transaction.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
