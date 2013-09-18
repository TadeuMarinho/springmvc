package com.springmvc.exemplo.infra.negocio;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.springmvc.exemplo.ConstantesMensagens;
import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.infra.mensagem.Mensagem;
import com.springmvc.exemplo.infra.mensagem.RotuloConverter;
import com.springmvc.exemplo.infra.persistencia.Filtro;
import com.springmvc.exemplo.infra.persistencia.ObjetoPersistente;
import com.springmvc.exemplo.infra.persistencia.ResultadoPaginado;
import com.springmvc.exemplo.infra.persistencia.hibernate.AbstractHibernateDAO;

/**
 * Define um Mediator no sistema.
 * 
 * @param <T> Tipo a ser persistido.
 * @param <E> Tipo utilizado na chave.
 */
@SuppressWarnings("serial")
public abstract class AbstractService<T extends ObjetoPersistente, ID extends Serializable>
        implements Serializable, IService<T, ID> {

	/** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void salvar(T obj) {
        validarInclusao(obj);
        getDao().salvar(obj);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void alterar(T obj) {
        validarAlteracao(obj);
        getDao().alterar(obj);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void excluir(T obj) {
        validarExclusao(obj);
        getDao().excluir(obj);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public T buscar(ID id) {
        return getDao().buscar(id);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public T buscarPorModelo(T modelo) {
        return getDao().buscarPorModelo(modelo);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public ResultadoPaginado<T> consultarPorFiltro(Filtro filtro) {
        return getDao().consultarPorFiltro(filtro);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public List<T> consultarPorCampo(String nomeCampo, Object valorCampo, String... atributosEOrdem) {
        return getDao().consultarPorCampo(nomeCampo, valorCampo, atributosEOrdem);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public T consultarUmPorCampo(String nomeCampo, Object valorCampo) {
        return getDao().consultarUmPorCampo(nomeCampo, valorCampo);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public ResultadoPaginado<T> consultarPorModelo(T modelo,
            Map<String, Boolean> criteriosOrdenacao, int primeiroElemento,
            int quantidadeMaximaResultados) {
        return getDao().consultarPorModelo(modelo, criteriosOrdenacao, primeiroElemento,
                quantidadeMaximaResultados);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public List<T> consultarPorModelo(T modelo) {
        return getDao().consultarPorModelo(modelo);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public Long getTotalRegistrosPorModelo(T modelo) {
        return getDao().getTotalRegistrosPorModelo(modelo);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    public List<T> listar(String... atributosEOrdem) {
        return getDao().listar(atributosEOrdem);
    };

    /**
     * Recupera o DAO a ser utilizado para realizar a comunicação com o mecanismo de persistência.
     * 
     * @return DAO a ser utilizado para realizar a comunicação com o mecanismo de persistência.
     */
    protected abstract AbstractHibernateDAO<T, ID> getDao();

    /** {@inheritDoc} */
    @Override
    public void validarInclusao(T obj) throws NegocioException {
        verificarNegocioException(obj.validar());
    };

    /** {@inheritDoc} */
    @Override
    public void validarAlteracao(T obj) throws NegocioException {
        verificarNegocioException(obj.validar());
    };

    /** {@inheritDoc} */
    @Override
    public void validarExclusao(T obj) throws NegocioException {
        // Implementado na subclasse caso necessário.
    };

    @Override
    public void verificarNegocioException(List<Mensagem> mensagens) throws NegocioException {
        if (!mensagens.isEmpty()) {
            throw new NegocioException(mensagens);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void validarDataMenorIgualQueDataAtual(LocalDate dataVerificacao, List<Mensagem> erros,
            String chaveCampo) {
        validarDataMenorIgualQueDataAtual(dataVerificacao, erros,
                ConstantesMensagens.ERRO_GERAL_CAMPO_DEVE_SER_ANTERIOR_DATA_ATUAL, chaveCampo);
    }

    /** {@inheritDoc} */
    @Override
    public void validarDataMenorIgualQueDataAtual(LocalDate dataVerificacao, List<Mensagem> erros,
            String mensagem, String chaveCampo) {
        if (dataVerificacao != null && LocalDate.now().isBefore(dataVerificacao)) {
            erros.add(new Mensagem(mensagem, recuperarLabel(chaveCampo)));
        }
    }

    /**
     * Recupera labels para informar nas mensagens.
     * 
     * @param chave chave do labels
     */
    protected String recuperarLabel(String chave) {
        return RotuloConverter.getInstance().getAsString(chave);
    }
}