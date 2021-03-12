<#import "parts/common.ftlh" as c>
<@c.page>
    <div>
        <form method="post" action="/lot/add" enctype="multipart/form-data">
            <div class="form-group row my-4">
                <label for="inputName" class="col-sm-2 col-form-label mr-3">Название</label>
                <div class="col-sm-6">
                    <input type="text" class="form-control" id="inputName" placeholder="Xbox 360" name="name"/>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="inputDescription" class="col-sm-2 col-form-label mr-3">Описание</label>
                <div class="col-sm-6">
                    <textarea class="form-control" id="inputDescription" aria-label="With textarea"
                              placeholder="Не распакована" name="description"></textarea>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="inputInitialRate" class="col-sm-2 col-form-label mr-3">Начальная ставка</label>
                <div class="col-sm-6">
                    <input type="number" class="form-control" id="inputInitialRate" placeholder="400"
                           name="initialRate"/>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="date" class="col-sm-2 col-form-label mr-3">Дата начала торгов</label>
                <div class="col-sm-6">
                    <input type="datetime-local" class="form-control" id="date"/>
                    <input type="hidden" id="hiddenDate" name="startDate"/>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="inputTimeStep" class="col-sm-4 col-form-label mr-3">Временной интервал между
                    предложениями, минут</label>
                <div class="col-sm-4">
                    <input type="number" class="form-control" id="inputTimeStep" placeholder="15" name="timeStep"/>
                </div>
            </div>
            <div class="form-group row my-4">
                <label class="col-sm-2 col-form-label ml-0">Добавить картинку</label>
                <div class="custom-file col-sm-6">
                    <input type="file" class="custom-file-input" id="inputFile" name="file"/>
                    <label  class="custom-file-label" for="inputFile"></label>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" onclick="fillHiddenDateField()" class="btn btn-outline-secondary btn-lg">Создать</button>
        </form>
    </div>
</@c.page>