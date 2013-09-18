package com.springmvc.exemplo.infra.negocio;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.infra.mensagem.Mensagem;
import com.springmvc.exemplo.infra.persistencia.Filtro;
import com.springmvc.exemplo.infra.persistencia.ObjetoPersistente;
import com.springmvc.exemplo.infra.persistencia.ResultadoPaginado;

public interface IService<T extends ObjetoPersistente, ID extends Serializable> extends
        Serializable {

    /**
     * Realiza as valida��es necess�rias para inclus�o e repassa o objeto a ser persistido para o
     * DAO.
     * 
     * @param obj Objeto a ser validado e repassado para o DAO.
     */
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    void salvar(T obj);

    /**
     * Realiza as valida��es necess�rias para altera��o e repassa o objeto a ser alterado para o
     * DAO.
     * 
     * @param obj Objeto a ser validado e repassado para o DAO.
     */
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    void alterar(T obj);

    /**
     * Realiza as valida��es necess�rias para exclus�o e repassa o objeto a ser exclu�do para o
     * DAO.
     * 
     * @param obj Objeto a ser validado e repassado para o DAO.
     */
    @Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    void excluir(T obj);

    /**
     * Realiza a busca pelo identificador do objeto.
     * 
     * @param id identificador utilizado na consulta.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    T buscar(ID id);

    /**
     * Realiza a busca pelo modelo do objeto
     * 
     * @param modelo Modelo utilizado no filtro da consulta.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    T buscarPorModelo(T modelo);

    /**
     * Realiza a consulta por filtro, ordenando e paginando.
     * 
     * @param filtro Filtro utilizado na consulta.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    ResultadoPaginado<T> consultarPorFiltro(Filtro filtro);

    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    List<T> consultarPorCampo(String nomeCampo, Object valorCampo, String... atributosEOrdem);

    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    T consultarUmPorCampo(String nomeCampo, Object valorCampo);

    /**
     * Realiza a busca pelo modelo do objeto, ordenando e paginando.
     * 
     * @param modelo Modelo utilizado no filtro da consulta.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    ResultadoPaginado<T> consultarPorModelo(T modelo, Map<String, Boolean> criteriosOrdenacao,
            int primeiroElemento, int quantidadeMaximaResultados);

    /**
     * Realiza a busca pelo modelo do objeto.
     * 
     * @param modelo Modelo utilizado no filtro da consulta.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    List<T> consultarPorModelo(T modelo);

    /**
     * Retorna o total de registros para a consulta no criteria.
     * 
     * @param modelo Exemplo a ser usado como filtro na consulta.
     * @return Long O total de registros para a consulta.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    Long getTotalRegistrosPorModelo(T modelo);

    /**
     * Lista as entidades do banco.
     * 
     * @param atributosEOrdem ordena��o do resultado.<br/>
     *        Ex: propriedade:ordem -> nome:asc
     * 
     * @return uma lista com todas as entidades do banco.
     */
    @Transactional(isolation = Isolation.DEFAULT, readOnly = true)
    List<T> listar(String... atributosEOrdem);

    /**
     * Valida��o realizada antes da inclus�o.
     * 
     * @param obj Objeto a ser validado.
     * @throws NegocioException Caso alguma valida��o falhe.
     */
    void validarInclusao(T obj) throws NegocioException;

    /**
     * valida��o realizada antes da altera��o.
     * 
     * @param obj Objeto a ser validado.
     * @throws NegocioException Caso alguma valida��o falhe.
     */
    void validarAlteracao(T obj) throws NegocioException;

    /**
     * valida��o realizada antes da exclus�o.
     * 
     * @param obj Objeto a ser validado.
     * @throws NegocioException Caso alguma valida��o falhe.
     */
    void validarExclusao(T obj) throws NegocioException;

    /**
     * Verifica se houve mensagem na lista lan�ando um NegocioException
     * 
     * @param mensagens Lista de mensagens de erro.
     * @throws NegocioException Caso exista mensagem na lista.
     */
    void verificarNegocioException(List<Mensagem> mensagens) throws NegocioException;

    /**
     * Verifica se data passada como par�metro � menor ou igual a data atual, retornando um true se
     * for menor ou igual
     * 
     * @param dataVerificacao data para verifica��o
     * @param chaveCampo chave do atributo da classe
     * @param erros lista de erros para valida��es
     */
    void validarDataMenorIgualQueDataAtual(LocalDate dataVerificacao, List<Mensagem> erros,
            String chaveCampo);

    /**
     * Verifica se data passada como par�metro � menor ou igual a data atual, retornando um true se
     * for menor ou igual
     * 
     * @param dataVerificacao data para verifica��o
     * @param chaveCampo chave do atributo da classe
     * @param erros lista de erros para valida��es
     * @param mensagem mensagem a ser exibida.
     */
    void validarDataMenorIgualQueDataAtual(LocalDate dataVerificacao, List<Mensagem> erros,
            String mensagem, String chaveCampo);
}