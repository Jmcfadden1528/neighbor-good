$(function (){

var = $time = $('#time');

 $.ajax({
    type: 'GET',
    url: '/event/add',
    success: function(time) {

    }
 });

 $('#add event').on('click', function() {

    var time = {
    time: $time.val();
    };

    $.ajax({
    type: 'POST'
    url: '/add/event',
    data: time,
    success: function(time) {

    }
    })
 })

});