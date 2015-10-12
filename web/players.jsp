<%@page import="control.DataProcess"%>
<section class="panel">
    <header class="panel-heading">
        All projects List
        <span class="pull-right">
            <button type="button" id="loading-btn" class="btn btn-warning btn-xs"><i class="fa fa-refresh"></i> Refresh</button>
            <a href="#" class=" btn btn-success btn-xs"> Create New Project</a>
        </span>
    </header>
    <div class="panel-body">
        <div class="row">

            <div class="col-md-12">
                <div class="input-group"><input type="text" placeholder="Search Here" class="input-sm form-control"> <span class="input-group-btn">
                        <button type="button" class="btn btn-sm btn-success"> Go!</button> </span></div>
            </div>
        </div>
    </div>
    <table class="table table-hover p-table">
        <thead>
            <tr>
                <th>Project Name</th>
                <th>Team Member</th>
                <th>Project Progress</th>
                <th>Project Status</th>
                <th>Custom</th>
            </tr>
        </thead>
        <tbody id="tablebody">
        </tbody>
    </table>
    <div id="pagi"></div>
    <script src="js/bootstrap-paginator.js"></script>
    <script type='text/javascript'>
        var options = {
            currentPage: 1,
            totalPages: <% DataProcess db = new DataProcess();%> <%=db.getTotalPlayer()%>/10+1 ,
            onPageClicked: function (e, originalEvent, type, page) {
                $.ajax({
                    url: 'getPlayers?page=' + page,
                    beforeSend: function () {
                        $('#tablebody').empty();
                        $('#tablebody').append("<div class=\"spinner\"> <div class=\"rect1\"></div> <div class=\"rect2\"></div> <div class=\"rect3\"></div> <div class=\"rect4\"></div> <div class=\"rect5\"></div> </div>");
                        $('.spinner').show();
                    },
                    complete: function () {
                        $('.spinner').hide();
                    },
                    success: function (responseText) {
                        $('#tablebody').append(responseText);
                    }
                });
            }
        };
        $('#pagi').bootstrapPaginator(options);
    </script>
</section>
<script src="js/tpxjs.js"></script>