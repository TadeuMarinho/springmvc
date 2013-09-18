package com.springmvc.exemplo.infra;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Classe para convers�o segura de lista para lista tipada.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ColecaoChecada implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Retorna a lista passada com checagem de tipo.
     * 
     * @param <T> Tipo dos �tens da lista.
     * @param lista Lista a ser checada.
     * @param classe Classe dos itens da lista.
     * @return Lista checada.
     */
    public static <T> List<T> listaChecada(List lista, Class<T> classe) {
        return Collections.checkedList(lista, classe);
    }

    /**
     * Retorna a cole��o passada com checagem de tipo.
     * 
     * @param <T> Tipo dos �tens da cole��o.
     * @param colecao Cole��o raw a ser checada.
     * @param classe Classe dos itens da cole��o.
     * @return Cole��o checada.
     */
    public static <T> Collection<T> colecaoChecada(Collection colecao, Class<T> classe) {
        return Collections.checkedCollection(colecao, classe);
    }
}