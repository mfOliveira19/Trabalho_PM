package com.example.listadecontactos.entities;

public class Contacto {
    public String nome;
    public String apelido;
    public int numero;
    public String email;
    public String morada;
    public int idade;
    public int id_user;

    public Contacto(String name, String surname, int number, String email, String address, int age, int id_user) {
        this.nome = name;
        this.apelido = surname;
        this.numero = number;
        this.email = email;
        this.morada = address;
        this.idade = age;
        this.id_user = id_user;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
