<#import "parts/common.ftlh" as c>
<@c.page>

    <div class="card-columns display: inline-block">
        <#list lots as lot>
            <div class="card my-5 border-secondary mb-3 ">
                <div class="card-header text-center">
                    <span class="align-middle">Лот: ${lot.getName()?ifExists}</span>
                </div>
                <#if lot.filename??>
                    <img src="/img/${lot.filename}" class="card-img-top img-fluid" alt="">
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
                </div>
                <div class="card-footer text-center">
                    <span class="align-middle ">Создатель лота: ${lot.getCreator().getUsername()?ifExists}</span>
                </div>
            </div>
        </#list>
    </div>
</@c.page>