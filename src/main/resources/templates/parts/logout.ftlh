
<form method="post" action="/logout">
    <button type="submit" class="btn btn-outline-secondary btn-sm">Выйти</button>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>

