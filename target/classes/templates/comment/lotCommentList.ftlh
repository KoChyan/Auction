<#import '../parts/common.ftlh' as c>
<#include "../parts/security.ftlh">
<@c.page>

    <div class="card mb-3">
        <div class="row g-0">
            <div class="col-md-4">
                <img src="/img/resized/300px_${lot.getFilename()!''}" class="mx-auto d-block img-fluid" alt="">
            </div>
            <div class="col-md-8">
                <div class="card-body">
                    <h5 class="card-title">
                        <div class="row">
                            <div class="col-sm-6">Лот: ${lot.getName()}</div>
                            <div class="col-sm-6">Создатель: ${lot.getCreator().getUsername()}</div>
                        </div>
                    </h5>
                    <p class="card-text">
                        <#list comments as comment>
                        <div class="row">
                            <div class="col-sm-3">
                    <p>${comment.getDate()?string('dd.MM.yyyy HH:mm:ss')!''}</p>
                </div>
                <div class="col-sm-2">
                    <p>${comment.getAuthor().getUsername()!''}</p>
                </div>
                <div class="col-sm-6">
                    <p class="text-break">${comment.getText()!''}</p>
                </div>
                <div class="col-sm-1">
                    <#if isAdmin>
                        <form method="post" action="/lot/${lot.getId()!''}/comment/${comment.getId()!''}/delete">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-outline-danger btn-sm">Х</button>
                        </form>
                        <#else>
                    </#if>
                </div>
            </div>
            </#list>
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

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-outline-secondary px-2 mx-2">Добавить</button>
                    </div>

                </div>
            </form>
            </p>
        </div>
    </div>


</@c.page>