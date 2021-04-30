<#macro pager url lotPage>
    <div>
        <nav aria-label="...">
            <ul class="pagination">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Страницы</a>
                </li>
                <#list 1..lotPage.getTotalPages() as p>
                    <#if p - 1 == lotPage.getNumber()>
                        <li class="page-item active" aria-current="page">
                            <a class="page-link" href="#">${p}</a>
                        </li>
                    <#else>
                        <li class="page-item" aria-current="page">
                            <a class="page-link" href="${url}?page=${p - 1}">${p}</a>
                        </li>
                    </#if>
                </#list>
            </ul>
        </nav>
    </div>
</#macro>