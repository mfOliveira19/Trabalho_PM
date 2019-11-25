package com.example.listadecontactos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 21;
    public static final String DATABASE_NAME = "contactos.db";

    public DB(Context context) { super (context, DATABASE_NAME, null, DATABASE_VERSION); }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contrato.User.SQL_CREATE_ENTRIES);
        db.execSQL(Contrato.Contacto.SQL_CREATE_ENTRIES);

        db.execSQL("insert into " + Contrato.User.TABLE_NAME + " values (1, 'admin', '2e33a9b0b06aa0a01ede70995674ee23');");

        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (1, 'Joaquim', 'Santos', 91523157, 'joaquim@teste.pt', 'Rua de teste', 50, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (2, 'Alfredo', 'Campos', 96612885, 'alfredo@teste.pt', 'Rua de teste', 52, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (3, 'José', 'Silva', 96612885, 'jose@teste.pt', 'Rua de teste', 55, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (4, 'Miguel', 'Oliveira', 966400474, 'miguel@teste.pt', 'Rua de teste', 20, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (5, 'Paulo', 'Silva', 96612835, 'paulo@teste.pt', 'Rua de teste', 25, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (6, 'Tiago', 'Oliveira', 96612334, 'tiago@teste.pt', 'Rua de teste', 27, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (7, 'João', 'Oliveira', 96612475, 'joao@teste.pt', 'Rua de teste', 26, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (8, 'André', 'Silva', 96612885, 'andre@teste.pt', 'Rua de teste', 30, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (9, 'Pedro', 'Ferreira', 96612885, 'pedro@teste.pt', 'Rua de teste', 21, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (10, 'António', 'Silva', 96612885, 'antonio@teste.pt', 'Rua de teste', 24, 1);");
        db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (11, 'Jorge', 'Silva', 96612885, 'jorge@teste.pt', 'Rua de teste', 23, 1);");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contrato.Contacto.SQL_DROP_ENTRIES);
        db.execSQL(Contrato.User.SQL_DROP_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
