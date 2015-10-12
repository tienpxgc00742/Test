/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function () {
    $.ajax({
        url: 'getPlayers',
        beforeSend: function () {
            $('#tablebody').append("<div class=\"spinner\"> <div class=\"rect1\"></div> <div class=\"rect2\"></div> <div class=\"rect3\"></div> <div class=\"rect4\"></div> <div class=\"rect5\"></div> </div>");
            $('.spinner').show();
        },
        complete: function () {
            $('.spinner').hide();
        },
        success: function (responseText) {
            $('#tablebody').append(responseText);
        }
    });
});