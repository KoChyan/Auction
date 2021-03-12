<#import "parts/common.ftlh" as c>
<@c.page>
    <div>User list</div>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th>Balance</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role} <#sep>, </#list></td>
                <td>${user.balance?ifExists}</td>
                <td><a href="/user/${user.id}">edit</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>