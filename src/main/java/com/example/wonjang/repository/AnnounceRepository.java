package com.example.wonjang.repository;

import com.example.wonjang.model.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announce, Integer> {
    int countByPin(boolean pin);

    List<Announce> findByPin(boolean pin);
}
