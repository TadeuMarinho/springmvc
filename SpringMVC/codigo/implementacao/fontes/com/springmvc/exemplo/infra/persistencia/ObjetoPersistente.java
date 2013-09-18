package com.springmvc.exemplo.infra.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.springmvc.exemplo.infra.mensagem.Mensagem;
import com.springmvc.exemplo.infra.mensagem.RotuloConverter;

/**
 * Define um objeto persistente no sistema.
 */
@SuppressWarnings("serial")
public abstract class ObjetoPersistente implements Serializable {
	
	protected Integer id;

    /**
     * Recupera o identificador do objeto.
     * 
     * @return Identificador do objeto.
     */
    @Transient
    public abstract Integer getId();

    /**
     * Configura o identificador do objeto.
     * 
     * @param pk Valor a ser configurado na pk.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Valida o objeto.
     * 
     * @return Lista de mensagens contendo informações sobre a validação do objeto.
     */
    public List<Mensagem> validar() {
        return validar(this);
    }

    private <E> List<Mensagem> validar(E obj) {
        List<Mensagem> mensagens = new ArrayList<Mensagem>();
        List<Mensagem> mensagensObrigatoriedade = new ArrayList<Mensagem>();
        List<String> parametros = null;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<E>> constraintViolations = validator.validate(obj);
        for (ConstraintViolation<E> violacao : constraintViolations) {
            String chaveMensagem =
                    violacao.getConstraintDescriptor().getAnnotation().annotationType().getName();

            String propriedade =
                    violacao.getRootBeanClass().getSimpleName() + "."
                            + violacao.getPropertyPath().toString();

            parametros = new ArrayList<String>();
            parametros.add(RotuloConverter.getInstance().getAsString(propriedade));
            adicionarParametrosAdicionais(parametros, violacao);

            if ("org.hibernate.validator.constraints.NotBlank".equals(chaveMensagem)
                    || "javax.validation.constraints.NotNull".equals(chaveMensagem)) {

                mensagensObrigatoriedade.add(new Mensagem(chaveMensagem, parametros));
            }
            mensagens.add(new Mensagem(chaveMensagem, parametros));
        }

        if (mensagensObrigatoriedade.isEmpty()) {
            return mensagens;
        } else {
            return mensagensObrigatoriedade;
        }
    }

    private <E> void adicionarParametrosAdicionais(List<String> parametros,
            ConstraintViolation<E> violacao) {

        adicionarParametro(violacao, parametros, "value");
        adicionarParametro(violacao, parametros, "min");
        adicionarParametro(violacao, parametros, "max");
    }

    private <E> void adicionarParametro(ConstraintViolation<E> violacao, List<String> parametros,
            String atributo) {

        if (violacao.getConstraintDescriptor().getAttributes().get(atributo) != null) {
            parametros.add(violacao.getConstraintDescriptor().getAttributes().get(atributo)
                    .toString());
        }
    }

}