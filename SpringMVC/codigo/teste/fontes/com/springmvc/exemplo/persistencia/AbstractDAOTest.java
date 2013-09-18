package com.springmvc.exemplo.persistencia;

import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.springmvc.exemplo.AbstractTest;

@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public abstract class AbstractDAOTest extends AbstractTest {
	
	// Adiciona o teste no contexto transacional dentro do DAO já que os mesmos estarão mokados no servico.
	
	public abstract void salvar();
	public abstract void alterar();
	public abstract void excluir();
	
}