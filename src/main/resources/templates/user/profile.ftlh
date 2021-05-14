<#import "../parts/common.ftlh" as c>
<#import "/spring.ftl" as spring>
<@c.page>
    <div class="m-5">
        <a href="/user/profile/subscriptions"><@spring.message "link.toSubscriptions"/></a>
    </div>

    <div class="m-5">
        <h5>${user.getUsername()!''}</h5>
    </div>

    <div class="m-5">
        <h5><@spring.message "field.user.balance"/>: #{user.getBalance()!''}</h5>
    </div>
    <form method="post" action="/user/profile">
        <input type="hidden" name="username">

        <div class="form-group row my-3">
            <label class="col-sm-2 col-form-label">
                <@spring.message "form.label.password"/>
            </label>
            <div class="col-sm-5">
                <input type="password" class="form-control ${(newPasswordError??)?string('is-invalid', '')}"
                       placeholder="<@spring.message 'form.placeholder.newPassword'/>"
                       name="newPassword"/>
                <#if newPasswordError??>
                    <#list newPasswordError as newPasswordErr >
                        <div class="invalid-feedback">
                            ${newPasswordErr}
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
        <div class="form-group row my-3">
            <label class="col-sm-2 col-form-label">
                <@spring.message "form.label.email"/>
            </label>
            <div class="col-sm-5">
                <input type="text" class="form-control ${(newEmailError??)?string('is-invalid', '')}"
                       value="${user.getEmail()!''}"
                       placeholder="<@spring.message 'form.placeholder.newEmail'/>"
                       name="newEmail"/>
                <#if newEmailError??>
                    <#list newEmailError as newEmailErr >
                        <div class="invalid-feedback">
                            ${newEmailErr}
                        </div>
                    </#list>
                </#if>
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" class="btn btn-secondary"><@spring.message "button.saveUpdates"/></button>
    </form>
</@c.page>