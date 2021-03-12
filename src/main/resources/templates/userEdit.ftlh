<#import "parts/common.ftlh" as c>

<@c.page>

    <form method="post" action="/user">
        <input type="text" value="${user.username}" name="username">
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
       <label>Balance</label> <input type="number" value="${user.balance}" name="balance">
        <input type="hidden" value="${user.id}" name="userId">

        <input type="hidden" value="${_csrf.token}" name="_csrf">

        <button type="submit">Сохранить</button>
    </form>

</@c.page>