package com.simuladortactico.repository;

import com.simuladortactico.model.Participa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipaRepository extends JpaRepository<Participa, Long> {
    List<Participa> findByAlineacionId(Long alineacionId);
    void deleteByAlineacionId(Long alineacionId);
}
