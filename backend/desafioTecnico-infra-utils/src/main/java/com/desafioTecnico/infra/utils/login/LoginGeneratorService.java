package com.desafioTecnico.infra.utils.login;

import com.desafioTecnico.application.port.LoginGeneratorPort;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Login;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginGeneratorService implements LoginGeneratorPort {

    @Override
    public Login generate(String fullName, List<String> existingLogins) {
        Set<String> taken = existingLogins.stream().collect(Collectors.toSet());
        String normalized = normalize(fullName);
        String[] parts = normalized.split("\\s+");

        List<String> candidates = buildCandidates(parts);

        for (String candidate : candidates) {
            if (!taken.contains(candidate)) {
                return Login.of(candidate);
            }
        }

        String allChars = String.join("", parts);
        for (int offset = 0; offset < allChars.length(); offset++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                sb.append(allChars.charAt((offset + i) % allChars.length()));
            }
            String candidate = sb.toString();
            if (candidate.matches("[a-z]{7}") && !taken.contains(candidate)) {
                return Login.of(candidate);
            }
        }

        throw new DomainException("Could not generate a unique login for: " + fullName);
    }

    private List<String> buildCandidates(String[] parts) {
        List<String> candidates = new ArrayList<>();
        String firstName = parts[0];
        List<String> surnames = Arrays.asList(parts).subList(1, parts.length);

        for (int take = 1; take <= Math.min(7, firstName.length()); take++) {
            String prefix = firstName.substring(0, take);
            for (String surname : surnames) {
                String candidate = pad(prefix, surname, 7);
                if (candidate != null) candidates.add(candidate);
            }
        }

        for (String surname : surnames) {
            for (int take = 1; take <= Math.min(7, surname.length()); take++) {
                String prefix = surname.substring(0, take);
                String candidate = pad(prefix, firstName, 7);
                if (candidate != null) candidates.add(candidate);
            }
        }

        for (String surname : surnames) {
            String combined = firstName + surname;
            for (int i = 0; i <= 7 && i <= combined.length() - 7; i++) {
                String candidate = combined.substring(i, i + 7);
                if (candidate.matches("[a-z]{7}")) candidates.add(candidate);
            }
        }

        String all = String.join("", parts);
        for (int i = 0; i <= all.length() - 7; i++) {
            String candidate = all.substring(i, i + 7);
            if (candidate.matches("[a-z]{7}")) candidates.add(candidate);
        }

        return candidates.stream().distinct().toList();
    }

    private String pad(String prefix, String source, int targetLength) {
        int needed = targetLength - prefix.length();
        if (needed < 0) return prefix.substring(0, targetLength);
        if (source.length() < needed) return null;
        String result = prefix + source.substring(0, needed);
        return result.matches("[a-z]{" + targetLength + "}") ? result : null;
    }

    private String normalize(String name) {
        String nfd = Normalizer.normalize(name.toLowerCase().trim(), Normalizer.Form.NFD);
        return nfd.replaceAll("[^a-z\\s]", "").replaceAll("\\s+", " ").trim();
    }
}
