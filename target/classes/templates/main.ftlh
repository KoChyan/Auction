<#import "parts/common.ftlh" as c>
<@c.page>

    <div class="card-columns c">
        <#list lots as lot>
            <div class="card m-4" style="max-width:18rem">
                <div class="card-header text-center">
                    <span>Лот: ${lot.getName()?ifExists}</span>
                </div>
                <#if lot.filename??>
                    <div id="imgContainer"><img src="/img/resized/${lot.filename}" class="card-img-top" alt=""></div>
                </#if>
                <div class="card-body m-1">
                    <div class="d-grid gap-2">
                        <button class="btn btn-outline-secondary btn-sm " type="button" data-toggle="collapse"
                                data-target="#target${lot.getId()}" aria-expanded="false"
                                aria-controls="collapseExample">
                            Описание
                        </button>
                    </div>
                    <div class="collapse" id="target${lot.getId()}">
                        <p class="card-text">${lot.getDescription()?ifExists}</p>

                        <b>текущая ставка: ${lot.getFinalRate()?ifExists}</b><br>
                    </div>
                    <a href="/lot/${lot.getId()}" class="card-link mx-2 my-5">Сделать ставку</a>
                </div>
                <div class="card-footer text-center">
                    <span>Разместил: ${lot.getCreator().getUsername()?ifExists}</span>
                </div>
            </div>
        </#list>
    </div>

</@c.page>