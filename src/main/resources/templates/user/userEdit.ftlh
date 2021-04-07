<#import "../parts/common.ftlh" as c>

<@c.page>

    <form method="post" action="/user">

        <div class="form-group row my-3">
            <label class="col-form-label col-sm-2">Username</label>
            <div class="col-sm-5">
                <input type="text" class="form-control col-sm-4" value="${user.getUsername()}" name="username">
            </div>
        </div>
        <div class="form-group row my-3">
            <label class="col-form-label col-sm-2">Balance</label>
            <div class="col-sm-5">
                <input type="number" class="form-control col-sm-4" value="${user.getBalance()}"
                       min="-999999999" max="999999999" name="balance">
            </div>
        </div>
        <div class="form-group row my-3">
            <div class="btn-group-vertical col-sm-1 my-3" role="group"
                 aria-label="Basic checkbox toggle button group">
                <div class="col-sm-3 my-3">Роли</div>

                <#list roles as role>
                    <input type="checkbox" class="btn-check" id="target${role}" autocomplete="off"
                           name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>
                    <label class="btn btn-outline-secondary" for="target${role}">${role}</label>
                </#list>
            </div>
        </div>
        <input type="hidden" value="${user.id}" name="userId">

        <input type="hidden" value="${_csrf.token}" name="_csrf">

        <button type="submit" class="btn btn-secondary m-3">Сохранить</button>
    </form>

</@c.page>