package com.desafioTecnico.application.command;

import java.time.LocalDate;

public class RegisterPersonCommand {

    private final String fullName;
    private final String document;
    private final String email;
    private final LocalDate birthDate;
    private final String cep;
    private final String complement;
    private final String number;

    public RegisterPersonCommand(String fullName, String document, String email,
                                  LocalDate birthDate, String cep,
                                  String complement, String number) {
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.birthDate = birthDate;
        this.cep = cep;
        this.complement = complement;
        this.number = number;
    }

    public String fullName()    { return fullName; }
    public String document()    { return document; }
    public String email()       { return email; }
    public LocalDate birthDate(){ return birthDate; }
    public String cep()         { return cep; }
    public String complement()  { return complement; }
    public String number()      { return number; }
}
