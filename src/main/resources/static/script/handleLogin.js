
$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "/user",
        success: function (data, textStatus, xhr) {
            console.log(data);
            if (xhr.status === 200 && data.hasOwnProperty("userAuthentication")) {
                console.log(xhr);
                var matchCharityPage = /charityPage\/*[0-9]*\/*$/;
                var charityPageString = matchCharityPage.exec(window.location.pathname);

                $(".nameLabel").html(data.userAuthentication.details.name);
                $("#userIdSpan").html(data.userAuthentication.details.id);

                $(".unauthenticated").hide();
                $(".authenticated").show();

                if (charityPageString !== "" && $("#userIsCharity").html() === "true" && $("#userCharityId").html() === $("#charityIdDiv").html()) {
                    $("#editLink").toggle();
                } else {
                    $("#active-button").hide();
                }
            } else {
                $(".unauthenticated").show();
                $(".authenticated").hide();
            }
        },
        error: function (xhr, textStatus) {
            $(".unauthenticated").show();
            $(".authenticated").hide();
        }
    });
});



function logOutUser() {
    $.ajax({
        type: "POST",
        url: "/logout",
        beforeSend: function (xhr, settings) {
            if (settings.type == 'POST' || settings.type == 'PUT'
                || settings.type == 'DELETE') {
                xhr.setRequestHeader("X-XSRF-TOKEN",
                    Cookies.get('XSRF-TOKEN'));
            }
        },
        success: function (data, textStatus, xhr) {
            $(".nameLabel").html('');
            $(".unauthenticated").show();
            $(".authenticated").hide();
        },
        error: function (xhr, textStatus) {
            console.log(xhr)
        }
    });
}
