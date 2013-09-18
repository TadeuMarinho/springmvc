package com.springmvc.exemplo.infra.persistencia;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.joda.time.LocalDate;

/**
 * Define um objeto persistente auditável no sistema.
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class ObjetoPersistenteAuditavel extends ObjetoPersistente {

    @Column(name = "dataAtualizacao", nullable = false)
    private LocalDate dataAtualizacao;

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}