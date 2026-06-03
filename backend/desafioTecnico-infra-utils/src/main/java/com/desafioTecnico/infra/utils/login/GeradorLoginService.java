package com.desafioTecnico.infra.utils.login;

import com.desafioTecnico.application.interface_.IPortaGeradorLogin;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GeradorLoginService implements IPortaGeradorLogin {

    @Override
    public String gerar(String nomeCompleto, List<String> loginsExistentes) {
        Set<String> ocupados = loginsExistentes.stream().collect(Collectors.toSet());
        String normalizado = normalizar(nomeCompleto);
        String[] partes = normalizado.split("\\s+");

        List<String> candidatos = gerarCandidatos(partes);

        for (String candidato : candidatos) {
            if (!ocupados.contains(candidato)) {
                return candidato;
            }
        }

        String todosChars = String.join("", partes);
        for (int offset = 0; offset < todosChars.length(); offset++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                sb.append(todosChars.charAt((offset + i) % todosChars.length()));
            }
            String candidato = sb.toString();
            if (candidato.matches("[a-z]{7}") && !ocupados.contains(candidato)) {
                return candidato;
            }
        }

        throw new ExcecaoDominio("Não foi possível gerar um login único para: " + nomeCompleto);
    }

    private List<String> gerarCandidatos(String[] partes) {
        List<String> candidatos = new ArrayList<>();
        String primeiroNome = partes[0];
        List<String> sobrenomes = Arrays.asList(partes).subList(1, partes.length);

        for (int take = 1; take <= Math.min(7, primeiroNome.length()); take++) {
            String prefixo = primeiroNome.substring(0, take);
            for (String sobrenome : sobrenomes) {
                String candidato = completar(prefixo, sobrenome, 7);
                if (candidato != null) candidatos.add(candidato);
            }
        }

        for (String sobrenome : sobrenomes) {
            for (int take = 1; take <= Math.min(7, sobrenome.length()); take++) {
                String prefixo = sobrenome.substring(0, take);
                String candidato = completar(prefixo, primeiroNome, 7);
                if (candidato != null) candidatos.add(candidato);
            }
        }

        for (String sobrenome : sobrenomes) {
            String combinado = primeiroNome + sobrenome;
            for (int i = 0; i <= 7 && i <= combinado.length() - 7; i++) {
                String candidato = combinado.substring(i, i + 7);
                if (candidato.matches("[a-z]{7}")) candidatos.add(candidato);
            }
        }

        String todos = String.join("", partes);
        for (int i = 0; i <= todos.length() - 7; i++) {
            String candidato = todos.substring(i, i + 7);
            if (candidato.matches("[a-z]{7}")) candidatos.add(candidato);
        }

        return candidatos.stream().distinct().toList();
    }

    private String completar(String prefixo, String fonte, int tamanho) {
        int necessario = tamanho - prefixo.length();
        if (necessario < 0) return prefixo.substring(0, tamanho);
        if (fonte.length() < necessario) return null;
        String resultado = prefixo + fonte.substring(0, necessario);
        return resultado.matches("[a-z]{" + tamanho + "}") ? resultado : null;
    }

    private String normalizar(String nome) {
        String nfd = Normalizer.normalize(nome.toLowerCase().trim(), Normalizer.Form.NFD);
        return nfd.replaceAll("[^a-z\\s]", "").replaceAll("\\s+", " ").trim();
    }
}
