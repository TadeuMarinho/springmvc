package com.springmvc.exemplo.controlador;

import java.util.Arrays;

import org.springframework.ui.Model;

import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.infra.excecao.NegocioExceptionConverter;
import com.springmvc.exemplo.infra.mensagem.Mensagem;
import com.springmvc.exemplo.infra.mensagem.MensagemConverter;

public abstract class AbstractViewController {

//    private static final Locale LOCALE = new Locale("pt", "BR");
//    private static final String DATA_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
	
    /**
     * Adiciona as mensagens de erro no modelo para serem apresentadas na tela.
     * 
     * @param modelo Modelo no qual as mensagens de erro serão adicionadas.
     * @param excecao Exceção com as mensagens a serem apresentadas na tela.
     */
    public void adicionarMensagemErro(Model modelo, NegocioException excecao) {
        adicionarMensagem(modelo, "erros",
                NegocioExceptionConverter.getInstance().getAsString(excecao));
    }

    /**
     * Adiciona a mensagem de info no modelo para ser apresentada na tela.
     * 
     * @param modelo Modelo no qual a mensagem de info será adicionada.
     * @param excecao Mensagem com a mensagem a ser apresentada na tela.
     */
    public void adicionarMensagemInfo(Model modelo, Mensagem mensagem) {
        adicionarMensagem(modelo, "infos", MensagemConverter.getInstance().getAsString(mensagem));
    }

    private void adicionarMensagem(Model model, String nome, String... detalhes) {
        model.addAttribute(nome, Arrays.asList(detalhes));
    }
}