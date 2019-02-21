$("form").on("submit", function(e){
    e.preventDefault();
    let s= $('#movieSearcher').val();
    fetch("/movies?title=" + s)
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            console.log(JSON.stringify(myJson));

            let movieTitle = $('<h2 class="movieTitle"></h2>');
            movieTitle.text(myJson.title);
            $(".movieResults").append(movieTitle);

            let movieRating = $('<p class="movieRating"></p>');
            movieRating.text("ImdbRating: " + myJson.imdbRating);
            $(".movieResults").append(movieRating);


            $("#bookMovie").removeClass("hidden");

        })
        .catch(function(error) {
            console.log('Looks like there was a problem: \n', error);
        });
});


$("#bookMovie").on("click", function(e){
    e.preventDefault();
    fetch("/availableTimes")
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {

            for(let i = 0; i < myJson.length; i++) {

                var timeStart = myJson[i].start.value;
                var timeEnd = myJson[i].end.value;

                var dateStart = new Date(timeStart);
                var dateEnd = new Date(timeEnd);

                console.log(JSON.stringify("Start: " + dateStart + ", End: " + dateEnd));

                let p = $('<p class="timeItem"></p>');
                let button = $('<button class="bookItem">Book</button>');
                button.data({start:timeStart, end:timeEnd});
                p.text("Start: " + dateStart.toLocaleString() + " -  End: " + dateEnd.toLocaleString());
                $(".availableTimes").append(p, button);

            }

        });
});