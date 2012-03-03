!function( $j ){

  "use strict"

 /* DROPDOWN CLASS DEFINITION
  * ========================= */

  var toggle = '[data-toggle="dropdown"]'
    , Dropdown = function ( element ) {
        var $jel = $j(element).on('click.dropdown.data-api', this.toggle)
        $j('html').on('click.dropdown.data-api', function () {
          $jel.parent().removeClass('open')
        })
      }

  Dropdown.prototype = {

    constructor: Dropdown

  , toggle: function ( e ) {
      var $jthis = $j(this)
        , selector = $jthis.attr('data-target')
        , $jparent
        , isActive

      if (!selector) {
        selector = $jthis.attr('href')
        selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
      }

      $jparent = $j(selector)
      $jparent.length || ($jparent = $jthis.parent())

      isActive = $jparent.hasClass('open')

      clearMenus()
      !isActive && $jparent.toggleClass('open')

      return false
    }

  }

  function clearMenus() {
    $j(toggle).parent().removeClass('open')
  }


  /* DROPDOWN PLUGIN DEFINITION
   * ========================== */

  $j.fn.dropdown = function ( option ) {
    return this.each(function () {
      var $jthis = $(this)
        , data = $jthis.data('dropdown')
      if (!data) $jthis.data('dropdown', (data = new Dropdown(this)))
      if (typeof option == 'string') data[option].call($jthis)
    })
  }

  $j.fn.dropdown.Constructor = Dropdown

$j('.dropdown-toggle').dropdown()

}( window.jQuery );
