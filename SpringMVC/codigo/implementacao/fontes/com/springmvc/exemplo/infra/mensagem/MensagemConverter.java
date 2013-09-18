package com.springmvc.exemplo.infra.mensagem;

import java.util.List;
import java.util.ResourceBundle;

public final class MensagemConverter {

    private static final MensagemConverter INSTANCE;
    private ResourceBundle bundle;

    static {
        INSTANCE = new MensagemConverter();
    }

    private MensagemConverter() {
        this.bundle = ResourceBundle.getBundle("mensagens");
    }

    public static MensagemConverter getInstance() {
        synchronized (INSTANCE) {
            return INSTANCE;
        }
    }

    public String getAsString(Mensagem mensagem) {
        String mensagemStr = bundle.getString(mensagem.getChave());
        List<String> parametros = mensagem.getParametros();
        if (parametros != null && !parametros.isEmpty()) {
            for (int i = 0; i < parametros.size(); i++) {
                mensagemStr = mensagemStr.replace("{" + i + "}", parametros.get(i));
            }
        }

        return mensagemStr;
    }

    public String getAsString(String chave, String... parametros) {
        return getAsString(new Mensagem(chave, parametros));
    }
}