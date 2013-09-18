package com.springmvc.exemplo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextHibernateValidacaoMapeamento.xml"})
public class ValidacaoMapeamentoTest {

    /**
     * Dispara a validação do hibernate com o objetivo de checar se o 
     * mapeamento atual está conforme o estado do banco.
     */
    @Test
    public void validacao() {
        Assert.assertTrue(true);
    }
}