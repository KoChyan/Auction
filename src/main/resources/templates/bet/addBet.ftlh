<#import "../parts/common.ftlh" as c>
<@c.scriptPage "connect()">

    <div  class="container border">
        <div class="row" style="background-color: #f8f9fa">
            <div class="col-sm-12 text-center mb-2 py-3">
                <div class="h4">Лот: ${lot.getName()!''}</div>
                <div class="h4">Загрузил: ${lot.getCreator().getUsername()!''}</div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-4 text-center ">
                <img src="/img/resized/300px_${lot.getFilename()!''}" alt="" style="max-width: 100%">
            </div>
            <div class="col-sm-8">
                <div class="row my-3 " style="font-weight: bold">
                    <div class="col">Дата</div>
                    <div class="col">Имя</div>
                    <div class="col">Ставка</div>
                </div>

                <div onload="connect()" class="row my-2">
                    <div class="col">${pricing[0].getDate()?string('MM.dd HH:mm:ss')!''}</div>
                    <div class="col">${pricing[0].getUser().getUsername()!''}</div>
                    <div class="col" id="bet" style="font-size: 14pt">
                        #{pricing[0].getBet()}
                    </div>
                </div>

                <div class="mb-3" style="font-size: 14pt">
                    Максимальный интервал между ставками, минут: ${lot.getTimeStep()}
                </div>
                <#if timeLeft??>
                    <div class="mt-5 mb-2" style="font-size: 14pt">
                        До окончания приема ставок, секунд: ${timeLeft}
                    </div>
                </#if>
                <#if timeBefore??>
                    <div class="mb-5" style="font-size: 14pt">
                        До начала аукциона, секунд: ${timeBefore}
                    </div>
                </#if>

                <form method="post" action="/lot/${lot.getId()}/bet">
                    <div class="form-group row mt-5 mb-3">
                        <label for="inputBet" class="col-sm-2 col-form-label mr-2 mb-2">Ставка</label>
                        <div class="col col-sm-4">
                            <input type="number" class="form-control ${(betError??)?string('is-invalid', '')}"
                                   id="inputBet" placeholder="400"
                                   name="bet">
                            <#if betError??>
                                <#list betError as betErr>
                                    <div class="invalid-feedback">
                                        ${betErr}
                                    </div>
                                </#list>
                            </#if>
                        </div>
                        <input type="hidden" name="date" id="hiddenDate">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="btn btn-outline-secondary px-2 mx-2" style="height: 2.4rem; width: 8rem"
                                onclick="hiddenDateIsNow()">
                            Поставить
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <div class="row" style="height:25px;    background-color: #f8f9fa"></div>
    </div>
</@c.scriptPage>

