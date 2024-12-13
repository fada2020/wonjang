package com.example.wonjang.repository;

import com.example.wonjang.model.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Integer> {
    int countByPin(boolean pin);
}
