package com.springmvc.exemplo.infra.mensagem;

import java.util.ResourceBundle;

public class RotuloConverter {

    private static final RotuloConverter INSTANCE;
    private ResourceBundle bundle;

    static {
        INSTANCE = new RotuloConverter();
    }

    private RotuloConverter() {
        this.bundle = ResourceBundle.getBundle("rotulos");
    }

    public static RotuloConverter getInstance() {
        synchronized (INSTANCE) {
            return INSTANCE;
        }
    }

    public String getAsString(String chave) {
        return bundle.getString(chave);
    }
}