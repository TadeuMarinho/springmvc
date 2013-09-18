package com.springmvc.exemplo;

import java.util.List;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.infra.mensagem.Mensagem;
import com.springmvc.exemplo.infra.mensagem.RotuloConverter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContextUnitTest.xml"})
public abstract class AbstractTest {

    /**
     * Verifica se a mensagem informada faz parte das mensagens de um NegocioException.
     * 
     * @param negocioException NegocioException com a lista de mensagens.
     * @param mensagem Mensagem a se verificada na lista de mensagens da exceção.
     */
    public static void assertMensagemComChaveParametros(NegocioException negocioException,
            Mensagem mensagem) {
    	
        assertMensagemComChaveParametros(negocioException.getMensagens(), mensagem);
    }

    /**
     * Verifica se a mensagem informada faz parte das mensagens da lista de erros.
     * 
     * @param erros Lista de mensagens.
     * @param mensagem Mensagem a se verificada na lista de mensagens da exceção.
     */
    public static void assertMensagemComChaveParametros(List<Mensagem> erros, Mensagem mensagem) {
        Assert.assertTrue("A lista de erros não deveria estar vazia.", !erros.isEmpty());
        Assert.assertTrue("A lista não possui a exceção informada. ", erros.contains(mensagem));
    }

    /**
     * Verifica se a mensagem informada faz parte das mensagens da lista de erros.
     * 
     * @param erros Lista de mensagens.
     * @param mensagem Mensagem a se verificada na lista de mensagens da exceção.
     */
    public static void assertMensagemComChaveParametros(List<Mensagem> erros, String chave,
            String... parametros) {
        Mensagem mensagem = new Mensagem(chave, parametros);
        assertMensagemComChaveParametros(erros, mensagem);
    }

    public String recuperarRotulo(String chave) {
        return RotuloConverter.getInstance().getAsString(chave);
    }
}