<#import "parts/common.ftlh" as c>
<@c.page>

    <div class="container border">
        <div class="row" style="background-color: #f8f9fa">
            <div class="col-sm-12 text-center mb-2 py-3">
                <div class="h4">Лот: ${lot.getName()}</div>
                <div class="h4">Загрузил: ${lot.getCreator().getUsername()}</div>
            </div>
        </div>
        <div class="row" id="scroll">
            <div class="col-sm-4 text-center ">
                <img src="/img/resized/${lot.getFilename()}" alt="" style="max-width: 100%">
            </div>
            <div class="col-sm-8">
                <div class="row my-3 " style="font-weight: bold">
                    <div class="col">Дата</div>
                    <div class="col">Имя</div>
                    <div class="col">Ставка</div>
                </div>
                <#list pricing as p>
                    <div class="row my-1">
                        <div class="col">${p.getDate()}</div>
                        <div class="col">${p.getUser().getUsername()}</div>
                        <div class="col">${p.getBet()}</div>
                    </div>
                </#list>
            </div>
        </div>
        <div class="row" style=" height:25px;    background-color: #f8f9fa"></div>
    </div>
</@c.page>