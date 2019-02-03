$(document).ready(function () {

    // Use to display the description of charity correctly.
    $(".charity-desc-p").each(function () {
        if ($(this).html().length > 300) {
            var words = $(this).html().substring(0, 300).split(" ");
            if (words.length > 1) {
                words = words.slice(0, -1);
            }
            $(this).html(words.join(" ") + " ...");
        }
    });


});


function changeCauseName(name) {
    $("#causeButton").html(name);
    $("#causeResult").attr("value", name);
}

function changeCountryName(name) {
    $("#countryButton").html(name);
    $("#countryResult").attr("value", name);
}


function searchBarKeyDown(key) {
    if (key.keyCode === 13)
        $('#searchFormSubmit').click();
}