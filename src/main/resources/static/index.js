
$("#submitButton").on("click", function() {
    // e.preventDefault();
    $(".movieResults").empty();
});


$("body").on("click",".bookItem", function() {
    console.log( $(this).data() );
    var data = $(this).data();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/booking',
        data: JSON.stringify(data),
        dataType: "json"
    });
});


