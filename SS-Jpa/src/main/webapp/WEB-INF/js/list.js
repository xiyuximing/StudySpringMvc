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
        url: path + "/qryAll",
        type: "GET",
        success: function (result) {
            console.log(result);
            build_resume_table(result);
        }
    })
}

function build_resume_table(result) {
    //清空表格内容
    $("#resume_table tbody").empty();
    //重置全选按钮状态
    $("#check_all").prop("checked",false);
    var emps = result.data.pageInfo.list;
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
    getDepts("#resumeAddModal select");
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
        url: path + "/insertOne",
        type: "POST",
        data: $("#resumeAddModal form").serialize(),
        success: function (result) {
            if (200 == result.code) {
                $("#resumeAddModal").modal("hide");
                qryAll();
            }
        }
    })
}


//编辑按钮点击
$(document).on("click", ".edit_btn", function () {

    getDepts("#resumeUpdateModal select");
    getResume($(this).attr("edit_id"));

    $("#resumeUpdateModal").modal({
        backdrop: "static"
    })
});

function getResume(id) {
    $.ajax({
        url: path + "/qryById" + id,
        type: "POST",
        success: function (result) {
            if (result.code == 200) {
                var data = result.data;
                $("#address_update_static").text(data.address);
                $("#name_update_static").val(data.name);
                $("#phone_update_static").val(data.phone);
                $("#dId").val(data.dId);
                $("#resume_update_btn").attr("edit_resume_id", data.dId);
            }
        }
    })
}


//更新按钮点击
$("#resume_update_btn").click(function () {

    $.ajax({
        url: path + "/updateOne/" + $(this).attr("edit_resume_id"),
        data: $("#resumeUpdateModal form").serialize(),
        type: "PUT",
        success: function (result) {
            if (200 == result.code) {
                $("#resumeUpdateModal").modal("hide");
                qryAll(currentPage);
            }
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
            url: path + "/deleteOne/" + id,
            type: "GET",
            success: function (result) {
                alert(result.msg);
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