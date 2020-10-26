//页面加载时调用
//utf-8
const path = $("#path").val();
var totalRecord, currentPage;
$(function () {
    qryAll();
});

function qryAll() {
    console.log("-------------s------${APP_PATH}--------" + path);
    $.ajax({
        url: path + "/resume/qryAll",
        type: "GET",
        success: function (result) {
            if (result == "error") {
                $(location).attr('href', 'index.jsp');
            } else {
                console.log(result);
                build_resume_table(result);
            }
        }
    })
}

function build_resume_table(result) {
    //清空表格内容
    $("#resume_table tbody").empty();
    //重置全选按钮状态
    $("#check_all").prop("checked",false);
    var emps = result;
    $.each(emps, function (index, item) {
        var checkBoxId = $("<td><input type='checkbox' class='check_item'/></td>");
        var id = $("<td></td>").append(item.id);
        var address = $("<td></td>").append(item.address);
        var name = $("<td></td>").append(item.name);
        var phone = $("<td></td>").append(item.phone);

        var btnEditId = $("<button></button>").addClass("btn btn-primary btn-sm  edit_btn")
            .append($("<span></span>").addClass("glyphicon glyphicon-edit")).append("编辑");
        btnEditId.attr("edit_id", item.id);
        var btnDeleteId = $("<button></button>").addClass("btn btn-danger btn-sm delete_btn")
            .append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除");
        btnDeleteId.attr("del_resume_id", item.id).attr("del_resune_name", item.name);

        var btnTd = $("<td></td>").append(btnEditId).append(" ").append(btnDeleteId);
        $("<tr></tr>").append(checkBoxId)
            .append(id)
            .append(address)
            .append(name)
            .append(phone)
            .append(btnTd)
            .appendTo("#resume_table tbody");
    })
}


function showAddResumeDialog() {
    reset_form("#resumeAddModal form");
    $("#resumeAddModal").modal({
        backdrop: "static"
    })
}

function reset_form(ele) {
    $(ele)[0].reset();
    $(ele).find("*").removeClass("has-success has-error");
    $(ele).find(".help-block").text("");

}


function saveEmployee() {

    $.ajax({
        url: path + "/resume/insertOne",
        type: "POST",
        data: $("#resumeAddModal form").serialize(),
        success: function (result) {
            $("#resumeAddModal").modal("hide");
            qryAll();
        }
    })
}


//编辑按钮点击
$(document).on("click", ".edit_btn", function () {

    getResume($(this).attr("edit_id"));

    $("#resumeUpdateModal").modal({
        backdrop: "static"
    })
});

function getResume(id) {
    $.ajax({
        url: path + "/resume/qryById/" + id,
        type: "POST",
        success: function (result) {
                var data = result;
                $("#address_update_static").val(data.address);
                $("#name_update_static").val(data.name);
                $("#phone_update_static").val(data.phone);
                $("#dId").val(data.id);
                $("#resume_update_btn").attr("edit_resume_id", data.id);
        }
    })
}


//更新按钮点击
$("#resume_update_btn").click(function () {

    $.ajax({
        url: path + "/resume/updateOne/" + $(this).attr("edit_resume_id"),
        data: $("#resumeUpdateModal form").serialize(),
        type: "PUT",
        success: function (result) {
            $("#resumeUpdateModal").modal("hide");
            qryAll();
        }
    })
});


//单个删除
// $("#delete_btn").click(function () {
$(document).on("click", ".delete_btn", function () {
    var id = $(this).attr("del_resume_id");
    var name = $(this).attr("del_resune_name");
    if (confirm("确认删除" + name + "吗？")) {
        $.ajax({
            url: path + "/resume/deleteOne/" + id,
            type: "GET",
            success: function (result) {
                qryAll();
            }
        })
    }
});


//点击全选或者全不选
$("#check_all").click(function () {
    // console.log("-----:" + $("#check_all").prop("checked"));
    $(".check_item").prop("checked", $("#check_all").prop("checked"));
});

//单个点击选中
$(document).on("click", ".check_item", function () {
    var flag = $(".check_item:checked").length == $(".check_item").length;
    console.log("-----2------:" + flag);
    $("#check_all").prop("checked", flag);
});