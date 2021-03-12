<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="m-5">
        ${message?ifExists}
    </div>
    <form method="post" action="/login">
        <div class="form-group row">
            <label for="inputUsername" class="col-sm-2 col-form-label">User name</label>
            <div class="col-sm-5">
                <input type="text" class="form-control" id="inputUsername" placeholder="Enter username"
                       name="username"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password</label>
            <div class="col-sm-5">
                <input type="password" class="form-control" placeholder="Enter password" name="password"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <a href="/registration">Создать аккаунт</a>
        <button type=submit" class="btn btn-secondary">Войти</button>
    </form>
</@c.page>
