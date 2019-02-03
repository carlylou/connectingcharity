var areYouSureText = "Are you sure? You are about to delete the article";

$(document).ready(function () {

    $("#editLink").click(editModeToggle);

    $('[data-toggle="tooltip"]').tooltip({container: 'body'});

    $("#csrfTokenInput").attr("value", Cookies.get('XSRF-TOKEN'));

    $("#active-button").click(function () {
        var id = $("#active-button").attr("data-value");
        //alert(id);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/charity/" + id + "/verify",
            success: function (data, textStatus, xhr) {
                alert("A verify message has been send to your email, please check it!");
            },
            error: function (xhr, textStatus) {
                if (xhr.status == 405){
                    $("#addMailModal").modal("toggle");}
                else if(xhr.status == 406){
                     alert("Invalid email. Please use an official one.");
                     $("#addMailModal").modal("toggle");
                }
                else{
                    alert("There was an error communicating with the server");}

                console.log("ERROR : ", xhr.status);
            }
        });
    });
});

function editModeToggle() {

    $(".edit-badge").toggle();
}

function showArticle(id) {
    $('#viewArticleModal').modal('toggle');
    $('#viewArticleLabel').html($("#showArticle_" + id.toString()).html());
    $('#viewArticleBody').html($("#articleDesc_" + id.toString()).html());
}


function editArticle(id) {
    $('#inputEditArticleTitle').attr("value", $("#showArticle_" + id.toString()).html());

    $('#inputEditArticleBody').html($("#articleDesc_" + id.toString()).html());

    $('#inputEditArticleId').html(id);

    $('#editArticleModal').modal('toggle');

}

function editActivity(id) {
    $('#inputEditActivityTitle').attr("value", $("#showActivity_" + id.toString()).html());

    $('#inputEditActivityContent').html($("#activityDesc_" + id.toString()).html());

    var date = new Date($("#activityDate_" + id.toString()).html());
    var day = ("0" + date.getDate()).slice(-2);
    var month = ("0" + (date.getMonth() + 1)).slice(-2);
    var res = date.getFullYear()+"-"+(month)+"-"+(day) ;
    $('#inputEditActivityHoldDate').val(res);

    $('#inputEditActivityCountry').val($("#activityCountry_" + id.toString()).html());

    $('#inputEditActivityId').html(id);

    $('#editActivityModal').modal('toggle');
}

function addArticle() {
    $('#addArticleModal').modal('toggle');

}

function addActivity() {
    $('#addActivityModal').modal('toggle');

}


