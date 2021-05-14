<#import "../parts/common.ftlh" as c>
<#import "/spring.ftl" as spring>
<#import "../parts/pager.ftlh" as p>

<@c.page>
    <h3 class="m-5 fw-bold"><@spring.message "userList"/></h3>
    <@p.pager url 5 10 15 20 page />
    <div class="container m-3">
        <div class="row my-3">
            <div class="col-sm-3">
                <p class="fw-bold"><@spring.message "field.user.username"/><p>
            </div>
            <div class="col-sm-2">
                <p class="fw-bold"><@spring.message "field.user.roles"/></p>
            </div>
            <div class="col-sm-2">
                <p class="fw-bold"><@spring.message "field.user.balance"/></p>
            </div>
            <div class="col-sm-2"></div>
        </div>
        <#list page.content as user>
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
                    <a href="/user/${user.getId()}" class="btn btn-secondary btn-sm">
                        <@spring.message "link.edit"/>
                    </a>
                </div>
            </div>
        </#list>
    </div>
</@c.page>