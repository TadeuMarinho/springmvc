package com.springmvc.exemplo.infra.persistencia;

import java.io.Serializable;
import java.util.List;

public class ResultadoPaginado<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> lista;
    private int qtdTotalRegistros;

    public ResultadoPaginado(List<T> lista, int qtdTotalRegistros) {
        this.lista = lista;
        this.qtdTotalRegistros = qtdTotalRegistros;
    }

    public List<T> getLista() {
        return this.lista;
    }

    public int getQtdTotalRegistros() {
        return this.qtdTotalRegistros;
    }

    public boolean isVazia() {
        return this.qtdTotalRegistros == 0;
    }
}