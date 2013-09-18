package com.springmvc.exemplo.infra.excecao;

/**
 * Representa exceções de infra-estrutura.
 */
@SuppressWarnings("serial")
public class InfraException extends RuntimeException {

    /**
     * Constrói uma nova InfraException.
     * 
     * @param cause Causa do erro.
     */
    public InfraException(Throwable cause) {
        super(cause);
    }

    /**
     * Constrói uma nova InfraException.
     * 
     * @param mensagem Mensagem de erro.
     * @param cause Causa do erro.
     */
    public InfraException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

    /**
     * Constrói uma nova InfraException com a mensagem de erro.
     * 
     * @param mensagem Mensagem de erro.
     */
    public InfraException(String mensagem) {
        super(mensagem);
    }
}