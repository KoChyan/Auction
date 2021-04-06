<#include "security.ftlh">
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Ambey</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="/">Home</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        Лот
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="/lot">Посмотреть существующие</a></li>
                        <li><a class="dropdown-item" href="/lot/add">Добавить лот</a></li>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <li><a class="dropdown-item" href="#">Something else here</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
                </li>
            </ul>
            <div class="container-fluid" style="width: 30rem">
                <form class="d-flex" action="/lot" method="get">
                    <input class="form-control me-2" type="search" placeholder="name" aria-label="Search"
                           name="name">
                    <input class="form-control me-2" type="search" placeholder="description" aria-label="Search"
                           name="description">
                    <button class="btn btn-outline-success" type="submit">Search</button>
                </form>
            </div>
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <#if isUser??>${name!'guest'}</#if>
                    </a>
                    <ul class="dropdown-menu text-center" aria-labelledby="navbarDropdown">
                        <#if isUser>
                            <li><a class="dropdown-item" href="/user/profile">Профиль</a></li>
                        </#if>
                        <#if isAdmin>
                            <li><a class="dropdown-item" href="/user">Список пользователей</a></li>
                        </#if>
                        <#if !isUser>
                            <li><a class="dropdown-item" href="/login">Sign in</a></li>
                        </#if>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <#if !isUser>
                            <li><a class="dropdown-item" href="/registration">Create account</a></li>
                        </#if>
                        <#if isUser || isAdmin>
                            <li class="my-2"><#include "logout.ftlh"></li>
                        </#if>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>