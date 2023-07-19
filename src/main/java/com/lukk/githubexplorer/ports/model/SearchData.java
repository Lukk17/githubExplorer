package com.lukk.githubexplorer.ports.model;

import java.time.LocalDate;

public record SearchData(String language, LocalDate since, int count) {
}
