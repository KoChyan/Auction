<#import "../parts/common.ftlh" as c>
<@c.page>
    <h3 class="m-5 fw-bold">Список пользователей</h3>

    <div class="container m-3">
        <div class="row my-3">
            <div class="col-sm-3">
                <p class="fw-bold">Имя пользователя<p>
            </div>
            <div class="col-sm-2">
                <p class="fw-bold">Роли</p>
            </div>
            <div class="col-sm-2">
                <p class="fw-bold">Баланс</p>
            </div>
            <div class="col-sm-2"></div>
        </div>
        <#list users as user>
            <div class="row my-3">
                <div class="col-sm-3">
                    ${user.getUsername()}
                </div>
                <div class="col-sm-2">
                    <#list user.getRoles() as role>
                        ${role} <#sep>,
                    </#list>
                </div>
                <div class="col-sm-2">
                    ${user.getBalance()}
                </div>
                <div class="col-sm-2">
                    <a href="/user/${user.getId()}" class="btn btn-secondary btn-sm">Редактировать</a>
                </div>
            </div>
        </#list>
    </div>
</@c.page>