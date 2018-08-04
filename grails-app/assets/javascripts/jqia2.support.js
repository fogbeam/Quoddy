(function($) {

  $.forDisplay = function(target) {
    if (target == null) return 'null';
    if (typeof target.toSource !== 'undefined' && typeof target.callee === 'undefined') {
      return target.toSource().slice(1, -1);
    }
    switch (typeof target) {
      case 'number':
      case 'boolean':
      case 'function':
        return target;
        break;
      case 'string':
        return '\'' + target + '\'';
        break;
      case 'object':
        var result;
        if (target instanceof Date) {
          result = 'new Date(' + target.getTime() + ')';
        }
        else if (target.constructor === Array || typeof target.callee !== 'undefined') {
          result = '[';
          var i, length = target.length;
          for (i = 0; i < length - 1; i++) {
            result += $.forDisplay(target[i]) + ',';
          }
          result += $.forDisplay(target[i]) + ']';
        }
        else {
          result = '{';
          var key;
          for (key in target) {
            result += key + ':' + $.forDisplay(target[key]) + ',';
          }
          result = result.replace(/\,$/, '') + '}';
        }
        return result;
        break;
      default:
        return '?unsupported-type?';
        break;
    }
  };

  $.fn.labModule = function() {
    this.each(function(){
      $(this).addClass('body');
      $(this).wrap('<div class="module">');
      if ($(this).attr('data-module-id')!=null) $(this).closest('.module').attr('id',$(this).attr('data-module-id'));
      $('<div class="banner">')
          .insertBefore(this)
          .append($('<div>').addClass('leftCap'))
          .append($('<div>').addClass('rightCap'))
          .append($('<h2>').html($(this).attr('data-module')));

    });
  };

  $.collectOptions = function(options) {
    //
    // boolean options
    //
    $('.booleanOption:checked').each(function(){
      if ($(this).val()!='') options[this.name] = $(this).val() == 'true';
    });
    //
    // radio options
    //
    $('.radioOption:checked').each(function(){
      if ($(this).val()!='') options[this.name] = $(this).val();
    });
    //
    // value options
    //
    $('.valueOption').each(function(){
      if ($(this).val()!='') options[this.name] = $(this).val();
    });
    //
    // eval options
    //
    $('.evalOption').each(function(){
      if ($(this).val()!='') options[this.name] = eval($(this).val());
    });
    return options;
  };

  $.fn.labConsole = function(settings,value) {
    if (typeof settings === 'string') {
      switch (settings) {
        case 'clear':
          this.clear();
          break;
        case 'say':
          var now = new Date();
          this.each(function(){
            if ($(this).hasClass('jqia2-console')) {
              var options = $(this).data('jqia2-console');
              var text = '';
              if (options.showTime) text += ('At ' + (now.getHours() < 10 ? '0' : '') + now.getHours() +
                  ':' + (now.getMinutes() < 10 ? '0' : '') + now.getMinutes() +
                  ':' + (now.getSeconds() < 10 ? '0' : '') + now.getSeconds() +
                  '.' + (now.getMilliseconds() < 10 ? '00' : (now.getMilliseconds() < 100 ? '0' : '')) + now.getMilliseconds() +
                  ' - ');
              text += value;
              $(this)[options.reverse ? 'prepend' : 'append']($('<div>' + text + '</div>').addClass('jqia2-console-message'));
            }
          });
      }
    }
    else {
      settings = $.extend({
        reverse: false,
        showTime: true
      },settings);
      this
        .addClass('jqia2-console')
        .data('jqia2-console',settings);
      return this;
    }
  };

  $(function(){

    $('[data-module]').labModule();

    if ($('#console').length == 0) $('<div>').attr('id', 'console').appendTo('body');

  });

})(jQuery);

function say(text) {
  if (!$('#console').hasClass('jqia2-console')) $('#console').labConsole();
  $('#console').labConsole('say',text);
}
