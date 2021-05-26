<#import "../parts/common.ftlh" as c>
<#import "/spring.ftl" as spring>
<@c.page>
    <div>
        <form method="post" action="/lot/add" enctype="multipart/form-data">
            <div class="form-group row my-4">
                <label for="inputName" class="col-sm-2 col-form-label mr-3">
                    <@spring.message "form.label.name"/>
                </label>
                <div class="col-sm-6">
                    <input type="text" class="form-control ${(nameError??)?string('is-invalid', '')} " id="inputName"
                           value="<#if lot??>${lot.getName()}</#if>"
                           name="name"
                           placeholder="<@spring.message 'form.placeholder.name'/>">
                    <#if nameError??>
                        <#list nameError as nErr >
                            <div class="invalid-feedback">
                                ${nErr}
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="inputDescription" class="col-sm-2 col-form-label mr-3 ">
                    <@spring.message "form.label.description"/>
                </label>
                <div class="col-sm-6">
                    <input type="text" class="form-control ${(descriptionError??)?string('is-invalid', '')} "
                           id="inputDescription"
                           value="<#if lot??>${lot.getDescription()!''}</#if>" aria-label="With textarea"
                           name="description"
                           placeholder="<@spring.message 'form.placeholder.description'/>">
                    <#if descriptionError??>
                        <#list descriptionError as dErr>
                            <div class="invalid-feedback">
                                ${dErr}
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="inputInitialBet" class="col-sm-2 col-form-label mr-3">
                    <@spring.message "form.label.initialBet"/>
                </label>
                <div class="col-sm-6">
                    <input type="number" class="form-control ${(initialBetError??)?string('is-invalid', '')} "
                           value="<#if lot??>${lot.getInitialBet()!''}</#if>"
                           id="inputInitialBet"
                           name="initialBet"
                           placeholder="<@spring.message 'form.placeholder.initialBet'/>">
                    <#if initialBetError??>
                        <#list initialBetError as ibErr >
                            <div class="invalid-feedback">
                                ${ibErr}
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="date" class="col-sm-2 col-form-label mr-3">
                    <@spring.message "form.label.startTime"/>
                </label>
                <div class="col-sm-6">
                    <input type="datetime-local" id="date"
                           class="form-control ${(startTimeError??)?string('is-invalid', '')}">
                    <input type="hidden" id="hiddenDate" name="startTime">
                    <#if startTimeError??>
                        <#list startTimeError as stErr >
                            <div class="invalid-feedback">
                                ${stErr}
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="form-group row my-4">
                <label for="inputTimeStep" class="col-sm-4 col-form-label mr-3">
                    <@spring.message "form.label.timeStep"/>
                </label>
                <div class="col-sm-4">
                    <input type="number" class="form-control  ${(timeStepError??)?string('is-invalid', '')} "
                           value="<#if lot??>${lot.getTimeStep()!''}</#if>"
                           name="timeStep" id="inputTimeStep"
                           placeholder="<@spring.message 'form.placeholder.timeStep'/>">
                    <#if timeStepError??>
                        <#list timeStepError as tsErr >
                            <div class="invalid-feedback">
                                ${tsErr}
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>
            <div class="form-group row my-4">
                <label class="col-sm-2 col-form-label ml-0">
                    <@spring.message "form.label.image"/>
                </label>
                <div class="custom-file col-sm-6">
                    <label class="custom-file-label" for="inputFile"></label>
                    <input class="form-control form-control-sm" id="formFileSm" type="file" accept=".png, .jpg, .jpeg" name="file"/>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>

            <button type="submit" onclick="dateToUTC()" class="btn btn-primary">
                <@spring.message "button.createLot"/>
            </button>
        </form>
    </div>
</@c.page>
