package com.intranet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.models.Auditoria;

public interface IRepoAuditoria extends JpaRepository<Auditoria, Integer> {

}

