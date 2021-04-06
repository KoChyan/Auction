<#import "../parts/common.ftlh" as c>
<@c.page>
    <div class="m-5">
        <h5>${username!''}</h5>
    </div>

    <form method="post">

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password</label>
            <div class="col-sm-5">
                <input type="password" class="form-control"
                       placeholder="Enter new password"
                       name="password"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-5">
                <input type="text" class="form-control "
                       value="${email!''}" placeholder="example@gmail.com"
                       name="email"/>
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" class="btn btn-secondary">Сохранить</button>
    </form>
</@c.page>