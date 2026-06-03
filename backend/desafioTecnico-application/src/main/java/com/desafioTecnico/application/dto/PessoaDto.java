package com.desafioTecnico.application.dto;

import com.desafioTecnico.domain.entidade.Pessoa;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PessoaDto {

    private final String id;
    private final String nomeCompleto;
    private final String cpf;
    private final String email;
    private final LocalDate dataNascimento;
    private final EnderecoDto endereco;
    private final String login;
    private final LocalDateTime criadoEm;

    public PessoaDto(String id, String nomeCompleto, String cpf, String email,
                           LocalDate dataNascimento, EnderecoDto endereco,
                           String login, LocalDateTime criadoEm) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.login = login;
        this.criadoEm = criadoEm;
    }

    public static PessoaDto de(Pessoa pessoa) {
        String cpfFormatado = formatarCpf(pessoa.getCpf());
        EnderecoDto endereco = new EnderecoDto(
                pessoa.getCep(),
                pessoa.getLogradouro(),
                pessoa.getBairro(),
                pessoa.getCidade(),
                pessoa.getEstado(),
                pessoa.getComplemento(),
                pessoa.getNumero()
        );
        return new PessoaDto(
                pessoa.getId().toString(),
                pessoa.getNomeCompleto(),
                cpfFormatado,
                pessoa.getEmail(),
                pessoa.getDataNascimento(),
                endereco,
                pessoa.getLogin(),
                pessoa.getCriadoEm()
        );
    }

    private static String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    public String getId()                       { return id; }
    public String getNomeCompleto()             { return nomeCompleto; }
    public String getCpf()                      { return cpf; }
    public String getEmail()                    { return email; }
    public LocalDate getDataNascimento()        { return dataNascimento; }
    public EnderecoDto getEndereco()       { return endereco; }
    public String getLogin()                    { return login; }
    public LocalDateTime getCriadoEm()          { return criadoEm; }
}
