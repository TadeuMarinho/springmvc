package com.springmvc.exemplo.infra.persistencia;

import com.springmvc.exemplo.ConstantesMensagens;
import com.springmvc.exemplo.infra.excecao.NegocioException;

@SuppressWarnings("serial")
public class ObjetoInexistenteException extends NegocioException {

    public ObjetoInexistenteException() {
        super(ConstantesMensagens.ERRO_GERAL_OBJETO_INEXISTENTE, (Throwable) null);
    }

    public ObjetoInexistenteException(Exception e) {
        super(ConstantesMensagens.ERRO_GERAL_OBJETO_INEXISTENTE, e);
    }

    public ObjetoInexistenteException(String chave, Exception e) {
        super(chave, e);
    }
}