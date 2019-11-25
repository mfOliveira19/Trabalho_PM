package com.example.listadecontactos.entities;

public class Contacto {
    public String nome;
    public String apelido;
    public int numero;
    public String email;
    public String morada;
    public int idade;

    public Contacto(String name, String surname, int number, String email, String address, int age) {
        this.nome = name;
        this.apelido = surname;
        this.numero = number;
        this.email = email;
        this.morada = address;
        this.idade = age;

    }

    public String getNome() {
        return nome;
    }

    public String getApelido() {
        return apelido;
    }

    public int getNumero() {
        return numero;
    }

    public String getEmail() {
        return email;
    }

    public String getMorada() {
        return morada;
    }

    public int getIdade() {
        return idade;
    }
}
