<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">
<#import "../parts/pager.ftlh" as p>
<@c.page>

    <div class="row row-cols-1 row-cols-md-3 g-4">
        <#list lotPage.content as lot>
            <div class="col" id="lotCol">
                <div class="card text-wight mb-3">
                    <div class="card-header text-center">
                        <p class="h5 mt-2">${lot.getName()!''}</p>
                    </div>
                    <div>
                        <img src="img/resized/300px_${lot.getFilename()!''}" class="card-img-top mx-auto d-block"
                             alt="">
                    </div>
                    <div class="card-body p-0">
                        <div class="fw-bold mt-2 text-center">
                            <p>Начало ${lot.getStartTime()?string('dd.MM.yyyy HH:mm')!''}</p>
                            <#if isAdmin>
                                <div class="mb-2">
                                    <a href="/lot/${lot.getId()}/cancel">Отменить торги</a>
                                </div>
                            </#if>
                        </div>
                        <div class="d-grid gap-2">
                            <button class="btn btn-outline-secondary btn-sm " type="button" data-bs-toggle="collapse"
                                    data-bs-target="#description${lot.getId()}" aria-expanded="false"
                                    aria-controls="collapseExample">
                                Описание
                            </button>
                        </div>
                        <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                            <a href="lot/${lot.getId()}/bet" id="btnGroup" role="button"
                               class="btn btn-outline-secondary">
                                К ставкам
                            </a>
                            <a href="lot/${lot.getId()}/comment" id="btnGroup" role="button"
                               class="btn btn-outline-secondary">
                                К комментариям
                            </a>
                        </div>
                        <div class="collapse my-2   " id="description${lot.getId()}">
                            <p class="m-1">${lot.getDescription()!''}</p>
                        </div>
                        <div class="card-footer text-center">
                            <p class="mt-2">Создатель лота: ${lot.getCreator().getUsername()!''}</p>
                        </div>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</@c.page>