// submit modificació només si no hi ha errors
$("form.container").on("submit", function(e) {
	e.preventDefault();
	var ok = true; 	
	// estat d'ànim
	if ($("form.container #estat").val().length < 1) {
		$("form.container #estat").siblings('obligatori').html("<span>Camp obligatori!</span>");
		ok = false;
	} else {
		$("form.container #estat").siblings('obligatori').html("<obligatori></obligatori>");
	}
	// posició
    if ($("form.container #posicio").val() == "0") {
        $('form.container #posicio').siblings('obligatori').html("<span>Camp obligatori!</span>");
        ok = false;
    } else {
        $('form.container #posicio').siblings('obligatori').html("<obligatori></obligatori>");
    }   
    // submit només si no hi ha errors
    if (ok)		{
		$("form.container").unbind('submit').submit();
		$("div.alert").removeClass("d-none");
    } else {
		$("div.alert").addClass("d-none");
	}
});