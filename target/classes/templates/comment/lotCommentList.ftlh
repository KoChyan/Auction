<#import '../parts/common.ftlh' as c>
<@c.page>

    <div class="container my-5 mx-3">
        <#list comments as comment>
            <div class="card-body">
                <div class="row">
                    <div class="col-sm-3">
                        <p>${comment.getDate()!''}</p>
                    </div>
                    <div class="col-sm-2">
                        <p>${comment.getAuthor().getUsername()!''}</p>
                    </div>
                    <div class="col-sm-6">
                        <p class="text-break">${comment.getText()!''}</p>
                    </div>
                    <div class="col-sm-1">
                        <form method="post" action="/comment/delete/${comment.getId()!''}">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit">Удалить</button>
                        </form>
                    </div>
                </div>
            </div>
        </#list>
    </div>

    <form method="post">

        <div class="form-group row my-4">
            <div class="col-sm-6">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')} "
                       placeholder="Введите комментарий..." id="inputText"
                       value="<#if comment??>${comment.getText()!''}</#if>" aria-label="With textarea"
                       name="text">
                <#if textError??>
                    <#list textError as textEr>
                        <div class="invalid-feedback">
                            ${textEr}
                        </div>
                    </#list>
                </#if>
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" class="btn btn-secondary">Добавить</button>
    </form>
</@c.page>