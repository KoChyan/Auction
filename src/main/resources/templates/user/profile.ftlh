<#import "../parts/common.ftlh" as c>
<@c.page>
    <div class="m-5">
        <h5>${user.getUsername()!''}</h5>
    </div>

    <div class="m-5">
        <h5>Баланс: ${user.getBalance()!''}</h5>
    </div>

    <form method="post" action="/user/profile">
        <input type="hidden" name="username">

        <div class="form-group row my-3">
            <label class="col-sm-2 col-form-label">Пароль</label>
            <div class="col-sm-5">
                <input type="password" class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Введите новый пароль"
                       name="password"/>
                <#if passwordError??>
                    <#list passwordError as pErr >
                        <div class="invalid-feedback">
                            ${pErr}
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
        <div class="form-group row my-3">
            <label class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-5">
                <input type="text" class="form-control ${(emailError??)?string('is-invalid', '')}"
                       value="${user.getEmail()!''}" placeholder="newmail@gmail.com"
                       name="email"/>
                <#if emailError??>
                    <#list emailError as eErr >
                        <div class="invalid-feedback">
                            ${eErr}
                        </div>
                    </#list>
                </#if>
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" class="btn btn-secondary">Сохранить</button>
    </form>
</@c.page>