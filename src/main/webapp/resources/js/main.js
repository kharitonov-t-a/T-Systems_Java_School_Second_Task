$(document).ready(function(){
    var carousel = $("#carousel");
    carousel.owlCarousel({pagination: true});

    $('#js-prev').click(function () {
        carousel.trigger('owl.prev');
        return false;
    });

    $('#js-next').click(function () {
        carousel.trigger('owl.next');
        return false;
    });
});