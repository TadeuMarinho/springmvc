package com.springmvc.exemplo.infra;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Classe para conversão segura de lista para lista tipada.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ColecaoChecada implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Retorna a lista passada com checagem de tipo.
     * 
     * @param <T> Tipo dos ítens da lista.
     * @param lista Lista a ser checada.
     * @param classe Classe dos itens da lista.
     * @return Lista checada.
     */
    public static <T> List<T> listaChecada(List lista, Class<T> classe) {
        return Collections.checkedList(lista, classe);
    }

    /**
     * Retorna a coleção passada com checagem de tipo.
     * 
     * @param <T> Tipo dos í­tens da coleção.
     * @param colecao Coleção raw a ser checada.
     * @param classe Classe dos itens da coleção.
     * @return Coleção checada.
     */
    public static <T> Collection<T> colecaoChecada(Collection colecao, Class<T> classe) {
        return Collections.checkedCollection(colecao, classe);
    }
}