package com.matheus.magazinehenrique.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by matheus on 06/10/17.
 */

public class ConfiguracaoFirebase {

    private volatile static FirebaseAuth firebaseAuth;
    private volatile static DatabaseReference databaseReference;

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            synchronized (ConfiguracaoFirebase.class){
                if(firebaseAuth == null){
                    firebaseAuth = FirebaseAuth.getInstance();
                }
            }
        }
        return firebaseAuth;
    }

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            synchronized (ConfiguracaoFirebase.class){
                if(databaseReference == null){
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                }
            }
        }
        return  databaseReference;
    }
}
