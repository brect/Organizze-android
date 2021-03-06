package com.blimas.organizze.config;

import com.blimas.organizze.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {


    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;



    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    //retorna a instancia do firebase
    public static FirebaseAuth getAuth() {

        if( auth == null ){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

//    public DatabaseReference getUserRef(){
//        String user = auth.getCurrentUser().getEmail();
//        String idUser = Base64Custom.encodeBase64(user);
//        DatabaseReference userRef = databaseReference.child("usuarios").child(idUser);
//        return userRef;
//    }

}
