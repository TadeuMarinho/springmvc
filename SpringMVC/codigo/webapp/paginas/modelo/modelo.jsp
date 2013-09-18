<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<html>
    <body>
        <div id="cabecalho">
            <%@ include file="../include_cache.jsp" %>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
            <tiles:insertAttribute name="cabecalho" />
        </div>
        <div id="mensagens">
            <tiles:insertAttribute name="mensagens"/>
        </div>
        <div id="corpo">
            <tiles:insertAttribute name="corpo" />
        </div>
        <div id="rodape">
            <tiles:insertAttribute name="rodape" />
        </div>
    </body>
</html>