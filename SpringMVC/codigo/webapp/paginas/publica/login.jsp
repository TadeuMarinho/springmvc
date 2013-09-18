<%@ include file="../include_taglibs.jsp" %>

<h2>Login</h2> <br>
<br /><br />

<sec:authorize access="isAnonymous()">
<sf:form modelAttribute="usuario" action="login.do">
    <label for="login">Login:</label>
    <sf:input path="login"/><br />
    <sf:errors path="login" cssClass="erro"/>
   
    <br />
   
    <label for="senha">Senha:</label>
    <sf:password path="senha"/><br />
    <sf:errors path="senha" cssClass="erro"/>
       
    <br /><input type="submit" value="Logar">
</sf:form>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
    Bem vindo(a) ${usuarioLogado.login}!
    <a href="<c:url value="/j_spring_security_logout"/>">Sair</a>
    
    <br />
    <br />
    
    <a href="paginas/privada/perfil1/pagina1.do">Página</a> acessível apenas pelo login1 e login 3. <br />
    <a href="paginas/privada/perfil2/pagina2.do">Página</a> acessível apenas pelo login2 e login 3.
</sec:authorize>