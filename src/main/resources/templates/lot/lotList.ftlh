<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">
<#import "../parts/pager.ftlh" as p>
<#import "/spring.ftl" as spring>
<@c.page>

    <@p.pager url 3 6 9 12 page />
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <#list page.content as lot>
            <div class="col" id="lotCol">
                <div class="card text-wight mb-3">
                    <div class="card-header text-center">
                        <p class="h5 mt-2">
                            <@spring.message "field.lot.name"/>: ${lot.getName()!''}
                        </p>
                    </div>
                    <p class="my-2" id="imgContainer">
                        <img src="img/300PX_${lot.getFilename()!''}" class="card-img-top mx-auto d-block img-fluid"
                             id="lotImg" alt="">
                    </p>
                    <div class="card-body p-0">
                        <div class="fw-bold mt-2 text-center">
                            <p>
                                <@spring.message "field.lot.startTime"/>
                                : ${lot.getStartTime()?string('dd.MM.yyyy HH:mm')!''}
                            </p>
                            <#if isAdmin>
                                <div class="mb-2">
                                    <a href="/lot/${lot.getId()}/cancel">
                                        <@spring.message "link.cancelAuction"/>
                                    </a>
                                </div>
                            </#if>
                            <#if isUser && lot.isSubscriber(user)>
                                <a href="lot/${lot.getId()}/unsubscribe"><@spring.message "unsubscribe"/></a>
                            <#elseif isUser && !lot.isSubscriber(user)>
                                <a href="lot/${lot.getId()}/subscribe"><@spring.message "subscribe"/></a>
                            </#if>
                        </div>
                        <div class="d-grid gap-2">
                            <button class="btn btn-outline-secondary btn-sm " type="button" data-bs-toggle="collapse"
                                    data-bs-target="#description${lot.getId()}" aria-expanded="false"
                                    aria-controls="collapseExample">
                                <@spring.message "button.openDescription"/>
                            </button>
                        </div>
                        <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                            <#if isUser>
                                <a href="lot/${lot.getId()}/bet" id="btnGroup" role="button"
                                   class="btn btn-outline-secondary">
                                    <@spring.message "link.toBets"/>
                                </a>
                            </#if>
                            <a href="lot/${lot.getId()}/comment" id="btnGroup" role="button"
                               class="btn btn-outline-secondary">
                                <@spring.message "link.toComments"/>
                            </a>
                        </div>
                        <div class="collapse my-2   " id="description${lot.getId()}">
                            <p class="m-1">${lot.getDescription()!''}</p>
                        </div>
                        <div class="card-footer text-center">
                            <p class="mt-2">
                                <@spring.message "field.lot.author"/>: ${lot.getCreator().getUsername()!''}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</@c.page>