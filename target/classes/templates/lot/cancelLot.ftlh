<#import "../parts/common.ftlh" as c>
<@c.page>

    <div class="container border">
        <div class="row" style="background-color: #f8f9fa">
            <div class="col-sm-12 text-center mb-2 py-3">
                <div class="h4">Лот: ${lot.getName()!''}</div>
                <div class="h4">Загрузил: ${lot.getCreator().getUsername()!''}</div>
                <div class="h4">Начало аукциона: ${lot.getStartTime()?string('dd.MM.yyyy HH:mm')!''}</div>
            </div>
        </div>
        <form method="post">
            <div class="form-group row my-4">
                <label for="reason" class="col-sm-2 col-form-label mr-3">Причина отмены </label>
                <div class="col-sm-6">
                    <input type="text" class="form-control" id="reason" name="reason">
                </div>

            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-secondary btn-sm m-5">Отменить аукцион</button>
        </form>
    </div>
</@c.page>
