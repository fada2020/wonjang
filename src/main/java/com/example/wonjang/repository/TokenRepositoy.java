package com.example.wonjang.repository;

import com.example.wonjang.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepositoy extends JpaRepository<Token, Long> {
}
