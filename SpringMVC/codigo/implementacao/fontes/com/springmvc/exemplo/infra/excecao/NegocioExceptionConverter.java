package com.springmvc.exemplo.infra.excecao;

import com.springmvc.exemplo.infra.mensagem.MensagemConverter;

public final class NegocioExceptionConverter {

    private static final NegocioExceptionConverter INSTANCE;

    static {
        INSTANCE = new NegocioExceptionConverter();
    }

    private NegocioExceptionConverter() {
        // Apenas para não deixar construir.
    }

    public static NegocioExceptionConverter getInstance() {
        synchronized (INSTANCE) {
            return INSTANCE;
        }
    }

    public String[] getAsString(NegocioException negocioException) {
        int qtdMensagens = negocioException.getMensagens().size();
        String[] mensagens = new String[qtdMensagens];

        for (int i = 0; i < qtdMensagens; i++) {
            mensagens[i] =
                    MensagemConverter.getInstance().getAsString(
                            negocioException.getMensagens().get(i));
        }

        return mensagens;
    }
}