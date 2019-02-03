$(document).ready(function () {


    $(".charity-desc-p").each(function () {
        if ($(this).html().length > 110) {
            var words = $(this).html().substring(0, 110).split(" ");
            if (words.length > 1) {
                words = words.slice(0, -1);
            }
            $(this).html(words.join(" ") + " ...");
        }
    });

    $(".charity-name-p").each(function () {
        if ($(this).html().length > 30) {
            var words = $(this).html().substring(0, 30).split(" ");
            if (words.length > 1) {
                words = words.slice(0, -1);
            }
            $(this).html(words.join(" ") + " ...");
        }
    });


});

function searchBarKeyDown(key) {
    if (key.keyCode === 13)
        $('#searchFormSubmit').click();
}


function changeCauseName(name){
    $("#causeButton").html(name);
    $("#causeResult").attr("value",name);
}

function changeCountryName(name){
    $("#countryButton").html(name);
    $("#countryResult").attr("value",name);
}
