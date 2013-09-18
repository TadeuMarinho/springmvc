<%@ include file="./include_taglibs.jsp" %>

<c:if test="${not empty erros}">
<div id="mensgemErro">
    <ul>
        <c:forEach var="erro" items="${erros}">
        <li>${erro}</li>
        </c:forEach>
    </ul>
</div>
</c:if>

<c:if test="${not empty infos}">
<div id="mensagemInfo">
    <ul>
        <c:forEach var="info" items="${infos}">
        <li>${info}</li>
        </c:forEach>
    </ul>
</div>
</c:if>