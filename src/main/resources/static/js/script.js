$('input[type="radio"]').click(function()){
         if($(this).attr("value")=="once"){
             $(".Box").hide('slow');
         }
         if($(this).attr("value")=="weekly"){
             $(".Box").show('slow');

         }
     });
 $('input[type="radio"]').trigger('click');
