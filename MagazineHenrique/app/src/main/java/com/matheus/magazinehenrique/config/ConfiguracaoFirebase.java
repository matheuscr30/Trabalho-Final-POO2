package com.matheus.magazinehenrique.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by matheus on 06/10/17.
 */

public class ConfiguracaoFirebase {

    private volatile static FirebaseAuth firebaseAuth;
    private volatile static DatabaseReference databaseReference;
    private volatile static StorageReference storageReference;

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

    public static StorageReference getFirebaseStorage(){
        if(storageReference == null){
            synchronized (ConfiguracaoFirebase.class){
                if(storageReference == null){
                    storageReference = FirebaseStorage.getInstance("gs://loja-4b018.appspot.com").getReference();
                }
            }
        }
        return storageReference;
    }
}
