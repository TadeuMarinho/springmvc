package com.springmvc.exemplo.infra.persistencia.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.sql.JoinType;

import com.springmvc.exemplo.ConstantesMensagens;
import com.springmvc.exemplo.infra.ColecaoChecada;
import com.springmvc.exemplo.infra.excecao.InfraException;
import com.springmvc.exemplo.infra.excecao.NegocioException;
import com.springmvc.exemplo.infra.persistencia.Filtro;
import com.springmvc.exemplo.infra.persistencia.HibernateSpringUtils;
import com.springmvc.exemplo.infra.persistencia.ObjetoInexistenteException;
import com.springmvc.exemplo.infra.persistencia.ObjetoPersistente;
import com.springmvc.exemplo.infra.persistencia.ResultadoPaginado;

/**
 * Define um DAO no sistema.
 * 
 * @param <T> Tipo a ser persistido.
 */
public abstract class AbstractHibernateDAO<T extends ObjetoPersistente, ID extends Serializable> implements
        Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PROP_ENT = "ent";
    private static final String PROP_ID = "id";
    private static final String UNCHECKED = "unchecked";

    /**
     * Recupera a classe persistente utilizada.
     * 
     * @return Classe persistente utilizada.
     */
    protected abstract Class<T> getClassePersistente();

    /**
     * Persiste um objeto no banco de dados.
     * 
     * @param obj Objeto a ser persistido no baco de dados.
     */
    @SuppressWarnings("unchecked")
	public ID salvar(T obj) {
        return (ID) getCurrentSession().save(obj);
    }

    /**
     * Altera um objeto no banco de dados.
     * 
     * @param obj Objeto a ser alterado no banco de dados.
     */
    public void alterar(T obj) {
        getCurrentSession().update(obj);
    }

    /**
     * Mantêm um objeto no banco de dados, o hibernate verifica se o objeto será inserido ou
     * atualizado.
     * 
     * @param obj Objeto a ser mantido no banco de dados.
     */
    public void salvarOuAlterar(T obj) {
        getCurrentSession().saveOrUpdate(obj);
    }

    /**
     * Exclui um objeto do banco de dados.
     * 
     * @param obj Objeto a ser excluído do banco de dados.
     */
    public void excluir(T obj) {
    	getCurrentSession().delete(obj);
    }

    /**
     * Verifica se existe objeto persistido no banco de dados com o identificador informado.
     * 
     * @param id Identificador do objeto.
     * @return True caso exista, false caso contrário.
     */
    public boolean existe(ID id) {
        Criteria criteria = createCriteria(getClassePersistente());
        criteria.add(Restrictions.idEq(id));
        return getTotalRegistros(criteria) > 0;
    }

    /**
     * Recupera o objeto com o identificador informado.
     * 
     * @param id Identificador do objeto.
     * @return Objeto com o identificador igual ao informado.
     * @throws ObjetoInexistenteException Caso não exista objeto com o identificador informado.
     */
    public T buscar(ID id) throws ObjetoInexistenteException {
        return getClassePersistente().cast(getCurrentSession().get(getClassePersistente(), id));
    }

    /**
     * Recupera o objeto do tipo da classe informada com o identificador informado.
     * 
     * @param id Identificador do objeto.
     * @param classePersistente Classe do objeto.
     * @return Objeto com classe e identificador informados.
     * @throws ObjetoInexistenteException Caso não exista objeto com os dados informados.
     */
    public <D extends ObjetoPersistente> D buscar(Integer id, Class<D> classePersistente)
            throws ObjetoInexistenteException {
        return classePersistente.cast(getCurrentSession().get(classePersistente, id));
    }

    /**
     * Recupera o objeto com o identificador informado sem adicioná-lo na sessão.
     * 
     * @param Identificador do objeto.
     * @return Objeto desconectado com dados informados
     */
    public T buscarDesconectado(ID id) {
        T entidade =
                getClassePersistente().cast(getCurrentSession().get(getClassePersistente(), id));
        if (entidade != null) {
            desconectar(entidade);
        }
        return entidade;
    }

    /**
     * Recupera o objeto com o identificador informado verificando se o mesmo ainda existe. Esse
     * método deve ser usado em situações onde o objeto pode ter sido removido da base por outro
     * usuário. Ex: uma tela de detalhe.
     * 
     * @param Identificador do objeto.
     * @return Objeto desconectado com dados informados
     */
    public T buscarVerificandoConcorrencia(ID id) {
        T registro =
                getClassePersistente().cast(getCurrentSession().get(getClassePersistente(), id));
        if (registro == null) {
            throw new NegocioException(ConstantesMensagens.ERRO_GERAL_ACESSO_CONCORRENTE);
        }
        return registro;
    }

    /**
     * Recupera todos os registros de uma tabela.
     * 
     * @return Lista contendo todos os registros.
     */
    public List<T> listar(String... atributosEOrdem) {
        Criteria criteria = createCriteria();

        if (atributosEOrdem.length > 0) {
            Map<String, Boolean> mapaOrdem = criarOrdemMap(atributosEOrdem);
            adicionarOrdenacao(criteria, mapaOrdem);
        }

        return ColecaoChecada.listaChecada(criteria.list(), getClassePersistente());
    }

    /**
     * Recupera uma lista de objetos do tipo T de acordo com o exemplo do mesmo tipo passado como
     * parâmetro.
     * 
     * @param modelo Exemplo a ser usado como filtro na consulta.
     * @param primeiroElemento Primeiro elemento a ser retornado para a página.
     * @param quantidadeMaximaResultados Quantidade máxima de resultados por página.
     * @return Lista de objetos do tipo T de acordo com o exemplo do mesmo tipo passado como
     *         parâmetro.
     */
    public ResultadoPaginado<T> consultarPorModelo(T modelo,
            Map<String, Boolean> criteriosOrdenacao, int primeiroElemento,
            int quantidadeMaximaResultados) {

        Example example = Example.create(modelo).enableLike(MatchMode.ANYWHERE).ignoreCase();

        Criteria criteria = createCriteria(getClassePersistente()).add(example);
        adicionarOrdenacao(criteria, criteriosOrdenacao);
        criteria.setFirstResult(primeiroElemento);
        criteria.setMaxResults(quantidadeMaximaResultados);

        Criteria criteriaQuantidade = createCriteria(getClassePersistente()).add(example);
        criteriaQuantidade.setProjection(Projections.rowCount());
        Integer quantidade = Integer.valueOf(criteriaQuantidade.uniqueResult().toString());

        List<T> resultado = ColecaoChecada.listaChecada(criteria.list(), getClassePersistente());

        return new ResultadoPaginado<T>(resultado, quantidade);
    }

    /**
     * Recupera uma lista de objetos do tipo T de acordo com o exemplo do mesmo tipo passado como
     * parâmetro.
     * 
     * @param modelo Exemplo a ser usado como filtro na consulta.
     * @return Lista de objetos do tipo T de acordo com o exemplo do mesmo tipo passado como
     *         parâmetro.
     * 
     */
    public List<T> consultarPorModelo(T modelo) {
        Criteria criteria = prepararCriteriaComExemplo(modelo);
        return ColecaoChecada.listaChecada(criteria.list(), getClassePersistente());
    }

    /**
     * Retorna o total de registros para a consulta no criteria.
     * 
     * @param modelo Exemplo a ser usado como filtro na consulta.
     * @return Long O total de registros para a consulta.
     */
    public Long getTotalRegistrosPorModelo(T modelo) {
        Criteria criteria = prepararCriteriaComExemplo(modelo);
        return getTotalRegistros(criteria);
    }

    /**
     * Recupera uma lista de objetos do tipo T de acordo com o exemplo do mesmo tipo passado como
     * parâmetro.
     * 
     * @param modelo Exemplo a ser usado como filtro na consulta.
     * @return Lista de objetos do tipo T de acordo com o exemplo do mesmo tipo passado como
     *         parâmetro.
     * 
     */
    @SuppressWarnings(UNCHECKED)
    public T buscarPorModelo(T modelo) {
        Criteria criteria = prepararCriteriaComExemplo(modelo);
        return (T) criteria.uniqueResult();
    }

    private Criteria prepararCriteriaComExemplo(T modelo) {
        Example example = Example.create(modelo).enableLike(MatchMode.ANYWHERE).ignoreCase();
        return createCriteria(getClassePersistente()).add(example);
    }

    /**
     * Cria um objeto do tipo Criteria com a classe persistente do DAO.
     * 
     * @return um objeto do tipo Criteria com a classe persistente do DAO.
     */
    protected Criteria createCriteria() {
        Session currentSession = getCurrentSession();
        return currentSession.createCriteria(getClassePersistente());
    }

    /**
     * Cria uma query hql.
     * 
     * @param hql a query escrita.
     * 
     * @return um objeto do tipo Query.
     */
    protected Query createQuery(String hql) {
        Session currentSession = getCurrentSession();
        return currentSession.createQuery(hql);
    }

    /**
     * Cria um objeto do tipo Criteria com a classe persistente do DAO e um alias passado como
     * parâmetro.
     * 
     * @return um objeto do tipo Criteria com a classe persistente do DAO.
     */
    protected Criteria createCriteria(String alias) {
        return getCurrentSession().createCriteria(getClassePersistente(), alias);
    }

    /**
     * Cria um objeto do tipo Criteria com a classe passada como parâmetro e um alias também
     * passado como parâmetro.
     * 
     * @return um objeto do tipo Criteria.
     */
    protected Criteria createCriteria(Class<?> classe, String alias) {
        return getCurrentSession().createCriteria(classe, alias);
    }

    /**
     * Cria um objeto do tipo Criteria com a classe passada como parâmetro.
     * 
     * @return um objeto do tipo Criteria.
     */
    protected Criteria createCriteria(Class<?> classe) {
        return getCurrentSession().createCriteria(classe);
    }

    /**
     * Cria um objeto do tipo Criteria com a classe persistente do DAO e altera o cacheMode da
     * sessÃ£o com o parâmetro passado
     * 
     * @return um objeto do tipo Criteria.
     */
    protected Criteria createCriteria(CacheMode cacheMode) {
        getCurrentSession().setCacheMode(cacheMode);
        Criteria criteria = createCriteria();
        criteria.setCacheMode(cacheMode);
        return criteria;
    }

    /**
     * Remove a instância da sessão hibernate. Para que a ação remova os relacionamentos em cascata
     * é necessário marcá-los com cascade="evict".
     * 
     * @param obj Objeto a ser desconectado da sessão.
     */
    public <D extends Serializable> void desconectar(D obj) {
        if (getCurrentSession().contains(obj)) {
            getCurrentSession().evict(obj);
        }
    }

    /**
     * Remove uma lista de objetos da sessão hibernate. Para que a ação remova os relacionamentos
     * em cascata é necessário marcá-los com cascade="evict".
     * 
     * @param obj Objeto a ser desconectado da sessÃ£o.
     */
    public <D extends Serializable> void desconectar(List<D> objs) {
        if (objs != null && !objs.isEmpty()) {
            for (D obj : objs) {
                getCurrentSession().evict(obj);
            }
        }
    }

    /**
     * Atualiza o objeto na sessão com os dados atuais da base de dados.
     * 
     * @param obj objeto a ser atualizado.
     */
    public <D extends Serializable> void reconectar(D obj) {
        getCurrentSession().refresh(obj);
    }

    /**
     * Libera os dados da sessão do Hibernate para a base de dados. Deve apenas ser utilizado em
     * situações especí­ficas.
     */
    public void flush() {
        getCurrentSession().flush();
    }

    /**
     * Adiciona ordenação a uma criteria.
     * 
     * @param propriedade Propriedade utilizada na ordenação.
     * @param ascendente true caso ascendente, false caso descendente.
     * @param criteria Criteria na qual a ordenação será adicionada.
     */
    protected void adicionarOrdenacao(String propriedade, boolean ascendente, Criteria criteria) {
        if (ascendente) {
            criteria.addOrder(Order.asc(propriedade).ignoreCase());
        } else {
            criteria.addOrder(Order.desc(propriedade).ignoreCase());
        }
    }

    /**
     * Adiciona ordenação a uma criteria.
     * 
     * @param criteria Criteria na qual a ordenação será adicionada.
     * @param propriedades Propriedades utilizadas na ordenação.
     */
    protected void adicionarOrdenacao(Criteria criteria, Map<String, Boolean> propriedades) {

        if (propriedades != null) {
            for (Entry<String, Boolean> entry : propriedades.entrySet()) {
                adicionarOrdenacao(entry.getKey(), entry.getValue(), criteria);
            }
        }
    }

    /**
     * Verifica se existe alguma associação com a entidade informada.
     * 
     * @param <U> Tipo da entidade
     * 
     * @param entidade entidade a ser verificada na consulta.
     * @param classeEntidadeAssociada classe da entidade que pode estar associada com outra.
     * @param fieldsProximaSuperClasse indicador se vai usar a próxima super classe para obter os
     *        fields.
     * @return true caso exista algum registro da classe informada (cadastrado no banco) que esteja
     *         associado com a entidade informada.
     */
    protected <U extends ObjetoPersistente> boolean existeEntidadeVinculada(U entidade, ID chave,
            Class<?> classeEntidadeAssociada, boolean fieldsProximaSuperClasse) {

        Criteria criteria = createCriteria(classeEntidadeAssociada);
        Field[] declaredFields = classeEntidadeAssociada.getDeclaredFields();
        if (fieldsProximaSuperClasse) {
            declaredFields = classeEntidadeAssociada.getSuperclass().getDeclaredFields();
        }
        int cont = 0;
        Disjunction disjunction = Restrictions.disjunction();
        for (Field field : declaredFields) {
            if (entidade.getClass().getName().toString().startsWith(field.getType().getName())) {
                cont++;
                String propriedadeEntidade = field.getName();
                criteria.createAlias(propriedadeEntidade, PROP_ENT + cont);
                disjunction.add(Restrictions.eq(propriedadeEntidade + "." + PROP_ID, chave));
            }
        }
        criteria.add(disjunction);

        return validarDisjunction(criteria, cont);
    }

    private boolean validarDisjunction(Criteria criteria, int cont) {
        if (cont == 0) {
            throw new IllegalArgumentException(
                    "A classe informada não possui atributo igual a classe da entidade informada.");
        }
        return getTotalRegistros(criteria) > 0;
    }

    /**
     * Recupera a sessão corrente.
     * 
     * @return Sessão corrente.
     */
    public Session getCurrentSession() {
    	return HibernateSpringUtils.getSessionFactory().getCurrentSession();
    }

    /**
     * Limpa as informações das tabelas passada como parâmetro.
     * 
     * @param tables tabelas a serem excluídas.
     */
    public void clearTableData(String... tables) {
        for (String table : tables) {
            getCurrentSession().createSQLQuery("delete from " + table).executeUpdate();
            getCurrentSession().flush();
        }
    }

    /**
     * Recupera o total de registros a partir de uma criteria.
     * 
     * @param criteria Criteria utilizada.
     */
    public Long getTotalRegistros(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    /**
     * Uso do filtro:<br/>
     * 
     * Filtrando valores indesejados: filtro.put("!campo", "valor");<br/>
     * Por padrão, esse método procura campos string usando MatchMode.ANYWHERE,<br/>
     * se quiser usar MatchMode.EXACT você deve usar "==" <br/>
     * no início do nome do campo: filtro.put("==campo", "valor");<br/>
     * Você pode usar a "!" e "==", ao mesmo tempo: filtro.put("!==campo", "valor");
     * 
     * @param filtro Filtro para consulta.
     * @return Resultado paginado com lista de T e quantidade total de registros.
     */
    @SuppressWarnings(UNCHECKED)
    public ResultadoPaginado<T> consultarPorFiltro(Filtro filtro) {

        Criteria quantidadeCriteria = createCriteria(getClassePersistente());
        adicionarCampos(filtro, quantidadeCriteria);
        int quantidade = getTotalRegistros(quantidadeCriteria).intValue();

        List<T> resultado = new ArrayList<T>();
        if (quantidade > 0) {
            Criteria criteria = createCriteria(getClassePersistente());
            Set<String> aliases = adicionarCampos(filtro, criteria);
            adicionarOrdenacao(criteria, filtro.getOrdem(), aliases);
            criteria.setFirstResult(filtro.getPrimeiroElemento());
            criteria.setMaxResults(filtro.getQuantidadeMaximaResultados());
            resultado = criteria.list();
        }

        return new ResultadoPaginado<T>(resultado, quantidade);
    }

    @SuppressWarnings("unchecked")
    public List<T> consultarPorCampo(String nomeCampo, Object valorCampo, String... atributosEOrdem) {
        Criteria criteria = createCriteria(getClassePersistente());

        if (atributosEOrdem.length > 0) {
            Map<String, Boolean> ordemMap = criarOrdemMap(atributosEOrdem);
            adicionarOrdenacao(criteria, ordemMap, new HashSet<String>());
        }

        if (valorCampo instanceof String) {
            criteria.add(((SimpleExpression) Restrictions.eq(nomeCampo, valorCampo)).ignoreCase());
        } else {
            criteria.add(Restrictions.eq(nomeCampo, valorCampo));
        }
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public T consultarUmPorCampo(String nomeCampo, Object valorCampo) {
        Criteria criteria = createCriteria(getClassePersistente());

        if (valorCampo instanceof String) {
            criteria.add(((SimpleExpression) Restrictions.eq(nomeCampo, valorCampo)).ignoreCase());
        } else {
            criteria.add(Restrictions.eq(nomeCampo, valorCampo));
        }

        criteria.setMaxResults(1);

        return (T) criteria.uniqueResult();
    }

    private Map<String, Boolean> criarOrdemMap(String... attributesAndOrder) {
        Map<String, Boolean> orderMap = new HashMap<String, Boolean>();

        for (String attributeAndOrder : attributesAndOrder) {
            String value;
            String attribute;
            boolean order;
            int tokenIndex = attributeAndOrder.indexOf(':');

            if (tokenIndex > 0) {
                attribute = attributeAndOrder.substring(0, tokenIndex);
                value = attributeAndOrder.substring(tokenIndex + 1, attributeAndOrder.length());
            } else {
                attribute = attributeAndOrder;
                value = "asc";
            }
            if ("asc".equalsIgnoreCase(value)) {
                order = true;
            } else if ("desc".equalsIgnoreCase(value)) {
                order = false;
            } else {
                throw new InfraException("Invalid attribute ordering syntax: " + attributeAndOrder); //TODO trmf texto em português.
            }
            orderMap.put(attribute, order);
        }
        return orderMap;
    }

    protected Set<String> adicionarCampos(Filtro filter, Criteria criteria) {
        Set<String> aliases = new HashSet<String>();

        for (Entry<String, Object> entry : filter.entrySet()) {
            String atributo = entry.getKey();

            Boolean isNegacao = Boolean.FALSE;
            Boolean isValorExato = Boolean.FALSE;

            // Nega a verificação
            if (atributo.charAt(0) == '!') {
                isNegacao = Boolean.TRUE;
                atributo = atributo.substring(1);
            }

            // Verifica se a restrição deve ser exata
            if (atributo.startsWith("==")) {
                isValorExato = Boolean.TRUE;
                atributo = atributo.substring(2);
            }

            Object valor = entry.getValue();

            if (isNaoVazio(valor)) {

                // Preparando as alias
                String alias = getAlias(atributo);

                if (StringUtils.isNotBlank(alias) && !aliases.contains(alias)) {
                    aliases.add(alias);
                    criteria.createAlias(alias, alias);
                }

                // Adicionando restrição
                Criterion criterion = null;

                if (valor.equals(Filtro.VALOR_NULO)) {
                    criterion = Restrictions.isNull(atributo);
                } else if (valor.equals(Filtro.VALOR_NAO_NULO)) {
                    criterion = Restrictions.isNotNull(atributo);
                } else if (valor instanceof String) {

                    // Verifica se a restrição de string é exata
                    if (isValorExato) {
                        criterion =
                                Restrictions.ilike(atributo, ((String) valor).toLowerCase(),
                                        MatchMode.EXACT);
                    } else {
                        // Se não, consulta em qualquer posição
                        criterion =
                                Restrictions.ilike(atributo, ((String) valor).toLowerCase(),
                                        MatchMode.ANYWHERE);
                    }

                } else if (valor instanceof Collection) {
                    criterion = Restrictions.in(atributo, (Collection<?>) valor);
                } else {
                    criterion = Restrictions.eq(atributo, valor);
                }

                // Caso negação
                if (isNegacao) {
                    criterion = Restrictions.not(criterion);
                }
                criteria.add(criterion);
            }
        }
        return aliases;
    }

    protected void adicionarOrdenacao(Criteria criteria, Map<String, Boolean> ordem,
            Set<String> aliases) {

        for (Entry<String, Boolean> entry : ordem.entrySet()) {

            String atributo = entry.getKey();
            Boolean ascendente = entry.getValue();

            String alias = getAlias(atributo);
            if (StringUtils.isNotBlank(alias) && !aliases.contains(alias)) {
                aliases.add(alias);
                criteria.createAlias(alias, alias, JoinType.LEFT_OUTER_JOIN);
            }

            if (ascendente) {
                criteria.addOrder(Order.asc(atributo).ignoreCase());
            } else {
                criteria.addOrder(Order.desc(atributo).ignoreCase());
            }
        }
    }

    private String getAlias(String atributo) {
        String alias = null;
        int index = atributo.indexOf('.');

        if (index > 0) {
            alias = atributo.substring(0, index);
        }
        return alias;
    }

    private static boolean isNaoVazio(Object valor) {
        boolean result;

        if (valor instanceof String) {
            result = StringUtils.isNotBlank((String) valor);
        } else {
            result = valor != null;
        }
        return result;
    }
}