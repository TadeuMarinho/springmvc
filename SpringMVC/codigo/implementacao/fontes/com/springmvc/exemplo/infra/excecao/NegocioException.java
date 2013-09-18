package com.springmvc.exemplo.infra.excecao;

import java.util.LinkedList;
import java.util.List;

import com.springmvc.exemplo.infra.mensagem.Mensagem;

/**
 * Representa exceções de negócio.
 */
@SuppressWarnings("serial")
public class NegocioException extends RuntimeException {

    private final List<Mensagem> mensagens = new LinkedList<Mensagem>();

    /**
     * Constrói uma exceção de negócio.
     * 
     * @param mensagem Mensagem de erro.
     * @param cause Causa do erro.
     */
    public NegocioException(final Mensagem mensagem, final Throwable causa) {
        super(mensagem.getChave(), causa);
        mensagens.add(mensagem);
    }

    /**
     * Constrói uma exceção de negócio.
     * 
     * @param chave Chave para recuperar a mensagem de erro.
     * @param causa Causa do erro.
     */
    public NegocioException(final String chave, final Throwable causa) {
        super(null, causa);
        List<String> parametros = null;
        adicionarErro(chave, parametros);
    }

    /**
     * Constrói uma exceção de negócio.
     * 
     * @param mensagens Lista de mensagens de erro.
     */
    public NegocioException(final List<Mensagem> mensagens) {
        super(null, null);
        this.mensagens.addAll(mensagens);
    }

    /**
     * Constrói uma exceção de negócio.
     * 
     * @param chave Chave para a mensagem.
     * @param mensagens Lista de mensagens de erro.
     */
    public NegocioException(final String chave, List<String> parametros) {
        super(null, null);
        adicionarErro(chave, parametros);
    }

    /**
     * Constrói uma exceção de negócio.
     * 
     * @param chave Chave para recuperar a mensagem de erro.
     * @param parametros Parâmetros para configuração da mensagem de erro.
     */
    public NegocioException(final String chave, final String... parametros) {
        super(null, null);
        adicionarErro(chave, parametros);
    }

    /**
     * Recupera a lista de mensagens de erro.
     * 
     * @return Lista de mensagens de erro.
     */
    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    /**
     * Recupera a mensagem de erro.
     * 
     * @return Mensagem de erro.
     */
    public String getMensagem() {
        String mensagem = super.getMessage();
        if (mensagem == null && !mensagens.isEmpty()) {
            mensagem = mensagens.get(0).toString();
        }
        return mensagem;
    }

    private void adicionarErro(final String chave, final List<String> parametros) { //NOPMD Bug Varargs
        mensagens.add(new Mensagem(chave, parametros));
    }

    private void adicionarErro(final String chave, final String... parametros) {
        mensagens.add(new Mensagem(chave, parametros));
    }
}