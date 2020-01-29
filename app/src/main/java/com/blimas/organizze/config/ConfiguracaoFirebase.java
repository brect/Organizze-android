package com.blimas.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {


    private static FirebaseAuth auth;


    //retorna a instancia do firebase
    public static FirebaseAuth getAuth() {

        if (auth == null){

            auth = FirebaseAuth.getInstance();

        }
        return auth;
    }

}