function sendEditArticle() {
    id = $('#inputEditArticleId').html();
    var article = {
        "id": id,
        "title": $('#inputEditArticleTitle').val(),
        "body": $('#inputEditArticleBody').val(),
        "charityId": $("#charityIdDiv").html()
    };
    $.ajax({
        type: "PATCH",
        contentType: "application/json",
        url: "/article",
        data: JSON.stringify(article),
        success: function () {
            $("#showArticle_" + id).html(article.title);
            $("#articleDesc_" + id).html(article.body);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
    $('#editArticleModal').modal('toggle');
}

function sendEditActivity() {
    id = $('#inputEditActivityId').html();
    var activity = {
        "id": id,
        "title": $('#inputEditActivityTitle').val(),
        "country": $('#inputEditActivityCountry').val(),
        "content": $('#inputEditActivityContent').val(),
        "holdDate": $('#inputEditActivityHoldDate').val()
    };
    $.ajax({
        type: "PATCH",
        contentType: "application/json",
        url: "/activity",
        data: JSON.stringify(activity),
        success: function () {
            $("#showActivity_" + id).html(activity.title);
            $("#activityDesc_" + id).html(activity.content);
            $("#activityCountry_" + id).html(activity.country);
            $("#activityDate_" + id).html("Hold Date: " + activity.holdDate);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
    $('#editActivityModal').modal('toggle');
}

function sendAddArticle() {
    var article = {
        "title": $('#inputNewArticleTitle').val(),
        "body": $('#inputNewArticleBody').val(),
        "charityId": $("#charityIdDiv").html()
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/article",
        data: JSON.stringify(article),
        success: function () {
            location.reload(true);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
    $('#editArticleModal').modal('toggle');
}

function sendAddActivity() {
    var activity = {
        "title": $('#inputNewActivityTitle').val(),
        "country": $('#inputNewActivityCountry').val(),
        "content": $('#inputNewActivityContent').val(),
        "holdDate": $('#inputNewActivityHoldDate').val()
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/activity/" + $("#charityIdDiv").html(),
        data: JSON.stringify(activity),
        success: function () {
            location.reload(true);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
    $('#editActivityModal').modal('toggle');
}

function sendDeleteArticle() {
    var id = $('#inputDeleteArticleId').html();
    $.ajax({
        type: "DELETE",
        contentType: "application/json",
        url: "/article/" + id,
        success: function () {
            location.reload(true);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
    $('#editArticleModal').modal('toggle');
}

function sendDeleteActivity() {
    var id = $('#inputDeleteActivityId').html();
    $.ajax({
        type: "DELETE",
        contentType: "application/json",
        url: "/activity/" + id,
        success: function () {
            location.reload(true);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
    $('#editActivityModal').modal('toggle');
}

function deleteArticle(id) {
    $('#deleteArticleBody').html(areYouSureText + " \"" + $("#showArticle_" + id.toString()).html() + "\"");
    $('#inputDeleteArticleId').html(id);
    $('#deleteArticleModal').modal('toggle');
}

function deleteActivity(id) {
    $('#deleteActivityBody').html(areYouSureText + " \"" + $("#showActivity_" + id.toString()).html() + "\"");
    $('#inputDeleteActivityId').html(id);
    $('#deleteActivityModal').modal('toggle');
}

function applyVolunteer(id) {
    var activity = {
        "id": id.toString()
    };
    $.ajax({
        type: "PATCH",
        contentType: "application/json",
        url: "/activity/volunteer",
        data: JSON.stringify(activity),
        success: function (result) {
            if (result == -1)
                alert("You need to log in!");
            if (result == -2)
                alert("You have already applied to volunteer for this activity!");
            if (result == -3)
                alert("Please login as Donor!");
            if (result > 0) {
                alert("You successfully applied to volunteer for this activity!");
                $("#activityDonorNumber_" + id.toString()).html("Current volunteers : "+result);
            }
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
}

function showActivity(id) {
    $('#viewActivityModal').modal('toggle');
    $('#viewActivityLabel').html($("#showActivity_" + id.toString()).html());
    $('#viewActivityContent').html($("#activityDesc_" + id.toString()).html());
    $('#viewActivityCountry').html($("#activityCountry_" + id.toString()).html());
    $('#viewActivityDate').html($("#activityDate_" + id.toString()).html());
}

function loadDescriptionModal() {
    $("#inputDescription").html($("#charityDesc").html());
}

function editCharityName() {
    $("#charityName").html($("#inputName").val());
    sendUpdateCharity();
    $("#editNameModal").modal('toggle');
}

function editCharityMail() {
    $("#charityEmail").html($("#inputMail").val());
    sendUpdateCharity();
    $("#addMailModal").modal('toggle');
}

function editCharityDesc() {
    $("#charityDesc").html($("#inputDescription").val());
    sendUpdateCharity();
    $("#editDescriptionModal").modal('toggle');
}
function sendUpdateCharityThumbUp() {
    var charity = {
        "id": $("#charityIdDiv").html()
    };
    $.ajax({
        type: "PATCH",
        contentType: "application/json",
        url: "/charity/thumbUpUnique",
        data: JSON.stringify(charity),
        success: function (result) {
            if (result == -1)
                alert("You need to log in!");
            if (result == -2)
                alert("Your thumbs up is already registered!");
            if (result == -3)
                alert("Please log in as a valid donor!");
            if (result > 0)
               $("#charityThumbUp").html("Thumbs : " +result);
        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
}

function sendUpdateCharity() {
    var charity = {
        "id": $("#charityIdDiv").html(),
        "name": $("#charityName").html(),
        "description": $("#charityDesc").html(),
        "email": $("#charityEmail").html()
    };
    $.ajax({
        type: "PATCH",
        contentType: "application/json",
        url: "/charity",
        data: JSON.stringify(charity),
        success: function () {

        },
        error: function (e) {
            alert("There was an error communicating with the server");
            console.log("ERROR : ", e);
        }
    });
}




