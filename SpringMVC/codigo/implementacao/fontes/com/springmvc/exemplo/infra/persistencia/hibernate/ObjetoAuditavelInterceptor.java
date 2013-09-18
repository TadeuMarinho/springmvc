package com.springmvc.exemplo.infra.persistencia.hibernate;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.joda.time.LocalDate;

import com.springmvc.exemplo.infra.persistencia.ObjetoPersistenteAuditavel;

@SuppressWarnings("serial")
public class ObjetoAuditavelInterceptor extends EmptyInterceptor {

    @Override
    public boolean onFlushDirty(Object entidade, Serializable id, Object[] estadoAtual,
            Object[] estadoAnterior, String[] propriedades, Type[] tipos) {
        
    	setDadosAuditaveis(entidade, estadoAtual, propriedades);
        return true;
    }

    @Override
    public boolean onSave(Object entidade, Serializable id, Object[] estado, String[] propriedades,
            Type[] tipos) {
        
    	setDadosAuditaveis(entidade, estado, propriedades);
        return true;
    }

    private void setDadosAuditaveis(Object entidade, Object[] stado, String[] propriedades) {
        if (entidade instanceof ObjetoPersistenteAuditavel) {
        	((ObjetoPersistenteAuditavel) entidade).setDataAtualizacao(new LocalDate());
        }
    }
}