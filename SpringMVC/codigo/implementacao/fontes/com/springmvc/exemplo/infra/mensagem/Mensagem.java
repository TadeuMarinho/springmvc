package com.springmvc.exemplo.infra.mensagem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Mensagem composta por uma chave e parâmetros.
 */
public class Mensagem {

    private String chave;
    private List<String> parametros = new ArrayList<String>();

    public Mensagem() {
        // Construtor padrão
    }

    public Mensagem(String chave, List<String> parametros) {
        validarChave(chave);
        this.chave = chave;
        this.parametros = parametros;
    }

    public Mensagem(String chave, String... parametros) {
        validarChave(chave);
        this.chave = chave;
        for (int i = 0; i < parametros.length; i++) {
            this.parametros.add(parametros[i]);
        }
    }

    private void validarChave(String chave) {
        if (chave == null) {
            throw new IllegalArgumentException("A chave não pode ser nula!");
        }
    }

    /**
     * Recupera a chave da mensagem.
     * 
     * @return Chave da mensagem.
     */
    public String getChave() {
        return chave;
    }

    /**
     * Configura a chave da mensagem.
     * 
     * @param chave Chave da mensagem.
     */
    public void setChave(String chave) {
        this.chave = chave;
    }

    /**
     * Recupera os parâmetros da mensagem.
     * 
     * @return Parâmetros da mensagem.
     */
    public List<String> getParametros() {
        return parametros;
    }

    /**
     * Configura os parâmetros da mensagem.
     * 
     * @param parametros Parâmetros da mensagem.
     */
    public void setParametros(List<String> parametros) {
        this.parametros = parametros;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        boolean iguais = false;
        if (obj == this) {
            iguais = true;
        } else if (obj instanceof Mensagem) {
            Mensagem objeto = (Mensagem) obj;
            iguais =
                    new EqualsBuilder().append(chave, objeto.chave)
                            .append(parametros, objeto.getParametros()).isEquals();
        }
        return iguais;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(chave).append(parametros).toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuffer retorno = new StringBuffer(chave);
        for (String parametro : parametros) {
            retorno.append(" - ");
            retorno.append(parametro);
        }

        return retorno.toString();
    }
}