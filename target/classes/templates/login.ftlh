<#import "parts/common.ftlh" as c>
<@c.page>

    <div class="m-5">
        ${message!''}
    </div>

    <div class="text-center">
        <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
            <div class="alert alert-danger" role="alert" style="max-width: 80rem;">
                ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
            </div>
        </#if>
    </div>



    <form method="post" action="/login">
        <div class="form-group row my-3">
            <div class="col-sm-5">
                <input type="text" class="form-control " id="inputUsername"
                       placeholder="Enter username"
                       name="username"/>
            </div>
        </div>
        <div class="form-group row my-3">
            <div class="col-sm-5">
                <input type="password" class="form-control"
                       placeholder="Enter password"
                       name="password"/>

            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <a class="mx-3  my-5" href="/registration">Создать аккаунт</a>
        <button type=submit" class="btn btn-secondary mx-4">Войти</button>
    </form>
</@c.page>
