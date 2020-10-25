<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>列表</title>
    <%
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>
    <link href="resources/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<input type="hidden" id="path" name="path" value="${APP_PATH}">
<div class="container">

    <div class="row">
        <div class="col-md-12">
            <h1>SSM-CRUD</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <button class="btn btn-primary" id="emp_add_modal_btn" onclick="showAddResumeDialog()">新增</button>
        </div>
    </div>

    <div style="margin-top: 10px">
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover table-bordered" id="resume_table">
                <thead>
                <tr>
                    <th><input type="checkbox" id="check_all"/></th>
                    <th>#</th>
                    <th>地址</th>
                    <th>姓名</th>
                    <th>电话</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<%--新增员工--%>
<div class="modal fade" id="resumeAddModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">新增简历</h4>
            </div>

            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">地址</label>
                        <div class="col-sm-10">
                            <input name="address" type="text" class="form-control" id="address_add_input"
                                   placeholder="address">
                            <span class="help-block"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <input name="name" type="text" class="form-control" id="name_add_input"
                                   placeholder="name">
                            <span class="help-block"></span>
                        </div>
                    </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">电话</label>
                            <div class="col-sm-10">
                                <input name="phone" type="text" class="form-control" id="phone_add_input"
                                       placeholder="phone">
                                <span class="help-block"></span>
                            </div>
                        </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="emp_save_btn" onclick="saveEmployee()">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<%--编辑员工--%>
<div class="modal fade" id="resumeUpdateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">修改简历信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">地址</label>
                        <div class="col-sm-10">
                            <p name="address" class="form-control-static" id="address_update_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">姓名</label>
                        <div class="col-sm-10">
                            <p name="name" class="form-control-static" id="name_update_static"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">电话</label>
                        <div class="col-sm-10">
                            <p name="phone" class="form-control-static" id="phone_update_static"></p>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="resume_update_btn">更新</button>
            </div>
        </div>
    </div>
</div>


<script src="resources/jquery-3.4.1.min.js"></script>
<script src="resources/bootstrap.min.js"></script>
<script type="text/javascript" src="${APP_PATH}/js/index.js" charset="utf-8"></script>
<%--<script type="text/javascript"></script>--%>
</body>

</html>