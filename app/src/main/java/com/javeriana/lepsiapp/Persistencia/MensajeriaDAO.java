package com.javeriana.lepsiapp.Persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.javeriana.lepsiapp.entidades.Mensaje;

public class MensajeriaDAO {

    private static MensajeriaDAO mensajeriaDAO;
    private FirebaseDatabase database;
    private DatabaseReference referenceMensajeria;
    public static MensajeriaDAO getInstancia(){
        if(mensajeriaDAO==null) mensajeriaDAO = new MensajeriaDAO();
        return mensajeriaDAO;
    }

    private MensajeriaDAO(){
        database = FirebaseDatabase.getInstance();
        referenceMensajeria = database.getReference("Mensajes");
        //storage = FirebaseStorage.getInstance();
        //referenceUsuarios = database.getReference(Constantes.NODO_USUARIOS);
        //referenceFotoDePerfil = storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }

    public void nuevoMensaje(String keyEmisor, String keyReceptor, Mensaje mensaje){
        DatabaseReference referenceEmisor = referenceMensajeria.child(keyEmisor).child(keyReceptor);
        DatabaseReference referenceReceptor = referenceMensajeria.child(keyReceptor).child(keyEmisor);
        referenceEmisor.push().setValue(mensaje);
        referenceReceptor.push().setValue(mensaje);
    }

}
