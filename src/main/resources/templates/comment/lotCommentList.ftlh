<#import '../parts/common.ftlh' as c>
<@c.page>

    <div class="container my-5 mx-3">
        <#list comments as comment>
            <div class="col-sm-6">
                <p>${comment!''}</p>
            </div>
        </#list>
    </div>

    <form method="post">
        <div class="form-group row my-4">
            <label for="inputDescription" class="col-sm-2 col-form-label mr-3 ">Описание</label>
            <div class="col-sm-6">
                <input type="text" class="form-control ${(commentError??)?string('is-invalid', '')} "
                       id="inputText"
                       value="<#if comment??>${comment.getText()()!''}</#if>" aria-label="With textarea"
                       name="text">
                <#if commentError??>
                    <#list commentError as commentEr>
                        <div class="invalid-feedback">
                            ${commentEr}
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
    </form>
</@c.page>