<#include "security.ftlh">

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand mb-0 h1" href="/">Ambey</a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/main">Main</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/lot/add">создать лот</a>
            </li>
            <#if isUser>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Профиль</a>
                </li>
            </#if>

            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User list</a>
                </li>
                <#else>
            </#if>

        </ul>
            <div class="mx-auto">
                <form method="get" class="form-inline my-2 my-lg-0" action="/main">
                    <input type="text" name="filter" class="form-control mr-sm-2 " placeholder="filter"/>
                    <button type="submit" class="btn btn-outline-secondary my-2">Найти</button>
                </form>
            </div>
        <#if isUser>
            <div class=".navbar-text ml-3 mr-3 my-2"> ${name}</div>
            <div class=".navbar-text my-2"><#include "logout.ftlh"></div>
            <#else>
        </#if>
    </div>
</nav>