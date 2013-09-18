package com.springmvc.exemplo.infra.persistencia;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class Filtro extends HashMap<String, Object> {

    public static final String VALOR_NULO = "__NULL_VALUE__";
    public static final String VALOR_NAO_NULO = "__NOT_NULL_VALUE__";

    private Map<String, Boolean> ordem;
    private int primeiroElemento;
    private int quantidadeMaximaResultados;

    public Filtro() {
        primeiroElemento = 0;
        quantidadeMaximaResultados = Integer.MAX_VALUE;
        ordem = new HashMap<String, Boolean>();
    }

    public Filtro(Filtro filtro) {
        this();
        putAll(filtro);
        ordem.putAll(filtro.getOrdem());
        primeiroElemento = filtro.getPrimeiroElemento();
        quantidadeMaximaResultados = filtro.getQuantidadeMaximaResultados();
    }

    public int getPrimeiroElemento() {
        return primeiroElemento;
    }

    public void setPrimeiroElemento(int primeiroElemento) {
        this.primeiroElemento = primeiroElemento;
    }

    public int getQuantidadeMaximaResultados() {
        return quantidadeMaximaResultados;
    }

    public void setQuantidadeMaximaResultados(int quantidadeMaximaResultados) {
        this.quantidadeMaximaResultados = quantidadeMaximaResultados;
    }

    public void adicionarOrdem(String attribute) {
        adicionarOrdem(attribute, true);
    }

    public void adicionarOrdem(String attribute, Boolean ascending) {
        if (StringUtils.isNotBlank(attribute) && !"null".equalsIgnoreCase(attribute)) {
            ordem.put(attribute, ascending);
        }
    }

    public Map<String, Boolean> getOrdem() {
        return ordem;
    }

    public void setOrdem(Map<String, Boolean> ordem) {
        this.ordem = ordem;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("rawtypes")
    public final Object put(String chave, Object valor) {
        if (valor == null || valor instanceof String && StringUtils.isBlank((String) valor)
                || valor instanceof Collection && ((Collection) valor).isEmpty()
                || valor instanceof Map && ((Map) valor).isEmpty()) {
            remove(chave);
            return null;
        }
        return super.put(chave, valor);
    }

    public final Object putValorNulo(String chave) {
        return super.put(chave, VALOR_NULO);
    }

    public final Object putValorNaoNulo(String chave) {
        return super.put(chave, VALOR_NAO_NULO);
    }
}