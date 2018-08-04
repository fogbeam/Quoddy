(function($){

  $.fn.termifier = function(actionURL,options) {
    var settings = $.extend({
      origin: {top:0,left:0},
      paramName: 'term',
      addClass: null,
      actionURL: actionURL
    },options||{});
    this.click(function(event){
      $('div.termifier').remove();
      $('<div>')
        .addClass('termifier' +
          (settings.addClass ? (' ') + settings.addClass : ''))
        .css({
          position: 'absolute',
          top: event.pageY - settings.origin.top,
          left: event.pageX - settings.origin.left,
          display: 'none'
        })
        .click(function(event){
          $(this).fadeOut('slow');
        })
        .appendTo('body')
        .append(
          $('<div>').load(
            settings.actionURL,
            encodeURIComponent(settings.paramName) + '=' +
              encodeURIComponent($(event.target).text()),
            function(){
              $(this).closest('.termifier').fadeIn('slow');
            }
          )
        );
    });
    this.addClass('termified');
    return this;
  };

})(jQuery);
