<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="card-columns">
        <#list lots as lot>
            <div class="card my-5" style="width: 85%;">
                <#if lot.filename??>
                    <img src="/img/${lot.filename}" class="card-img-top"
                         style="max-height:300px; min-height: 40%;" alt="">
                </#if>
                <div class="card-body m-1">
                    <b>${lot.getName()?ifExists}</b>
                    <p class="mt-3 mb-1 mx-auto">
                        <button class="btn btn-outline-secondary btn-sm" type="button" data-toggle="collapse"
                                data-target="#text${lot.getId()}" aria-expanded="false" aria-controls="collapseExample">
                            Подробнее
                        </button>
                    </p>
                    <div class="collapse" id="text${lot.getId()}">
                        <b>описание: ${lot.getDescription()?ifExists}</b><br>
                        <b>время старта аукциона: ${lot.getStartTime()?ifExists}</b><br>
                        <b>время окончания аукциона: ${lot.getEndTime()?ifExists}</b><br>
                        <b>текущая ставка: ${lot.getFinalRate()?ifExists}</b><br>
                        <b>время до окончания: ${lot.getTimeStep()?ifExists}</b><br>
                    </div>
                </div>
                <div class="card-footer text-muted">
                    ${lot.getCreator().getUsername()?ifExists}
                </div>
            </div>
        </#list>
    </div>
</@c.page>