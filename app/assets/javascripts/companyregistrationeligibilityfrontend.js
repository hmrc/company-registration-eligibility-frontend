$(document).ready(function() {


  // Details/summary polyfill from frontend toolkit
  GOVUK.details.init()

  // =====================================================
  // Initialise show-hide-content
  // Toggles additional content based on radio/checkbox input state
  // =====================================================
  var showHideContent, mediaQueryList;
  showHideContent = new GOVUK.ShowHideContent()
  showHideContent.init()

  // =====================================================
  // Back link mimics browser back functionality
  // =====================================================
  $('#back-link').on('click', function(e){
    e.preventDefault();
    window.history.back();
  })


});
