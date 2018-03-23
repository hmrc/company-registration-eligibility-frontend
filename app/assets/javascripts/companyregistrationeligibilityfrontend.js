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

  var osyes = $("#ordinaryShares-yes");
  var osno = $("#ordinaryShares-no");
  var dontknow = $("#ordinaryShares-dk");
  var dontknowtoggle = $("#dk-hidden");

  if(dontknow.is(":checked")){
    dontknowtoggle.show();
  } else {
    dontknowtoggle.hide();
  }

  osyes.on("change", function () {
    if(dontknow.is(":checked")){
       dontknowtoggle.show();
    } else {
       dontknowtoggle.hide();
    }
  });

  osno.on("change", function () {
    if(dontknow.is(":checked")){
      dontknowtoggle.show();
    } else {
      dontknowtoggle.hide();
    }
  });

  dontknow.on("change", function () {
      if(dontknow.is(":checked")){
        dontknowtoggle.show();
      } else {
        dontknowtoggle.hide();
      }
  });

  // =====================================================
  // Back link mimics browser back functionality
  // =====================================================
  $('#back-link').on('click', function(e){
    e.preventDefault();
    window.history.back();
  })


});
